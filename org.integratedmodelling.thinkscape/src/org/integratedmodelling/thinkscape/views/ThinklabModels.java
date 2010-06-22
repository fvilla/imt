package org.integratedmodelling.thinkscape.views;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.modeleditor.model.ModelNamespace;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.integratedmodelling.thinkscape.wizards.ImportModelWizard;
import org.integratedmodelling.thinkscape.wizards.NewModel;

import com.swtdesigner.ResourceManager;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;

public class ThinklabModels extends ViewPart implements IPropertyChangeListener {

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
			Object[] ret = null;
			if (object instanceof ModelNamespace) {
				Collection<Model> models = ((ModelNamespace)object).getRootModels();
				ret = new Model[models.size()]; int i = 0;
				for (Model m : models) {
					ret[i++] = m;
				}
			}
			return ret;
		}

		@Override
		public Image getImage(Object object, int column) {
			String ret = null;
			if (object instanceof ModelNamespace && column == 0) {
				ret = "icons/database_gear.png";
			} else if (object instanceof Model && column == 0) {
				ret = "icons/cog_go.png";
			} 
			return ret == null ? 
					null :
					ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", ret);
		}

		@Override
		public String getName(Object object, int column) {
			String ret = "";
			if (object instanceof ModelNamespace && column == 0) {
				ret = ((ModelNamespace)object).getNamespace();
			} else if (object instanceof Model && column == 0) {
				ret = ((Model)object).getModelName();
			} 
			return ret; 
		}
		
	}
	
	ModelTreeModel modelTreeModel = null;
	
	public ThinklabModels() {
		ThinkScape.getDefault().addPropertyChangeListener(this);
	}

	public void rescan() {
		ThinklabProject proj = ThinkScape.getActiveProject();
		modelTreeModel.instrumentConceptTree(proj.getModelNamespaces());
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
							        	
							        	ModelNamespace namespace = project.getModelNamespace(wizard.getNamespace());
							        	IFile file = namespace.getFile();
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
					tree.setLinesVisible(true);
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					tree.setBounds(0, 0, 85, 85);
					{
						TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
						treeColumn.setWidth(400);
						treeColumn.setText("New Column");
					}
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
							ToolItem tltmNew_1 = new ToolItem(toolBar, SWT.NONE);
							tltmNew_1.setText("New ");
							tltmNew_1.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_add.png"));
							tltmNew_1.setToolTipText("Add a new scenario namespace");
						}
						{
							ToolItem tltmImport_1 = new ToolItem(toolBar, SWT.NONE);
							tltmImport_1.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_go.png"));
							tltmImport_1.setText("Import");
						}
					}
					
					treeViewer_2 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_2.getTree();
					tree.setLinesVisible(true);
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					{
						TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer_2, SWT.NONE);
						TreeColumn treeColumn = treeViewerColumn.getColumn();
						treeColumn.setWidth(400);
						treeColumn.setText("");
					}
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
							ToolItem tltmNew_2 = new ToolItem(toolBar, SWT.NONE);
							tltmNew_2.setText("New ");
							tltmNew_2.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_add.png"));
							tltmNew_2.setToolTipText("Add a new agent namespace");
						}
						{
							ToolItem tltmImport_2 = new ToolItem(toolBar, SWT.NONE);
							tltmImport_2.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_go.png"));
							tltmImport_2.setText("Import");
						}
					}
					
					treeViewer_3 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_3.getTree();
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					tree.setLinesVisible(true);
					{
						TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer_3, SWT.NONE);
						TreeColumn treeColumn = treeViewerColumn.getColumn();
						treeColumn.setWidth(400);
						treeColumn.setText("");
					}
				}
			}
		}
		{
			text = new Text(parent, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}

		this.modelTreeModel = new ModelTreeModel(getModelTreeViewer(), this);
		rescan();
		
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(ThinkscapeEvent.MODEL_FILE_IMPORTED)) {
			ThinkScape.getDefault().getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					rescan();
				}
			});
		}
	}

}
