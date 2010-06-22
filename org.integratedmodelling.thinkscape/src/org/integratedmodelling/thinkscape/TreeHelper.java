package org.integratedmodelling.thinkscape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IKnowledgeSubject;
import org.integratedmodelling.thinklab.plugin.IPluginLifecycleListener;

import com.swtdesigner.ResourceManager;

public class TreeHelper {
	
	ArrayList<IConcept> index;
	ArrayList<IConcept> indexh;
	TreeViewer viewer;
	ViewPart   view;
	IConcept   root = null;
	TreeObject rooth = null;
	
	/*
	 * ensure ontology tree is redefined if the plugin being loaded/unloaded provides ontologies
	 */
	class PluginListener implements IPluginLifecycleListener {

		@Override
		public void onPluginLoaded(IThinklabPlugin plugin) {
			if (plugin.getOntologiesLocation() != null) {
				rooth = null;
				viewer.setContentProvider(new ViewContentProvider());
				viewer.refresh();
			}
		}

		@Override
		public void onPluginUnloaded(IThinklabPlugin plugin) {
			if (plugin.getOntologiesLocation() != null) {
				rooth = null;
				viewer.setContentProvider(new ViewContentProvider());
				viewer.refresh();
			}
		}

		@Override
		public void prePluginLoaded(IThinklabPlugin thinklabPlugin) {
		}

		@Override
		public void prePluginUnloaded(IThinklabPlugin thinklabPlugin) {
		}
		
	}

	public TreeHelper(TreeViewer viewer, ViewPart view) {
		this.viewer = viewer;
		this.view = view;
		KnowledgeManager.registerPluginListener(new PluginListener());
	}
	
	/**
	 * Return the concepts corresponding to the given filter, so they can
	 * be used to instrument another tree (or self).
	 * 
	 * @param txt
	 * @return
	 */
	public ArrayList<IConcept> filter(String txt) {

		ArrayList<IConcept> filtered = new ArrayList<IConcept>();
		
		int i = 0; int start = -1; int end = -1;
		txt = txt.toLowerCase().trim();
		if (txt.equals(""))
			return null;
		
		for (IConcept c : index) {
			if (c.getLocalName().toLowerCase().startsWith(txt)) {
				start = i;
				break;
			}
			i++;
		}

		if (start >= 0) {
			end = start;
			while (end < index.size() && index.get(end).getLocalName().toLowerCase().startsWith(txt))
				end++;
		}
		
		for (i = start; i < end; i++) {
			filtered.add(index.get(i));
		}
		
		return filtered;
	}

	
	public class TreeObject implements IAdaptable {
		
		private IKnowledgeSubject concept;
		private TreeParent parent;
		String ontology;
		
		public TreeObject(IKnowledgeSubject name) {
			this.concept = name;
		}
		public String getName() {
			return concept == null ? "" : concept.getLocalName();
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return ontology == null ? getName() : ontology.toUpperCase();
		}
		public Object getAdapter(Class key) {
			return null;
		}
		public boolean isConcept() {
			return concept != null;
		}
		public boolean isOntology() {
			return ontology != null;
		}
		public Object getConcept() {
			return concept;
		}
	}
	
	class TreeParent extends TreeObject {
		
		private ArrayList<TreeObject> children;
		
		public TreeParent(IConcept concept) {
			super(concept);
			children = new ArrayList<TreeObject>();
		}
		
