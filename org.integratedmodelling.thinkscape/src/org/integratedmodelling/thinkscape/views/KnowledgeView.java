package org.integratedmodelling.thinkscape.views;

import java.util.ArrayList;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.widgets.ToolItem;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.TreeHelper;

import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class KnowledgeView extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.KnowledgeView"; //$NON-NLS-1$
	private Text conceptSearch;
	private Text dataSearch;
	private TreeViewer conceptTree;
	private TreeViewer dataTree;
	private TreeHelper treeHelper;

	public KnowledgeView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBounds(0, 0, 122, 43);
		
		TabItem tbtmConceptSpace = new TabItem(tabFolder, SWT.NONE);
		tbtmConceptSpace.setText("Concept Space");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmConceptSpace.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem treeViewButton = new ToolItem(toolBar, SWT.NONE);
		treeViewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO get the currently selected concept if any, and start the hierarchy from it.
				IConcept start = KnowledgeManager.Thing();
				treeHelper.instrumentConceptTree(start);
			}
		});
		treeViewButton.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/eview16/filenav_nav.gif"));
		
		ToolItem listViewButton = new ToolItem(toolBar, SWT.NONE);
		listViewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO see what to do with the selection if we have any.
				treeHelper.instrumentOntologyTree(null);
			}
		});
		listViewButton.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/eview16/outline_co.gif"));
		
		this.conceptTree = new TreeViewer(composite, SWT.BORDER);
		Tree tree = conceptTree.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(conceptTree, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn.getColumn();
		trclmnName.setWidth(260);
		trclmnName.setText("Name");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(conceptTree, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_1.getColumn();
		trclmnDescription.setWidth(400);
		trclmnDescription.setText("Description");
		
		conceptSearch = new Text(composite, SWT.BORDER);
		conceptSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ArrayList<IConcept> cc = treeHelper.filter(conceptSearch.getText() + (char)e.keyCode);
				treeHelper.instrumentOntologyTree(cc);
				conceptTree.expandAll();
			}
		});
		conceptSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmDataSpace = new TabItem(tabFolder, SWT.NONE);
		tbtmDataSpace.setText("Data Space");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmDataSpace.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar_1 = new ToolBar(composite_1, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.dataTree = new TreeViewer(composite_1, SWT.BORDER);
		Tree tree_1 = dataTree.getTree();
		tree_1.setHeaderVisible(true);
		tree_1.setLinesVisible(true);
		tree_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		dataSearch = new Text(composite_1, SWT.BORDER);
		dataSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initializeContent();
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private void initializeContent() {
		/*
		 * initial status of concept tree is hierarchy of all concepts
		 */
		this.treeHelper = new TreeHelper(conceptTree, this);
		treeHelper.instrumentConceptTree(KnowledgeManager.Thing());
		
		/*
		 * TODO initialize data tree with all kboxes
		 */
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
