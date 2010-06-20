package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewModel extends Wizard implements INewWizard {

	NewModelPage page;
	private String project;
	private String namespace;
	
	public NewModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean performFinish() {
		
		boolean ok = true;
		
		/*
		 * TODO check namespace
		 */
		this.project =    page.getProject().getText();
		this.namespace =  page.getNamespace().getText();
		
		return ok;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	public String getProject() {
		return this.project;
	}
	
	public String getNamespace() {
		return this.namespace;
	}

	@Override
	public void addPages() {
		addPage(this.page = new NewModelPage());
	}
	
}
