package org.integratedmodelling.thinkscape.views;


import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.NewProjectAction;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


/**
 * The "navigator" for Thinklab projects.
 */
public class PluginView extends ViewPart {
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.integratedmodelling.thinkscape.views.PluginView";

	
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

	
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			Collection<Bundle> plugins;
			try {
				plugins = Thinklab.getThinklabPlugins();
			} catch (ThinklabException e) {
				throw new ThinklabRuntimeException(e);
			}
			Bundle[] ret = new Bundle[plugins.size()];
			
			int i = 0;
			for (Bundle p : plugins)
				ret[i++] = p;
			
			return ret;
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return ((Bundle)obj).getSymbolicName();
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	private TreeViewer treeViewer;

	/**
	 * The constructor.
	 */
	public PluginView() {
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
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
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