		public TreeParent(String conceptSpace) {
			super(null);
			ontology = conceptSpace;
			children = new ArrayList<TreeObject>();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(view.getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}

		private void initialize() {
			
			if (root == null)
				root = KnowledgeManager.Thing();
			
			if (root.equals(KnowledgeManager.Thing()))  {
				
				// cache root hierarchy for speed
				if (rooth == null) {
					indexh = new ArrayList<IConcept>();
					HashSet<IConcept> hash = new HashSet<IConcept>();					
					rooth = populate(root, indexh, hash);
					Collections.sort(indexh, new Comparator<IConcept>() {
						@Override
						public int compare(IConcept o1, IConcept o2) {
							return o1.getLocalName().compareTo(o2.getLocalName());
						}
					});
					index = indexh;
				}
				
				this.invisibleRoot = (TreeParent) rooth;
				index = indexh;
				
			} else {
				// not root; recreate hierarchy
				index = new ArrayList<IConcept>();
				HashSet<IConcept> hash = new HashSet<IConcept>();
				this.invisibleRoot = (TreeParent) populate(root, index, hash);
				Collections.sort(index, new Comparator<IConcept>() {
					@Override
					public int compare(IConcept o1, IConcept o2) {
						return o1.getLocalName().compareTo(o2.getLocalName());
					}
				});
			}
		}
		
		private TreeObject populate(IConcept r, ArrayList<IConcept> idx, HashSet<IConcept> refs) {

			if (refs.contains(r))
				return null;
			
			refs.add(r);
			Collection<IConcept> ch = r.getChildren();
			TreeObject ret = 
				ch.size() > 0 ?
					new TreeParent(r) :
					new TreeObject(r);
			
	        idx.add(r);
					
			if (ret instanceof TreeParent)
				for (IConcept c : ch) {
					TreeObject o = populate(c, idx, refs);
					if (o != null)
						((TreeParent)ret).addChild(o);
				}
			
			return ret;	
		}
	}
	class ViewLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				// TODO get image from concept annotation
				String imageKey = "icons/bullet_orange.png";
				if (element instanceof TreeParent) {
					if (((TreeParent)element).ontology != null) {
						imageKey = "icons/lightbulb.png";
					} else {
						imageKey = "icons/bullet_orange.png";
					}
				}
				return 
					ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", imageKey);
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO transfer getDescription to TreeObject and handle ontologies and other content intelligently
			return columnIndex == 0 ?
					element.toString() :
					((((TreeObject)element).concept == null) ?
							"" :
							((TreeObject)element).concept.getDescription());
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

	class ResultContentProvider implements IStructuredContentProvider, 
	   ITreeContentProvider {

		private TreeParent invisibleRoot = null;
		ArrayList<IConcept> list = null;
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(view.getViewSite())) {
				if (invisibleRoot == null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}

		private void initialize() {
			
			this.invisibleRoot = new TreeParent(KnowledgeManager.Thing());

			if (this.list.size() < 1)
				return;

			HashSet<IConcept> refs = new HashSet<IConcept>();
			TreeParent parent = new TreeParent(this.list.get(0).getConceptSpace());
			ArrayList<TreeObject> objs = new ArrayList<TreeObject>();

			for (int i = 0; i <= this.list.size(); i++) {

				IConcept cur = 
					i < this.list.size() ?
						this.list.get(i) :
						null;
	
				if (cur == null || cur.getConceptSpace() == null || !cur.getConceptSpace().equals(parent.ontology)) {
					for (TreeObject to : objs) {
						parent.addChild(to);
					}
					objs.clear();
					invisibleRoot.addChild(parent);
					if (cur != null)
						parent = new TreeParent(cur.getConceptSpace());
				} 
				if (!refs.contains(cur)) {
					objs.add(new TreeObject(cur));
					refs.add(cur);
				}
			}
			
		}

		public ResultContentProvider(ArrayList<IConcept> l) {
			this.list = l;
		}
		
	}

	
	/**
	 * Setup the passed tree to show the hierarchy of the concepts starting
	 * at root, or owl:Thing if null.
	 * 
	 * @param viewer
	 * @param root
	 */
	public void instrumentConceptTree(IConcept root) {
		
		this.root = root;
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(view.getViewSite());
		viewer.refresh();
	}

	/**
	 * Setup the passed tree to show flat lists of concepts grouped by ontology.
	 * Use all knowledge base if null is passed for the second argument.
	 * 
	 * @param viewer
	 * @param concepts
	 */
	public void instrumentOntologyTree(ArrayList<IConcept> concepts) {

		if (concepts == null) {
			concepts = new ArrayList<IConcept>(index.size());
			concepts.addAll(index);
		}
		
		Collections.sort(concepts, new Comparator<IConcept>() {
			@Override
			public int compare(IConcept o1, IConcept o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		viewer.setContentProvider(new ResultContentProvider(concepts));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(view.getViewSite());
		viewer.refresh();
	}

	public void handleDoubleClick(Object obj, IWorkbenchPage page) {
		
		// TODO open whatever editor we have associated with this object. This should only
		// happen when the tree view is active.
		TreeObject o = (TreeObject) obj;
		if (o.isConcept()) {
			
			/*
			 * fire up concept annotation editor; create concept axiom file if not there
			 */
			IFile file = 
				ThinkScape.getActiveProject().getNewConceptFile((IConcept) o.concept);
			IEditorDescriptor desc = 
				PlatformUI.getWorkbench().
				getEditorRegistry().getDefaultEditor(file.getName());
			try {
				page.openEditor(new FileEditorInput(file), desc.getId());
			} catch (PartInitException e) {
				throw new ThinklabRuntimeException(e);
			}
			
		} else if (o.isOntology()) {
			
		}
	}


}
