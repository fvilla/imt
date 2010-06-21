package org.integratedmodelling.thinkscape.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.integratedmodelling.thinkscape.wizards.ImportModelWizard;
import org.integratedmodelling.thinkscape.wizards.NewModel;

import com.swtdesigner.ResourceManager;

public class ThinklabModels extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ThinklabModels"; //$NON-NLS-1$
	private Text text;
	private ModelFactory manager;
	private TreeViewer treeViewer_1;
	private TreeViewer treeViewer_2;
	private TreeViewer treeViewer_3;

	public class ModelTreeModel extends TreeModel {

		public ModelTreeModel(TreeViewer viewer, ViewPart view) {
			super(viewer, view);
		}

		@Override
		public Object[] getChildren(Object object) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Image getImage(Object object, int column) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName(Object object, int column) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	public ThinklabModels() {


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
			TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			{
				TabItem tbtmModels = new TabItem(tabFolder, SWT.NONE);
				tbtmModels.setText("Models");
				{
					Composite composite = new Composite(tabFolder, SWT.NONE);
					tbtmModels.setControl(composite);
					composite.setLayout(new GridLayout(1, false));
					{
						ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
						toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						toolBar.setBounds(0, 0, 89, 23);
						{
							ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
							tltmNew.setText("New");
							tltmNew.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									IWorkbenchPage pg = getSite().getPage();
							        NewModel wizard = new NewModel();
									wizard.init(getSite().getWorkbenchWindow().getWorkbench(),
									            (IStructuredSelection)null);
							        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
							        dialog.setBlockOnOpen(true);
							        int returnCode = dialog.open();
							        
							        if (returnCode == WizardDialog.OK) {

							        	ThinklabProject project = ThinkScape.getProject(wizard.getProject(), true);
							        	
							        	IFile file = project.getModelNamespace(wizard.getNamespace());
							        	IEditorDescriptor desc = 
							        		PlatformUI.getWorkbench().
							        			getEditorRegistry().getDefaultEditor(file.getName());
							        	try {
							        		pg.openEditor(new FileEditorInput(file), desc.getId());
							        	} catch (PartInitException ee) {
							        		throw new ThinklabRuntimeException(ee);
							        	}
							        }

								}
							});
							tltmNew.setToolTipText("Add a new model namespace");
							tltmNew.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_add.png"));
						}
						{
							ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
							tltmImport.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									IWorkbenchPage pg = getSite().getPage();
							        ImportModelWizard wizard = new ImportModelWizard();
//									wizard.init(getSite().getWorkbenchWindow().getWorkbench(),
//									            (IStructuredSelection)null);
							        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
							        dialog.setBlockOnOpen(true);
							        dialog.open();
								}
							});
							tltmImport.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_go.png"));
							tltmImport.setText("Import");
						}
					}
					
					treeViewer_1 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_1.getTree();
					tree.setHeaderVisible(true);
					tree.setLinesVisible(true);
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					tree.setBounds(0, 0, 85, 85);
				}
			}
			{
				TabItem tbtmScenarios = new TabItem(tabFolder, SWT.NONE);
				tbtmScenarios.setText("Scenarios");
				{
					Composite composite = new Composite(tabFolder, SWT.NONE);
					tbtmScenarios.setControl(composite);
					composite.setLayout(new GridLayout(1, false));
					{
						ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
						toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						{
							ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
							toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/add.png"));
							toolItem.setToolTipText("Add a new scenario namespace");
						}
					}
					
					treeViewer_2 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_2.getTree();
					tree.setLinesVisible(true);
					tree.setHeaderVisible(true);
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				}
			}
			{
				TabItem tbtmAgents = new TabItem(tabFolder, SWT.NONE);
				tbtmAgents.setText("Agents");
				{
					Composite composite = new Composite(tabFolder, SWT.NONE);
					tbtmAgents.setControl(composite);
					composite.setLayout(new GridLayout(1, false));
					{
						ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
						toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						{
							ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
							toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/add.png"));
							toolItem.setToolTipText("Add a new agent namespace");
						}
					}
					
					treeViewer_3 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_3.getTree();
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					tree.setLinesVisible(true);
					tree.setHeaderVisible(true);
				}
			}
		}
		{
			text = new Text(parent, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
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
	protected TreeViewer getModelTreeViewer() {
		return treeViewer_1;
	}
	public TreeViewer getScenarioTreeViewer() {
		return treeViewer_2;
	}
	public TreeViewer getAgentTreeViewer() {
		return treeViewer_3;
	}

}
