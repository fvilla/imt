package org.integratedmodelling.thinkscape;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

public abstract class TreeModel {
	
	TreeViewer viewer;
	ViewPart   view;
	TreeObject rooth = null;
	ArrayList<Object> index = new ArrayList<Object>();
	

	public TreeModel(TreeViewer viewer, ViewPart view) {
		this.viewer = viewer;
		this.view = view;
	}
	
	/**
	 * Return the objects corresponding to the given filter, so they can
	 * be used to instrument another tree (or self).
	 * 
	 * @param txt
	 * @return
	 */
	public ArrayList<Object> filter(Object filter) {

		ArrayList<Object> filtered = new ArrayList<Object>();
		for (Object c : index) {
			if (matchesFilter(c, filter)) {
				filtered.add(c);
			}
		}
		return filtered;
	}
	
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	private boolean matchesFilter(Object c, Object filter) {
		return filter == null;
	}

	public abstract String getName(Object object, int column);
	
	public abstract Image getImage(Object object, int column);

	public abstract Object[] getChildren(Object object);

	public class TreeObject implements IAdaptable {
		
		public Object data;
		public TreeObject parent;
		private ArrayList<TreeObject> children;
		
		public TreeObject(Object data) {
			this.data = data;
			Object[] ch = TreeModel.this.getChildren(data);
			if (ch != null) {
				children = new ArrayList<TreeObject>();
				for (Object o : ch) {
					TreeObject to = new TreeObject(o);
					to.setParent(this);
					children.add(to);
				}
			}
		}
		public String getName(int column) {
			return data == null ? "" : TreeModel.this.getName(data, column);
		}
		public void setParent(TreeObject parent) {
			this.parent = parent;
		}
		public TreeObject getParent() {
			return parent;
		}
		public String toString() {
			return TreeModel.this.getName(data, 0);
		}
		public Object getAdapter(Class key) {
			return null;
		}
		public void addChild(TreeObject child) {
			if (children == null)
				children = new ArrayList<TreeObject>();
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject[] getChildren() {
			return children == null ? new TreeObject[0] : (TreeObject[])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children != null;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TreeObject) 
				return data == null ? 
						(((TreeObject)obj).data == null) : 
						(data instanceof TreeObject && data.equals(((TreeObject)obj).data));
						
			return data == null ?
					obj == null :
					data.equals(obj);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return data.hashCode();
		}
	}
	

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeObject invisibleRoot;
		private Collection<?> roots;
		
		ViewContentProvider(Collection<?> roots) {
			this.roots = roots;
		}
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(view.getViewSite())) {
				if (invisibleRoot == null) 
					initialize(roots);
				return invisibleRoot.getChildren();
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			return ((TreeObject)child).parent;
		}
		public Object[] getChildren(Object parent) {
			return ((TreeObject)parent).getChildren();
		}
		
		public boolean hasChildren(Object parent) {
			return ((TreeObject)parent).hasChildren();
		}

		private void initialize(Collection<?> roots) {
			this.invisibleRoot = new TreeObject(null);
			if (roots != null)
				for (Object o : roots)
					this.invisibleRoot.addChild(new TreeObject(o));
		}		
	}
	
	class ViewLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return TreeModel.this.getImage(((TreeObject)element).data, columnIndex);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((TreeObject)element).getName(columnIndex);
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
		}
	}

	class NameSorter extends ViewerSorter {
	}
	
	/**
	 * Setup the passed tree to show the hierarchy of the concepts starting
	 * at root, or owl:Thing if null.
	 * 
	 * @param viewer
	 * @param root
	 */
	public void instrumentConceptTree(Collection<?> roots) {
		
		viewer.setContentProvider(new ViewContentProvider(roots));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(view.getViewSite());
		viewer.refresh();
	}

	public void handleDoubleClick(Object obj, IWorkbenchPage page) {
		TreeObject o = (TreeObject) obj;
		
	}


}
