package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CLabel;

public class AnnotationWizardPage extends WizardPage {
	
	private Text text;

	/**
	 * Create the wizard.
	 */
	public AnnotationWizardPage() {
		super("wizardPage");
		setTitle("New Semantic Annotation");
		setDescription("Create a semantic annotation file as a set of observation definitions, for import into a KBox");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		CLabel lblAnnotationName = new CLabel(container, SWT.NONE);
		lblAnnotationName.setBounds(0, 0, 61, 21);
		lblAnnotationName.setText("Annotation name:");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setBounds(0, 0, 76, 21);
	}
}
