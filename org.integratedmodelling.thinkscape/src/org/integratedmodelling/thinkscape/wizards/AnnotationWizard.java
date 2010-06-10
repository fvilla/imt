package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.Wizard;

public class AnnotationWizard extends Wizard {

	public AnnotationWizard() {
		setWindowTitle("New Semantic Annotation");
	}

	@Override
	public void addPages() {
		this.addPage(new AnnotationWizardPage());
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
