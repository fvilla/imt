package org.integratedmodelling.thinkscape.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class ProjectImportPage extends WizardPage {
	private Combo namespace;
	private Combo project;
	private Text text;

	private String description = "Wizard page description";
	private String title = "Wizard page description";
	private String fileMask = "*.txt";
	private String fileDesc = "Text files (*.txt)";
	private String objectLabel;
	
	/**
	 * Create the wizard.
	 */
	public ProjectImportPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	public void define(String fileMask, String fileDesc, String description, String title, String objectLabel) {
		this.fileMask = fileMask;
		this.fileDesc = fileDesc;
		setTitle(this.title = title);
		setDescription(this.description = description);
		this.objectLabel = objectLabel;
	}

	/**
	 * Override this one to choose existing namespaces 
	 * 
	 * @param project
	 * @return
	 */
	protected List<String> getNamespaces(String project) {
		ArrayList<String> ret = new ArrayList<String>();
		return ret;
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
		project.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				namespace.removeAll();
				for (String s : getNamespaces(project.getText())) {
					namespace.add(s);
				}
			}
		});
		project.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAnnotationNamespace = new Label(container, SWT.NONE);
		lblAnnotationNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnnotationNamespace.setText(objectLabel + " namespace: ");

		namespace = new Combo(container, SWT.BORDER);
		namespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFileToImport = new Label(container, SWT.NONE);
		lblFileToImport.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileToImport.setAlignment(SWT.RIGHT);
		lblFileToImport.setText("File to import: ");
		
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			      FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
			        dlg.setFilterNames(new String[] {fileDesc});
			        dlg.setFilterExtensions(new String[] {fileMask});
			        String fn = dlg.open();
			        if (fn != null) {
			          text.setText(fn);
			        }
			}
		});
		btnBrowse.setText("Browse...");

		boolean ex = false;
		try {
			for (ThinklabProject p : ThinkScape.scanProjects()) {
				project.add(p.getName());
				ex = true;
			}
		} catch (ThinklabException e) {
			throw new ThinklabRuntimeException(e);
		}
		if (ex) {
			project.select(0);
			for (String s : getNamespaces(project.getText())) {
				namespace.add(s);
			}
		}
		
		if (!ex) {
			project.setEnabled(false);
			namespace.setEnabled(false);
			this.setErrorMessage("No projects have been created. Create a project first.");
		} 

	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return 
			!getNamespace().trim().equals("") &&
			!getFilename().trim().equals("") &&
			new File(getFilename()).exists();
	}

	public String getFilename() {
		return text.getText();
	}
	
	public String getProject() {
		return project.getText();
	}
	
	public String getNamespace() {
		return namespace.getText();
	}
}
