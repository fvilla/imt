package org.integratedmodelling.thinkscape.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
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
import org.integratedmodelling.thinkscape.wizards.NewSourceWizard;

public class Sources extends ViewPart {

	public static final String ID = "org.integratedmodelling.thinkscape.views.Sources"; //$NON-NLS-1$

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
				tltmNewSource.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
				        NewSourceWizard wizard = new NewSourceWizard();
				        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
				        dialog.setBlockOnOpen(true);
				        int returnCode = dialog.open();
				        if(returnCode == Dialog.OK)
				          System.out.println("OK");
				        else
				          System.out.println("Cancelled");
					}
				});
				tltmNewSource.setImage(SWTResourceManager.getImage(Sources.class, "/icons/full/obj16/add_obj.gif"));
				tltmNewSource.setText("New Source");
			}
		}
		{
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			composite.setLayout(new TreeColumnLayout());
			{
				TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
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

}
