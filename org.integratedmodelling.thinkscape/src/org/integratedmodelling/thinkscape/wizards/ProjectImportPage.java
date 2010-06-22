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
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class ProjectImportPage extends WizardPage {
	
	private Combo namespaceField;
	private Combo projectField;
	private Text textField;

	private String description = "Wizard page description";
	private String title = "Wizard page description";
	private String fileMask = "*.txt";
	private String fileDesc = "Text files (*.txt)";
	private String objectLabel;
	private String text;
	private String project;
	private String namespace;
	private boolean namespaceEnabled = true; 
	
	/**
	 */
	public ProjectImportPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	protected void define(String fileMask, String fileDesc, String description, String title, String objectLabel) {
		this.fileMask = fileMask;
		this.fileDesc = fileDesc;
		setTitle(this.title = title);
		setDescription(this.description = description);
		this.objectLabel = objectLabel;
	}

	protected void enableNamespace(boolean b) {
		this.namespaceEnabled = b;
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
		
		projectField = new Combo(container, SWT.READ_ONLY);
		projectField.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				project = projectField.getText();
				if (namespaceEnabled) {
					namespaceField.removeAll();
					for (String s : getNamespaces(project)) {
						namespaceField.add(s);
					}
				}
			}
		});
		projectField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if (namespaceEnabled) {

			Label lblAnnotationNamespace = new Label(container, SWT.NONE);
			lblAnnotationNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblAnnotationNamespace.setText(objectLabel + " namespace: ");

			namespaceField = new Combo(container, SWT.BORDER);
			namespaceField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent arg0) {
					namespace = namespaceField.getText();
				}
			});
			namespaceField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		
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
		
		textField = new Text(composite, SWT.BORDER);
		textField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			      FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
			        dlg.setFilterNames(new String[] {fileDesc});
			        dlg.setFilterExtensions(new String[] {fileMask});
			        String fn = dlg.open();
			        if (fn != null) {
			          textField.setText(fn);
			        }
			        text = fn == null ? "" : fn;
			}
		});
		btnBrowse.setText("Browse...");

		boolean ex = false;
		for (ThinklabProject p : ThinkScape.getProjects()) {
			projectField.add(p.getName());
			ex = true;
		}

		if (ex) {
			projectField.select(0);
			project = projectField.getText();
			if (namespaceEnabled) {
				for (String s : getNamespaces(projectField.getText())) {
					namespaceField.add(s);
				}
			}
		}
		
		if (!ex) {
			projectField.setEnabled(false);
			if (namespaceEnabled)
				namespaceField.setEnabled(false);
			this.setErrorMessage("No projects have been created. Create a project first.");
		} 

	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return true;
//			!getNamespace().trim().equals("") &&
//			!getFilename().trim().equals("") &&
//			new File(getFilename()).exists();
	}

	public String getFilename() {
		return text;
	}
	
	public String getProject() {
		return project;
	}
	
	public String getNamespace() {
		return namespace;
	}
}
