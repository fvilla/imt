package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationFactory;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationProvider;

public class NewSourcePage extends WizardPage {

	private Text text;
	private CCombo combo;
	private CLabel lblSourceType;
	private Label label;

	/**
	 * Create the wizard.
	 */
	public NewSourcePage() {
		super("wizardPage");
		setTitle("New Source");
		setDescription("Define a source of observations for semantic annotation");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		CLabel lblSourceUrlOr = new CLabel(container, SWT.NONE);
		lblSourceUrlOr.setText("Source URL or filesystem reference:");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.lblSourceType = new CLabel(container, SWT.NONE);
		lblSourceType.setText("Source type:");
		
		combo = new CCombo(container, SWT.BORDER);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * set label to full description, current to first part of string
				 */
				String s = combo.getText();
				if (s != null && s.trim().length() > 0) {
					s = s.split("\\ ")[0];
					SemanticAnnotationProvider pr = 
						SemanticAnnotationFactory.get().getAnnotator(s);
					String ds = pr.getDescription();
					label.setText(ds);
				}
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		combo.setEditable(false);
		
		for (String s : SemanticAnnotationFactory.get().getAnnotatorIds()) {
			
			SemanticAnnotationProvider pr = SemanticAnnotationFactory.get().getAnnotator(s);
			combo.add(s + " - " + pr.getLabel());
			
		}
		
		this.label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	}

	public String getType() {
		return combo.getText();
	}

	public String getSrc() {
		return text.getText();
	}

	public CCombo getTypeSelector() {
		return combo;
	}
}
