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

public class NewSourcePage extends WizardPage {

	private Text text;
	private CCombo combo;

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
		
		CLabel lblSourceType = new CLabel(container, SWT.NONE);
		lblSourceType.setText("Source type:");
		
		combo = new CCombo(container, SWT.BORDER);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		combo.setEditable(false);
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	}

	public String getType() {
		return text.getText();
	}

	public String getSrc() {
		return combo.getText();
	}

}
