package org.integratedmodelling.thinkscape.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

public class NewProjectPage extends WizardPage {
	private Text projectName;
	private Table dependenciesTable;

	/**
	 * Create the wizard.
	 */
	public NewProjectPage() {
		super("wizardPage");
		setTitle("Create a new Thinklab Project");
		setDescription("A Thinklab project contains models, ontologies and semantic annotations of data sources. As an OSGI bundle it can be deployed in any OSGI container. ");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setBounds(0, 0, 64, 64);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblProjectName = new Label(composite, SWT.NONE);
		lblProjectName.setText("Project name: ");
		
		projectName = new Text(composite, SWT.BORDER);
		projectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDependencies = new Label(container, SWT.NONE);
		lblDependencies.setBounds(0, 0, 55, 15);
		lblDependencies.setText("Dependencies:");
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		dependenciesTable = tableViewer.getTable();
		dependenciesTable.setLinesVisible(true);
		dependenciesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		dependenciesTable.setBounds(0, 0, 85, 85);
	}
	public Text getProjectName() {
		return projectName;
	}
}
