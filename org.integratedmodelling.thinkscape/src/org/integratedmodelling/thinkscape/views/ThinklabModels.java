package org.integratedmodelling.thinkscape.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ThinklabModels extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ThinklabModels"; //$NON-NLS-1$
	private Text text;

	public ThinklabModels() {
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
					TreeViewer treeViewer = new TreeViewer(tabFolder, SWT.BORDER);
					Tree tree = treeViewer.getTree();
					tbtmModels.setControl(tree);
				}
			}
			{
				TabItem tbtmScenarios = new TabItem(tabFolder, SWT.NONE);
				tbtmScenarios.setText("Scenarios");
				{
					TreeViewer treeViewer = new TreeViewer(tabFolder, SWT.BORDER);
					Tree tree = treeViewer.getTree();
					tbtmScenarios.setControl(tree);
				}
			}
			{
				TabItem tbtmAgents = new TabItem(tabFolder, SWT.NONE);
				tbtmAgents.setText("Agents");
				{
					TreeViewer treeViewer = new TreeViewer(tabFolder, SWT.BORDER);
					Tree tree = treeViewer.getTree();
					tbtmAgents.setControl(tree);
				}
			}
			{
				TabItem tbtmObservables = new TabItem(tabFolder, SWT.NONE);
				tbtmObservables.setText("Observables");
				{
					TreeViewer treeViewer = new TreeViewer(tabFolder, SWT.BORDER);
					Tree tree = treeViewer.getTree();
					tbtmObservables.setControl(tree);
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

}
