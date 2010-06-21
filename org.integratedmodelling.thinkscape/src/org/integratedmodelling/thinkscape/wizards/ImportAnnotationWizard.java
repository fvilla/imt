package org.integratedmodelling.thinkscape.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

public class ImportAnnotationWizard extends Wizard {

	class AnnotationImportPage extends ProjectImportPage {

		AnnotationImportPage() {
			this.define("*.xml", "XML annotation definition files (*.xml)",
					"Parse and import XML annotation files in the specified namespace", 
					"Import an XML annotation file", "Annotation");
		}
		
		/* (non-Javadoc)
		 * @see org.integratedmodelling.thinkscape.wizards.ProjectImportPage#getNamespaces(java.lang.String)
		 */
		@Override
		protected List<String> getNamespaces(String project) {
			ThinklabProject proj = ThinkScape.getProject(project, false);
			ArrayList<String> ret = new ArrayList<String>();
			for (SemanticAnnotationContainer c : proj.getAnnotationNamespaces()) {
				ret.add(c.getNamespace());
			}
			return ret;
		}
		
	}

	private AnnotationImportPage page;

	public ImportAnnotationWizard() {
		setWindowTitle("Import model files");
	}

	@Override
	public void addPages() {		
		addPage(page = new AnnotationImportPage());
	}

	@Override
	public boolean performFinish() {

		Job job = new WorkspaceJob("Importing model file") {
			public IStatus runInWorkspace(IProgressMonitor monitor)
					throws CoreException {
				ThinklabProject project = ThinkScape.getProject(page.getProject(), true);
				project.importAnnotations(page.getNamespace(), new File(page.getFilename()));
				return Status.OK_STATUS;
			}
		};

		job.schedule();
		return false;
	}

}
