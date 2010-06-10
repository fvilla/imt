package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.Wizard;

public class NewSourceWizard extends Wizard {

	NewSourcePage page = null;
	
	public NewSourceWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		addPage(page = new NewSourcePage());
	}

	@Override
	public boolean performFinish() {

		String src = page.getSrc();
		String typ = page.getType();
		
		if (validate(src, typ))
			return true;
		
		return false;
	}

	private boolean validate(String src, String typ) {
		
		return true;
	}

}
