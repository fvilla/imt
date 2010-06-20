package org.integratedmodelling.thinkscape.editors;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.utils.Pair;

public class SemanticAnnotationEditor extends EditorPart {

	public static final String ID = "org.integratedmodelling.thinkscape.editors.SemanticAnnotationEditor"; //$NON-NLS-1$
	private Table table;
	private ArrayList<Pair<SemanticAnnotationContainer, SemanticSource>> sources = 
		new ArrayList<Pair<SemanticAnnotationContainer,SemanticSource>>();

	private Composite parent;
	private TableViewer tableViewer;

	public SemanticAnnotationEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		this.tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData gd_table = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_table.widthHint = 160;
		table.setLayoutData(gd_table);
		table.setBounds(0, 0, 85, 85);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnObservations = tableViewerColumn.getColumn();
		tblclmnObservations.setText("Observations");
		tblclmnObservations.setWidth(400);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ToolBar toolBar = new ToolBar(composite_1, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		DropTarget dropTarget = new DropTarget(composite, DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		
		dropTarget.addDropListener(new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent arg0) {
			}

			@Override
			public void drop(DropTargetEvent arg0) {
				handleDrop((String) (arg0.data));
			}

			@Override
			public void dragOver(DropTargetEvent arg0) {
			}

			@Override
			public void dragOperationChanged(DropTargetEvent arg0) {
			}

			@Override
			public void dragLeave(DropTargetEvent arg0) {
			}

			@Override
			public void dragEnter(DropTargetEvent arg0) {
				arg0.detail = DND.DROP_COPY;
			}
		});

		for (Pair<SemanticAnnotationContainer, SemanticSource> sp : sources) {
			tableViewer.add(sp.getSecond().id);
		}


	}
	
	protected void handleDrop(String string) {
		
		if (string.startsWith("_SOURCE")) {
			String[] ss = string.split("\\|");
			// TODO
		
			SemanticAnnotationContainer container = 
				ThinkScape.getActiveProject().getSemanticSource(ss[1]);
			SemanticSource source = container.getSource(ss[2]);
			
			sources.add(new Pair<SemanticAnnotationContainer, SemanticSource>(container, source));
			tableViewer.add(source.id);
				
		}
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
