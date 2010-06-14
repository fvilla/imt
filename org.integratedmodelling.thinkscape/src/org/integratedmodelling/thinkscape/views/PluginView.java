package org.integratedmodelling.thinkscape.views;


import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.osgi.framework.Bundle;

import com.swtdesigner.ResourceManager;


/**
 * The "navigator" for Thinklab projects.
 */
public class PluginView extends ViewPart {
	
	class ProjectTreeModel extends TreeModel {

		public ProjectTreeModel(TreeViewer viewer, ViewPart view) {
			super(viewer, view);
		}

		@Override
		public Object[] getChildren(Object object) {
			
			Object[] ret = null;
			
			if (object instanceof ThinklabProject) {
				ret = new Object[3];
				ret[0] = ((ThinklabProject)object).ontoPath;
				ret[0] = ((ThinklabProject)object).annotPath;
				ret[0] = ((ThinklabProject)object).modelPath;
			}
			return ret;
		}

		@Override
		public Image getImage(Object object, int column) {

			
			if (object instanceof IProject) {
				
			}
			
			return null;
		}

		@Override
		public String getName(Object object, int column) {

			if (object instanceof IProject) {
				return ((ThinklabProject)object).getLabel();
			} else if (object instanceof IFolder) {
				return object.toString();
			}
			return "";
		}
		
	}
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.integratedmodelling.thinkscape.views.PluginView";

	private ArrayList<ThinklabProject> thinklabProjects;
	private ProjectTreeModel treeModel;
	
	private void rescan() {
		
		this.thinklabProjects = new ArrayList<ThinklabProject>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject proj : root.getProjects()) {
			if (ThinkScape.isThinklabProject(proj))
					thinklabProjects.add(new ThinklabProject(proj));
		}
		
		/*
		 * rebuild the list
		 */
		this.treeModel.instrumentConceptTree(thinklabProjects);
	}
	
	class ResourceListener implements IResourceChangeListener {
		@Override
		public void resourceChanged(IResourceChangeEvent arg0) {
			rescan();
		}
	}
	
	/**
	* Create a new Project through a Wizard
	*
	* @return the newly created project
	*/
	private IProject displayNewProjectWizard() {

		NewProjectListener newProjectListener = new NewProjectListener();
		// Adding a Listener to listen for post change events
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				newProjectListener,	
				IResourceChangeEvent.POST_CHANGE);
		NewProjectAction newProjectAction =
			new NewProjectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		newProjectAction.run();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(newProjectListener);
		return newProjectListener.getNewProjectCreated();
	}

	/**
	* A Project Creation Listener
	*/
	class NewProjectListener implements IResourceChangeListener {

		// The Project that will be created

		private IProject newProject_ = null;

		@Override
		public void resourceChanged(IResourceChangeEvent event) {

			IResourceDelta root = event.getDelta();

			// Get all the affected Children. One of them would be the newly created project
			IResourceDelta[] projectDeltas = root.getAffectedChildren();

			for (int i = 0; i < projectDeltas.length; i++) {

				// Get individual delta's
				IResourceDelta delta = projectDeltas[i];
				IResource resource = delta.getResource();

				if (delta.getKind() == IResourceDelta.ADDED) {
					// The New Project that has been created via the New Project
					// Wizard
					newProject_ = (IProject) resource;
				}
			}
		}

		/**
		 * Get the new Project Created
		 */
		public IProject getNewProjectCreated() {
			return newProject_;
		}
	}

	private TreeViewer treeViewer;

	public PluginView() {

		/*
		 * install a listener to rescan after each resource change
		 */
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new ResourceListener(),	
				IResourceChangeEvent.POST_CHANGE);

	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IProject project = displayNewProjectWizard();
				if (project != null) {
					ThinklabProject tproj = new ThinklabProject(project);
					try {
						tproj.initialize();
					} catch (ThinklabException e1) {
						throw new ThinklabRuntimeException(e1);
					}
					
				}
			}
		});
		toolItem.setToolTipText("New Thinklab project");
		toolItem.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/etool16/new_wiz.gif"));
		
		this.treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);
		
		this.treeModel = new ProjectTreeModel(treeViewer, this);
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		rescan();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				PluginView.this.fillContextMenu(manager);
			}
		});
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
	}

	private void makeActions() {
	}

	private void hookDoubleClickAction() {
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			treeViewer.getControl().getShell(),
			"Thinklab Projects",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
}