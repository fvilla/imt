package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.integratedmodelling.corescience.annotations.ObservationAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinkscape.editors.SemanticAnnotationEditor;
import org.eclipse.swt.widgets.ToolItem;
import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class ConceptChooser extends Composite {
	private Text text;
	private SemanticAnnotationEditor editor;
	private SemanticAnnotation annotation;
	private SingleAnnotationEditor singleeditor;

	/**
	 * Create the composite.
	 * @param parent
	 * @param seditor 
	 * @param edit
	 */
	public ConceptChooser(Composite parent, SingleAnnotationEditor seditor, SemanticAnnotationEditor edit, SemanticAnnotation annot) {

		super(parent, SWT.BORDER);
		setLayout(new GridLayout(1, false));
		
		this.editor = edit;
		this.singleeditor = seditor;
		this.annotation = annot;
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblConcept = new Label(composite, SWT.NONE);
		lblConcept.setToolTipText("Drag a concept from the Knowledge view");
		lblConcept.setBounds(0, 0, 55, 15);
		lblConcept.setText("Observed Concept: ");
		
		text = new Text(composite, SWT.BORDER);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				annotation.setProperty(ObservationAnnotation.OBSERVABLE_PROPERTY, text.getText());
				editor.setDirty();
				singleeditor.refresh();
			}
		});
		text.setToolTipText("Drag a concept from the Knowledge view");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setBounds(0, 0, 76, 21);
		
		DropTarget dropTarget = new DropTarget(text, DND.DROP_COPY);
		dropTarget.setTransfer( new Transfer[] {TextTransfer.getInstance()});
		dropTarget.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent arg0) {}
			@Override
			public void drop(DropTargetEvent arg0) {
				handleDrop((String)(arg0.data));
			}
			@Override
			public void dragOver(DropTargetEvent arg0) {}
			@Override
			public void dragOperationChanged(DropTargetEvent arg0) {}
			@Override
			public void dragLeave(DropTargetEvent arg0) {}
			@Override
			public void dragEnter(DropTargetEvent arg0) {
				arg0.detail = DND.DROP_COPY;
			}
		});
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));
		
		Button btnSpecialize = new Button(composite_2, SWT.NONE);
		btnSpecialize.setBounds(0, 0, 75, 25);
		btnSpecialize.setText("Specialize");
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, true));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_1.setBounds(0, 0, 64, 64);
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_3.setBounds(0, 0, 64, 64);
		composite_3.setLayout(new GridLayout(1, false));
		
		Composite composite_5 = new Composite(composite_3, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_5.setBounds(0, 0, 64, 64);
		composite_5.setLayout(new GridLayout(2, false));
		
		Label lblSubclasses = new Label(composite_5, SWT.NONE);
		lblSubclasses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblSubclasses.setText("Direct Subclasses");
		
		ToolBar toolBar = new ToolBar(composite_5, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/add.png"));
		
		List list = new List(composite_3, SWT.BORDER);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		list.setBounds(0, 0, 71, 68);
		
		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_4.setBounds(0, 0, 64, 64);
		composite_4.setLayout(new GridLayout(1, false));
		
		Composite composite_6 = new Composite(composite_4, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_6.setBounds(0, 0, 64, 64);
		composite_6.setLayout(new GridLayout(2, false));
		
		Label lblPhysicalProperties = new Label(composite_6, SWT.NONE);
		lblPhysicalProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblPhysicalProperties.setText("Physical Properties");
		
		ToolBar toolBar_1 = new ToolBar(composite_6, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Tree tree = new Tree(composite_4, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected void handleDrop(String string) {
		
		if (string.startsWith("_CONCEPT")) {
			String[] ss = string.split("\\|");
			text.setText(ss[1]);
		}
	}
}
