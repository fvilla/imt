package org.integratedmodelling.thinkscape.views;


import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.modeleditor.model.ModelNamespace;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * The "navigator" for Thinklab projects.
 * 
 * TODO double-click projects to select the active project; ensure the active project shows up differently.
 */
public class PluginView extends ViewPart implements IPropertyChangeListener {
	
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
				ret[1] = ((ThinklabProject)object).annotPath;
				ret[2] = ((ThinklabProject)object).modelPath;
				
			} else if (object instanceof IFolder) {
				
				ThinklabProject project = 
					ThinkScape.getProject(((IFolder)object).getProject().getName(), false);
				
				if (object.toString().contains("models")) {
					
					Collection<ModelNamespace> models = 
						project.getModelNamespaces();
					ret = new ModelNamespace[models.size()];
					int i = 0;
					for (ModelNamespace c : models)
						ret[i++] = c;
					
				} else if (object.toString().contains("annotations")) {
					
					Collection<SemanticAnnotationContainer> annots = 
						project.getAnnotationNamespaces();
					ret = new SemanticAnnotationContainer[annots.size()];
					int i = 0;
					for (SemanticAnnotationContainer c : annots)
						ret[i++] = c;
					
				} else if (object.toString().contains("ontologies")) {
				} 
			} else if (object instanceof ModelNamespace) {
				
				ret = ((ModelNamespace)object).getModels().
					toArray(new Model[((ModelNamespace)object).getModels().size()]);
				
			} else if (object instanceof SemanticAnnotationContainer) {

				Collection<String> ann = ((SemanticAnnotationContainer)object).getAnnotationIds();
				ret = new SemanticAnnotation[ann.size()];
				int i = 0;
				for (String c : ann)
					ret[i++] = ((SemanticAnnotationContainer)object).getAnnotation(c);
				
				
			}
			return ret;
		}

		@Override
		public Image getImage(Object object, int column) {

			String ret = "icons/bullet_orange.png";
			if (object instanceof ThinklabProject) {
				ret = 
					((ThinklabProject)object).isActive() ?
							"icons/star_red.png" :
							"icons/folder_lightbulb.png";
			} else if (object instanceof IFolder) {
				if (object.toString().contains("models"))
					ret = "icons/database_gear.png";
				else if (object.toString().contains("annotations"))
					ret = "icons/database_edit.png";
				else if (object.toString().contains("ontologies"))
					ret = "icons/database_lightning.png";
			} else if (object instanceof IFile) {
				
			} else if (object instanceof SemanticAnnotationContainer) {
				ret =  "icons/database_edit.png";
			} else if (object instanceof ModelNamespace) {
				ret =  "icons/database_gear.png";
			} else if (object instanceof SemanticAnnotation) {
				if (((SemanticAnnotation)object).isValid()) {
					ret = "icons/pencil.png";
				} else {
					ret = "icons/error.png";
				}
			} else if (object instanceof Model) {
				ret = "icons/cog_go.png";
			}
			
			return ResourceManager.getPluginImage(
					"org.integratedmodelling.thinkscape", ret);
		}

		@Override
		public String getName(Object object, int column) {

			if (object instanceof ThinklabProject) {
				return ((ThinklabProject)object).getLabel();
			} else if (object instanceof IFolder) {
				if (object.toString().contains("/models"))
					return "Model Namespaces";
				else if (object.toString().contains("/annotations"))
					return "Annotations Namespaces";
				else if (object.toString().contains("/ontologies"))
					return "Observed Concepts";
				
			} else if (object instanceof SemanticAnnotationContainer) {
				return ((SemanticAnnotationContainer)object).getNamespace();
			} else if (object instanceof ModelNamespace) {
				return ((ModelNamespace)object).getNamespace();
			} else if (object instanceof SemanticAnnotation) {
				return ((SemanticAnnotation)object).getName();
			} else if (object instanceof Model) {
				return ((Model)object).getModelName();
			}
				
			return "";
		}

		/* (non-Javadoc)
		 * @see org.integratedmodelling.thinkscape.TreeModel#handleDoubleClick(java.lang.Object, org.eclipse.ui.IWorkbenchPage)
		 */
		@Override
		public void handleDoubleClick(Object object, IWorkbenchPage page) {

			if (object instanceof ThinklabProject) {
				((ThinklabProject)object).makeActive();
			} else if (object instanceof IFolder) {
				if (object.toString().contains("/models")) {
				} else if (object.toString().contains("/annotations")) {
				} else if (object.toString().contains("/ontologies")) {
				}
			} else if (object instanceof SemanticAnnotationContainer) {
			} else if (object instanceof ModelNamespace) {
			} else if (object instanceof SemanticAnnotation) {
			} else if (object instanceof Model) {
			}
		}
		
		
	}
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.integratedmodelling.thinkscape.views.PluginView";

	private ProjectTreeModel treeModel;
	
	private void rescan() throws ThinklabException {		
		this.treeModel.instrumentConceptTree(ThinkScape.getProjects());
	}
	
	/**
	* Create a new Project through a Wizard
	*
	* @return the newly created project
	*/
	private IProject displayNewProjectWizard() {

		NewProjectListener newProjectListener = new NewProjectListener();
		
		// make sure we get notified if anything changes the projects
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				newProjectListener,
				IResourceChangeEvent.POST_BUILD);
		
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
		ThinkScape.getDefault().addPropertyChangeListener(this);
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
		
		ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
		tltmNew.setText("New Project");
		tltmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IProject project = displayNewProjectWizard();
				if (project != null) {
					try {
						ThinklabProject tproj = new ThinklabProject(project);
					} catch (ThinklabException e1) {
						throw new ThinklabRuntimeException(e1);
					}
					/*
					 * TODO do something with the project? The listener should already
					 * take care of notifying it. I'm not sure there is a notion of
					 * current project in Eclipse.
					 */
				}
			}
		});
		tltmNew.setToolTipText("New Thinklab project");
		tltmNew.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_add.png"));
		
		this.treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				treeModel.handleDoubleClick(
						((TreeSelection)treeViewer.getSelection()).getFirstElement(),
						getSite().getPage());
			}
		});
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);
		
		this.treeModel = new ProjectTreeModel(treeViewer, this);
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn = treeViewerColumn.getColumn();
		treeColumn.setWidth(440);
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		try {
			rescan();
		} catch (ThinklabException e1) {
			throw new ThinklabRuntimeException(e1);
		}
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// we react to just about everything
		//		if (event.getProperty().equals(ThinkscapeEvent.WORKSPACE_CHANGE)) {
				ThinkScape.getDefault().getWorkbench().getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							rescan();
						} catch (ThinklabException e) {
							throw new ThinklabRuntimeException(e);
						}
					}
				});
//		}
	}
}