package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

public class NewSourceWizard extends Wizard implements INewWizard {

	NewSourcePage page = null;
	private IWorkbench workbench;
	
	public NewSourceWizard() {
		setWindowTitle("New Annotation Source");
	}

	@Override
	public void addPages() {
		addPage(page = new NewSourcePage());
	}

	@Override
	public boolean performFinish() {

		final String src = page.getSrc();
		final String typ = page.getType().split("\\ ")[0].trim();
		
		if (validate(src, typ)) {

			final ThinklabProject active = ThinkScape.getActiveProject();
						
			Job job = new WorkspaceJob("Importing: " + src) {
			      public IStatus runInWorkspace(IProgressMonitor monitor) 
			         throws CoreException {
			         	active.importNewSemanticSource(typ, src);
						return Status.OK_STATUS;
			      }
			   };
			   
			job.schedule();				
			return true;
		}
		
		return false;
	}

	private boolean validate(String src, String typ) {
		
		if (src == null || src.trim().equals(""))
			return false;
		
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

}
