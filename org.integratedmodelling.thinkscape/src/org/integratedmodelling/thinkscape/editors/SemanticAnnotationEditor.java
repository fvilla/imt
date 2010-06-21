package org.integratedmodelling.thinkscape.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
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
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.widgets.SingleAnnotationEditor;
import org.integratedmodelling.utils.MiscUtilities;
import org.integratedmodelling.utils.Pair;
import org.integratedmodelling.utils.Path;
import org.integratedmodelling.utils.Triple;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class SemanticAnnotationEditor extends EditorPart {

	public static final String ID = "org.integratedmodelling.thinkscape.editors.SemanticAnnotationEditor"; //$NON-NLS-1$
	private Table table;
	private ArrayList<Triple<SemanticAnnotationContainer, SemanticSource,SemanticAnnotation>> sources = 
		new ArrayList<Triple<SemanticAnnotationContainer,SemanticSource,SemanticAnnotation>>();

	private Composite itemEditorParent;
	private TableViewer tableViewer;

	private int currentAnnotation = -1;
	private SingleAnnotationEditor currentEditor;
	private Composite parent;
	private boolean dirty = false;
	private IEditorInput input;
	
	private SemanticAnnotationContainer theContainer;
	private String namespace;
	
	public SemanticAnnotationEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.parent = parent;
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (dirty && (e.keyCode == 's' || e.keyCode == 'S') && (e.stateMask & SWT.CTRL) != 0)
						doSave(new NullProgressMonitor());
			}
		});
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
		new Label(composite, SWT.NONE);
		
		itemEditorParent = new Composite(composite_1, SWT.NONE);
		itemEditorParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_itemEditorParent = new GridLayout(1, false);
		gl_itemEditorParent.marginWidth = 0;
		gl_itemEditorParent.marginTop = 3;
		gl_itemEditorParent.marginHeight = 0;
		itemEditorParent.setLayout(gl_itemEditorParent);
		
		if (currentAnnotation < 0) {
			Label lblDragASource = new Label(itemEditorParent, SWT.SHADOW_NONE);
			lblDragASource.setAlignment(SWT.CENTER);
			lblDragASource.setText("Drag a source from the Sources view or \r\nchoose an observation to edit from the list");
			lblDragASource.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		} else {
			this.currentEditor = new SingleAnnotationEditor(
					itemEditorParent,
					this,
					sources.get(currentAnnotation).getFirst(),
					sources.get(currentAnnotation).getSecond(),
					sources.get(currentAnnotation).getThird());
			this.currentEditor.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		}
		
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

		/*
		 * TODO initialize table with annotations, select current if any
		 */
	}
	
	protected void handleDrop(String string) {
		
		if (string.startsWith("_SOURCE")) {
			String[] ss = string.split("\\|");
			// TODO
		
			SemanticAnnotationContainer container = 
				ThinkScape.getActiveProject().getSemanticSource(ss[1]);
			SemanticSource source = container.getSource(ss[2]);
			
			/*
			 * make new annotation from source and select it.
			 */
			SemanticAnnotation annotation = container.startAnnotation(source, source.getId() + "_1");
			sources.add(new Triple<SemanticAnnotationContainer, SemanticSource, SemanticAnnotation>(container, source, annotation));
			currentAnnotation = sources.size() - 1;
			
			resetView();
		}
	}
	
	@Override
	public void setFocus() {
		// Set the focus
	}

	public void resetView() {
		for (Control c : this.parent.getChildren())
			c.dispose();
		createPartControl(this.parent);
		this.parent.layout(true);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {

		System.out.println("DOSAVE CALLED " + input);
		/*
		 * TODO flush the annotation container
		 */
		resetView();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
//		file.setContents(
//			new ByteArrayInputStream(out.toByteArray()), 
//			true,  // keep saving, even if IFile is out of sync with the Workspace
//			false, // dont keep history
//			monitor); // progress monitor
		
		
		this.dirty = false;
		setPartName("Annotation Namespace: " + (MiscUtilities.getFileBaseName(this.input.getName())));
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(this.input = input);
		this.namespace = MiscUtilities.getFileBaseName(input.getName());
		this.theContainer = 
				ThinkScape.getActiveProject().getAnnotationNamespace(this.namespace);
		setPartName("Annotation Namespace: " + this.namespace);
	}
	
	
	@Override
	public boolean isDirty() {
		return this.dirty;
	}
	
	public void setDirty() {
		this.dirty = true;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
