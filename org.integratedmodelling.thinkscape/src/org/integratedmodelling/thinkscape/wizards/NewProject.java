/**
 * 
 */
package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.integratedmodelling.thinkscape.ThinkScape;

/**
 * @author Ferdinando
 *
 */
public class NewProject extends Wizard implements INewWizard {

	private NewProjectPage page;

	/**
	 * 
	 */
	public NewProject() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		final String src = page.getProjectName().getText();
		
		if (validate(src)) {
			
			Job job = new WorkspaceJob("Creating project: " + src) {
			      public IStatus runInWorkspace(IProgressMonitor monitor) 
			         throws CoreException {
						ThinkScape.addProject(src);
						return Status.OK_STATUS;
			      }
			   };
			   
			job.schedule();

			return true;
		}
		
		return false;
	}
	private boolean validate(String src) {
		/*
		 * checks should be done internally to the page
		 */
		return true;
	}

	@Override
	public void addPages() {
		addPage(this.page = new NewProjectPage());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

}
