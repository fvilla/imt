package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinkscape.editors.SemanticAnnotationEditor;

import com.swtdesigner.ResourceManager;

public class SingleAnnotationEditor extends Composite {
	private Text text;

	private String[] obsTypes = {
		"Ranking",	
		"Percentage",	
		"Count",
		"Measurement",	
		"Classification",	
		"Numeric classification",
		"Binary classification"	
	};
	
	// ACHTUNG this must match the above
	private String[] obsConcepts = {
			"measurement:Ranking",	
			"measurement:Ranking",	
			"measurement:Count",
			"measurement:Measurement",	
			"observation:Classification",	
			"measurement:NumericClassification",
			"measurement:BinaryRanking"	
		};

	private SemanticAnnotationEditor editor;
	private SemanticAnnotation annotation;
	private SemanticAnnotationContainer container;
	private Composite parent;

	private ToolItem statusImage;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SingleAnnotationEditor(Composite parent, SemanticAnnotationEditor edit, SemanticAnnotationContainer contain, SemanticAnnotation annotat) {

		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		
		this.parent = parent;
		this.editor = edit;
		this.container = contain;
		this.annotation = annotat;
		
		Composite toolbar = new Composite(this, SWT.BORDER);
		GridLayout gl_toolbar = new GridLayout(2, false);
		gl_toolbar.verticalSpacing = 0;
		gl_toolbar.horizontalSpacing = 0;
		gl_toolbar.marginHeight = 0;
		gl_toolbar.marginWidth = 0;
		toolbar.setLayout(gl_toolbar);
		GridData gd_toolbar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_toolbar.heightHint = 23;
		toolbar.setLayoutData(gd_toolbar);

		ToolBar tbar = new ToolBar(toolbar, SWT.NONE);
		tbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		ToolItem toolItem = new ToolItem(tbar, SWT.NONE);
		toolItem.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/save.png"));
		toolItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				/*
				 * flush annotation to container
				 */
				annotation.setValid(annotation.validate() == 0);
				container.putAnnotation(annotation);
				editor.doSave(new NullProgressMonitor());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		ToolBar statusToolbar = new ToolBar(toolbar, SWT.FLAT | SWT.RIGHT);
		statusToolbar.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		
		int nProblems = annotation.validate();
		String tooltip = "The observation is valid";
		if (nProblems > 0) {
			tooltip = "";
			for (String p : annotation.getValidationMessages()) {
				tooltip += p + "\n";
			}
		}
		
		this.statusImage = new ToolItem(statusToolbar, SWT.NONE);
		statusImage.setImage(ResourceManager.getPluginImage(
				"org.integratedmodelling.thinkscape", 
					nProblems == 0 ? "icons/check.png" : "icons/error.png"));
		statusImage.setToolTipText(tooltip);
		statusImage.setSelection(true);
		new Label(toolbar, SWT.NONE);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name: ");
		
		text = new Text(composite, SWT.BORDER);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				editor.setDirty();
				refresh();
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText(annotation.getId());
		
		Label lblType = new Label(composite, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type: ");
		
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = 0; int idx = 0;
				for (String s : obsTypes) {
					if (s.equals(e.text)) {
						idx = i;
						break;
					}
					i++;
				}
				annotation.setType(obsConcepts[idx]);
				editor.setDirty();
				refresh();
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(obsTypes);
		combo.select(0);
		
		PShelf shelf = new PShelf(this, SWT.NONE);
		shelf.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		PShelfItem shelfItem = new PShelfItem(shelf, SWT.NONE);
		shelfItem.setText("Semantics");
		shelfItem.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ConceptChooser chooser = new ConceptChooser(shelfItem.getBody(), this.editor, this.annotation);
		
		PShelfItem shelfItem_1 = new PShelfItem(shelf, SWT.NONE);
		shelfItem_1.setText("Units and numeric range");
		
		PShelfItem shelfItem_2 = new PShelfItem(shelf, SWT.NONE);
		shelfItem_2.setText("Transformations");
		
		PShelfItem shelfItem_3 = new PShelfItem(shelf, SWT.NONE);
		shelfItem_3.setText("Spatial Context");
		
		PShelfItem shelfItem_4 = new PShelfItem(shelf, SWT.NONE);
		shelfItem_4.setText("Temporal Context");
	}

	public void refresh() {

		int nProblems = annotation.validate();
		String tooltip = "The observation is valid";
		if (nProblems > 0) {
			tooltip = "";
			for (String p : annotation.getValidationMessages()) {
				tooltip += p + "\n";
			}
		}
		
		statusImage.setImage(ResourceManager.getPluginImage(
				"org.integratedmodelling.thinkscape", 
					nProblems == 0 ? "icons/check.png" : "icons/error.png"));
		statusImage.setToolTipText(tooltip);
	}
	
	private void setDirty() {
		/*
		 * validate annotation and reset info
		 */
		
		/*
		 * set editor to dirty
		 */
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
