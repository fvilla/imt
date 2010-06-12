package org.integratedmodelling.thinkscape.wizards;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.editors.AnnotationEditor;

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

		String src = page.getSrc();
		String typ = page.getType();
		
		if (validate(src, typ)) {
			IWorkbenchPage pg = workbench.getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(pg, new URI(src), AnnotationEditor.ID, true);
			} catch (Exception e) {
				throw new ThinklabRuntimeException(e);
			}
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
