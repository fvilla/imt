package org.integratedmodelling.thinkscape.views;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.TreeHelper;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class KnowledgeView extends ViewPart {
	public KnowledgeView() {
	}
	

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.integratedmodelling.thinkscape.views.Knowledge";
	private DrillDownAdapter drillDownAdapter;
	private Action doubleClickAction;
	private Text text;
	private TabFolder tabFolder;

	private TreeHelper treeHelper;
	private TreeViewer conceptTree;

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		
		this.tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TabItem tbtmConceptTree = new TabItem(tabFolder, SWT.NONE);
		tbtmConceptTree.setText("Concept Space");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmConceptTree.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeHelper.instrumentOntologyTree(null);
			}
		});
		toolItem.setToolTipText("Group by concept space");
		toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/lightbulb.png"));
		
		this.conceptTree = new TreeViewer(composite, SWT.BORDER);
		Tree ctree = conceptTree.getTree();
		ctree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		ctree.setLinesVisible(true);
		ctree.setHeaderVisible(true);
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(conceptTree, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn.getColumn();
		trclmnName.setWidth(260);
		trclmnName.setText("Name");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(conceptTree, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_1.getColumn();
		trclmnDescription.setWidth(400);
		trclmnDescription.setText("Description");
		
		TabItem tbtmKboxes = new TabItem(tabFolder, SWT.NONE);
		tbtmKboxes.setText("Data Space");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmKboxes.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar_1 = new ToolBar(composite_1, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TreeViewer treeViewer = new TreeViewer(composite_1, SWT.BORDER);
		Tree kboxTree = treeViewer.getTree();
		kboxTree.setHeaderVisible(true);
		kboxTree.setLinesVisible(true);
		kboxTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		/*
		 * instrument the concept tree for initial hierarchical view
		 */
		this.treeHelper = new TreeHelper(conceptTree, this);
		treeHelper.instrumentConceptTree(KnowledgeManager.Thing());
		
		text = new Text(parent, SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ArrayList<IConcept> cc = treeHelper.filter(text.getText() + (char)e.keyCode);
				treeHelper.instrumentOntologyTree(cc);
				conceptTree.expandAll();
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		treeHelper.instrumentConceptTree(KnowledgeManager.Thing());
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
				KnowledgeView.this.fillContextMenu(manager);
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
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = conceptTree.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString() + " (" + obj.getClass() + ")");
			}
		};
	}

	private void hookDoubleClickAction() {
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			conceptTree.getControl().getShell(),
			"Knowledge",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		conceptTree.getControl().setFocus();
	}
}