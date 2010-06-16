package org.integratedmodelling.thinkscape.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CLabel;
import com.swtdesigner.ResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class ConceptEditorPart extends EditorPart {

	public static final String ID = "org.integratedmodelling.thinkscape.editors.concept"; //$NON-NLS-1$
	private IFile file;

	public ConceptEditorPart() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
		tabFolder.setTabPosition(SWT.BOTTOM);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmSemantics = new CTabItem(tabFolder, SWT.NONE);
		tbtmSemantics.setText("Semantics");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmSemantics.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		CLabel lblYouAreAdding = new CLabel(composite_1, SWT.NONE);
		lblYouAreAdding.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblYouAreAdding.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/lightbulb_add.png"));
		lblYouAreAdding.setText("You are adding a new concept that will be observed by a model and/or an annotated source. \r\nThis editor allows you to specify  how Thinklab should interpret and visualize observations\r\n of this concept in this project.");
		
		CTabItem tbtmAppearance = new CTabItem(tabFolder, SWT.NONE);
		tbtmAppearance.setText("Appearance");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmAppearance.setControl(composite_2);
		
		CTabItem tbtmCoverage = new CTabItem(tabFolder, SWT.NONE);
		tbtmCoverage.setText("Coverage");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmCoverage.setControl(composite_3);

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		this.file = ((FileEditorInput)input).getFile();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
