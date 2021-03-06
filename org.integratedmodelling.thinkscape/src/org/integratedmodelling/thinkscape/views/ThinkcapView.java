package org.integratedmodelling.thinkscape.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.ResourceManager;

public class ThinkcapView extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.ThinkcapView"; //$NON-NLS-1$
	private Table table;

	public ThinkcapView() {
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
		toolBar.setBounds(0, 0, 89, 23);
		
		ToolItem tltmStart = new ToolItem(toolBar, SWT.NONE);
		tltmStart.setText("Start");
		tltmStart.setToolTipText("Start server");
		tltmStart.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/play.png"));
		
		ToolItem tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setEnabled(false);
		tltmStop.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/stop.png"));
		tltmStop.setText("Stop ");
		
		ToolItem tltmRestart = new ToolItem(toolBar, SWT.NONE);
		tltmRestart.setEnabled(false);
		tltmRestart.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/refresh.png"));
		tltmRestart.setText("Restart ");
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBounds(0, 0, 85, 85);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setResizable(false);
		tableColumn.setWidth(36);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTime = tableViewerColumn_1.getColumn();
		tblclmnTime.setWidth(240);
		tblclmnTime.setText("Application");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnEvent = tableViewerColumn_2.getColumn();
		tblclmnEvent.setWidth(680);
		tblclmnEvent.setText("Description");

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
