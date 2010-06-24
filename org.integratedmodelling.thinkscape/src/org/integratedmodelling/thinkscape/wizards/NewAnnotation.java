package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

public class NewAnnotation extends Wizard implements INewWizard {

	NewAnnotationPage page;
	private String project;
	private String namespace;
	private SemanticAnnotationContainer container;
	
	public NewAnnotation() {
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
		
		ThinklabProject proj = ThinkScape.getProject(this.project, true);
		if (proj.getAnnotationNamespace(this.namespace, false) != null) {
			
			/*
			 * todo error message and stuff - this should be in the page validation
			 */
			return false;
		}
		
		this.container = proj.getAnnotationNamespace(this.namespace, true);
		
		return true;
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
		addPage(this.page = new NewAnnotationPage());
	}
	
}
