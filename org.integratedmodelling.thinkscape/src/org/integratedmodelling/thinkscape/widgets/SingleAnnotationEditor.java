package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class SingleAnnotationEditor extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SingleAnnotationEditor(Composite parent, int style, SemanticAnnotationContainer container, SemanticSource source) {

		super(parent, style);
		setLayout(new GridLayout(1, false));
		AnnotationConceptChooser cchoose = new AnnotationConceptChooser(this, SWT.NONE);
		cchoose.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}


	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
