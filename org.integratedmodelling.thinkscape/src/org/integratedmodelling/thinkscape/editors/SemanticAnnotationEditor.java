package org.integratedmodelling.thinkscape.editors;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.widgets.SingleAnnotationEditor;
import org.integratedmodelling.utils.Pair;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ToolBar;
import com.swtdesigner.SWTResourceManager;

public class SemanticAnnotationEditor extends EditorPart {

	public static final String ID = "org.integratedmodelling.thinkscape.editors.SemanticAnnotationEditor"; //$NON-NLS-1$

	private ArrayList<Pair<SemanticAnnotationContainer, SemanticSource>> sources = 
		new ArrayList<Pair<SemanticAnnotationContainer,SemanticSource>>();

	private Composite parent;
	
	
	public SemanticAnnotationEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		this.parent = parent;
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		PShelf shelf = new PShelf(container, SWT.NONE);
		shelf.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shelf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		DropTarget dropTarget = new DropTarget(shelf, DND.DROP_MOVE	| DND.DROP_COPY | DND.DROP_LINK);
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
			
			PShelfItem shelfItem = new PShelfItem(shelf, SWT.NONE);
			shelfItem.setText(sp.getSecond().id);
			shelfItem.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
			SingleAnnotationEditor ed = new SingleAnnotationEditor(
					shelfItem.getBody(), SWT.NONE, sp.getFirst(), sp.getSecond());
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
	
	protected void handleDrop(String string) {
		
		if (string.startsWith("_SOURCE")) {
			String[] ss = string.split("\\|");
			// TODO
		
			SemanticAnnotationContainer container = 
				ThinkScape.getActiveProject().getSemanticSource(ss[1]);
			SemanticSource source = container.getSource(ss[2]);
			
			sources.add(new Pair<SemanticAnnotationContainer, SemanticSource>(container, source));
				
			for (Control c : this.parent.getChildren()) {
				c.dispose();
			}
			
			createPartControl(this.parent);
			this.parent.layout(true);
		}
	}


}
