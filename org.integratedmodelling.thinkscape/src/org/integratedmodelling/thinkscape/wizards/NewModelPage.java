package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

public class NewModelPage extends WizardPage {
	private Combo namespace;
	private Combo project;

	/**
	 * Create the wizard.
	 */
	public NewModelPage() {
		super("wizardPage");
		setTitle("Create a new model namespace");
		setDescription("Define a new model namespace for a project. You can then create models graphically or by editing Clojure code.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblChooseProject = new Label(container, SWT.NONE);
		lblChooseProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblChooseProject.setText("Choose project: ");
		
		project = new Combo(container, SWT.READ_ONLY);
		project.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		boolean ex = false;
		try {
			for (ThinklabProject p : ThinkScape.scanProjects()) {
				project.add(p.getName());
				ex = true;
			}
		} catch (ThinklabException e) {
			throw new ThinklabRuntimeException(e);
		}
		if (ex)
			project.select(0);

		Label lblAnnotationNamespace = new Label(container, SWT.NONE);
		lblAnnotationNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnnotationNamespace.setText("Model namespace: ");
		
		namespace = new Combo(container, SWT.BORDER);
		namespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if (!ex) {
			project.setEnabled(false);
			namespace.setEnabled(false);
			this.setErrorMessage("No projects have been created. Create a project first.");
		}
	}
	
	public Combo getProject() {
		return project;
	}
	
	public Combo getNamespace() {
		return namespace;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#setPageComplete(boolean)
	 */
	@Override
	public void setPageComplete(boolean complete) {
		super.setPageComplete(complete);
	}

}
