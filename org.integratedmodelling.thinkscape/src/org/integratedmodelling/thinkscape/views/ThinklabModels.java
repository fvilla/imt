package org.integratedmodelling.thinkscape.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.eclipse.swt.widgets.ToolBar;

public class ThinklabModels extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ThinklabModels"; //$NON-NLS-1$
	private Text text;
	private ModelFactory manager;
	private TreeViewer treeViewer_1;
	private TreeViewer treeViewer_2;
	private TreeViewer treeViewer_3;
	private TreeViewer treeViewer;

	public ThinklabModels() {

		IConcept c = CoreScience.get().Ranking();
		CoordinateReferenceSystem diozone = Geospace.get().getDefaultCRS();
		
		this.manager = ModellingPlugin.get().getModelManager();
		for (Model m : manager.getModels()) {
			System.out.println("here is a model: " + m);
		}
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
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
					}
					
					treeViewer_3 = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer_3.getTree();
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					tree.setLinesVisible(true);
					tree.setHeaderVisible(true);
				}
			}
			{
				TabItem tbtmObservables = new TabItem(tabFolder, SWT.NONE);
				tbtmObservables.setText("Observables");
				{
					Composite composite = new Composite(tabFolder, SWT.NONE);
					tbtmObservables.setControl(composite);
					composite.setLayout(new GridLayout(1, false));
					{
						ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
						toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					}
					
					treeViewer = new TreeViewer(composite, SWT.BORDER);
					Tree tree = treeViewer.getTree();
					tree.setHeaderVisible(true);
					tree.setLinesVisible(true);
					tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
	public TreeViewer getObservableTreeViewer() {
		return treeViewer;
	}
}
