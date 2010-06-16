package org.integratedmodelling.thinkscape.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class AnnotationEditor extends EditorPart {

	public static final String ID = "org.integratedmodelling.thinkscape.editors.AnnotationEditor"; //$NON-NLS-1$

	public AnnotationEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeColumnLayout tcl_composite = new TreeColumnLayout();
		composite.setLayout(tcl_composite);
		
		TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnObservation = treeViewerColumn.getColumn();
		tcl_composite.setColumnData(trclmnObservation, new ColumnPixelData(150, true, true));
		trclmnObservation.setText("Observation");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_1 = treeViewerColumn_1.getColumn();
		tcl_composite.setColumnData(treeColumn_1, new ColumnPixelData(300, true, true));
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_2 = treeViewerColumn_2.getColumn();
		tcl_composite.setColumnData(treeColumn_2, new ColumnPixelData(150, true, true));
		treeColumn_2.setText("New Column");

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
