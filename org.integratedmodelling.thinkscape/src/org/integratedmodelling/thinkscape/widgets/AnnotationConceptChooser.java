package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class AnnotationConceptChooser extends Group {
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AnnotationConceptChooser(Composite parent, int style) {
		super(parent, style);
		setText("Observation Semantics");
		setLayout(new GridLayout(4, false));
		
		Label lblObservationType = new Label(this, SWT.NONE);
		lblObservationType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObservationType.setText("Observation Type: ");
		
		Combo combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblObservableType = new Label(this, SWT.NONE);
		lblObservableType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObservableType.setText("Observable Type: ");
		
		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
