package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.swt.widgets.Composite;

public class SingleAnnotationEditor extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SingleAnnotationEditor(Composite parent, int style) {
		super(parent, style);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
