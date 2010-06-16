package org.integratedmodelling.thinkscape.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.ResourceManager;

public class ObservablesView extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ObservablesView"; //$NON-NLS-1$

	public ObservablesView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolBar.setBounds(0, 0, 89, 23);
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setToolTipText("Add a new observable concept");
		toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/lightbulb_add.png"));
		
		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/lightning_add.png"));
		toolItem_1.setToolTipText("Add a new concept space");
		
		TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);

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
