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

public class ImportModelWizard extends Wizard {

	class ModelImportPage extends ProjectImportPage {

		ModelImportPage() {
			this.define("*.clj", "Clojure model definition files (*.clj)",
					"Parse and import Clojure model files in the specified namespace", 
					"Import a Clojure model file", "Model");
			this.enableNamespace(false);
		}
		
		/* (non-Javadoc)
		 * @see org.integratedmodelling.thinkscape.wizards.ProjectImportPage#getNamespaces(java.lang.String)
		 */
		@Override
		protected List<String> getNamespaces(String project) {
			ThinklabProject proj = ThinkScape.getProject(project, false);
			ArrayList<String> ret = new ArrayList<String>();
// TODO use model containers, tbd
//			for (SemanticAnnotationContainer c : proj.getAnnotationNamespaces()) {
//				ret.add(c.getNamespace());
//			}
			return ret;

		}
		
	}

	private ModelImportPage page;

	public ImportModelWizard() {
		setWindowTitle("Import model files");
	}

	@Override
	public void addPages() {		
		addPage(page = new ModelImportPage());
	}

	@Override
	public boolean performFinish() {

		final ThinklabProject project = ThinkScape.getProject(page.getProject(), true);
		final File mfile = new File(page.getFilename());
		
		Job job = new WorkspaceJob("Importing model file") {
			public IStatus runInWorkspace(IProgressMonitor monitor)
					throws CoreException {
				project.importModel(mfile);
				return Status.OK_STATUS;
			}
		};

		job.schedule();
		return true;
	}

}
