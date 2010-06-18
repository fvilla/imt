package org.integratedmodelling.thinkscape.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.integratedmodelling.thinkscape.wizards.NewSourceWizard;
import com.swtdesigner.ResourceManager;

public class Sources extends ViewPart implements IPropertyChangeListener {

	public static final String ID = "org.integratedmodelling.thinkscape.views.Sources"; //$NON-NLS-1$

	public class SourceTreeModel extends TreeModel {

		public SourceTreeModel(TreeViewer viewer, ViewPart view) {
			super(viewer, view);
		}

		@Override
		public Object[] getChildren(Object object) {
			
			if (object instanceof SemanticAnnotationContainer) {
				SemanticAnnotationContainer sc = (SemanticAnnotationContainer)object;
				ArrayList<String> al = new ArrayList<String>();
				for (String ss : sc.getSourceIds()) {
					if (sc.getAnnotationForSource(ss) != null) {
						ss = ss + "@";
					}
					al.add(ss);
				}
				Collections.sort(al);
				return al.toArray(new String[al.size()]);
			}
			return null;
		}

		@Override
		public Image getImage(Object object, int column) {
			String ret = "icons/bullet_orange.png";
			if (object instanceof SemanticAnnotationContainer) {
					ret = "icons/database_refresh.png";
			} else if (object instanceof String) {
				if (object.toString().endsWith("@"))
					ret = "icons/pencil.png";
			}
			return ResourceManager.getPluginImage(
					"org.integratedmodelling.thinkscape", ret);
		}

		@Override
		public String getName(Object object, int column) {
			if (object instanceof SemanticAnnotationContainer) {
				return ((SemanticAnnotationContainer)object).getSourceUrl();
			} else if (object instanceof String) {
				return object.toString().endsWith("@") ? 
						object.toString().substring(0, object.toString().length()-2) :
						object.toString();
				
			}
			return null;
		}
		
	}

	private TreeViewer treeViewer;
	private SourceTreeModel sourceTreeModel;
	
	public Sources() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		final Composite ps = parent;
		parent.setLayout(new GridLayout(1, false));
		{
			ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
			{
				ToolItem tltmNewSource = new ToolItem(toolBar, SWT.NONE);
				tltmNewSource.setToolTipText("Add a new source of observations");
				tltmNewSource.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {

				        NewSourceWizard wizard = new NewSourceWizard();
						wizard.init(getSite().getWorkbenchWindow().getWorkbench(),
						            (IStructuredSelection)null);
				        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
				        dialog.setBlockOnOpen(true);
				        int returnCode = dialog.open();
				        if(returnCode == Dialog.OK)
				          System.out.println("OK");
				        else
				          System.out.println("Cancelled");
					}
				});
				tltmNewSource.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/folder_add.png"));
			}
		}
		{
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			composite.setLayout(new TreeColumnLayout());
			{
				this.treeViewer = new TreeViewer(composite, SWT.BORDER);
				Tree tree = treeViewer.getTree();
				tree.setHeaderVisible(true);
				tree.setLinesVisible(true);
				{
					DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE);
					dropTarget.addDropListener(new DropTargetAdapter() {
						public void drop(DropTargetEvent event) {
							System.out.println("PORK! DROP!" + event + "!");
						}
						@Override
						public void dropAccept(DropTargetEvent event) {
							System.out.println("E ALORA! DROP!" + event + "!");
						}
					});
				}
			}
		}

		this.sourceTreeModel = new SourceTreeModel(treeViewer, this);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	

	private void rescan() throws ThinklabException {
		ThinklabProject project = ThinkScape.getActiveProject();
		this.sourceTreeModel.instrumentConceptTree(
				project == null ? null : project.getSemanticSources());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(ThinkscapeEvent.WORKSPACE_CHANGE) ||
			event.getProperty().equals(ThinkscapeEvent.ANNOTATION_SOURCE_CONNECTED)) {
				ThinkScape.getDefault().getWorkbench().getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							rescan();
						} catch (ThinklabException e) {
							throw new ThinklabRuntimeException(e);
						}
					}

				});
		}
	}

}
