package org.integratedmodelling.thinkscape.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.ThinkscapeEvent;
import org.integratedmodelling.thinkscape.TreeModel;
import org.integratedmodelling.thinkscape.TreeHelper.TreeObject;
import org.integratedmodelling.thinkscape.annotation.ThinkscapeSemanticAnnotationContainer;
import org.integratedmodelling.thinkscape.project.ThinklabProject;
import org.integratedmodelling.thinkscape.wizards.ImportAnnotationWizard;
import org.integratedmodelling.thinkscape.wizards.NewAnnotation;

import com.swtdesigner.ResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class AnnotationsView extends ViewPart implements IPropertyChangeListener {

	public static final String ID = "org.integratedmodelling.thinkscape.views.AnnotationsView"; //$NON-NLS-1$
	private Text text;
	private Composite  ps;
	private AnnotationModel treeModel;
	private TreeViewer treeViewer;
	
	class AnnotationModel extends TreeModel {

		public AnnotationModel(TreeViewer viewer, ViewPart view) {
			super(viewer, view);
		}

		@Override
		public Object[] getChildren(Object object) {
			
			Object[] ret = null;
			if (object instanceof SemanticAnnotationContainer) {
				ArrayList<String> ids = new ArrayList<String>();
				for (String id :((SemanticAnnotationContainer)object).getAnnotationIds())
					ids.add(id);
				ret = new SemanticAnnotation[ids.size()];
				Collections.sort(ids);
				int i = 0;
				for (String s : ids) {
					ret[i++] = ((SemanticAnnotationContainer)object).getAnnotation(s);
				}
			}
			return ret;
		}

		@Override
		public Image getImage(Object object, int column) {
			String ret = null;
			if (object instanceof SemanticAnnotationContainer && column == 1) {
					ret = "icons/database_edit.png";
			} else if (object instanceof SemanticAnnotation && column == 0) {
				if (((SemanticAnnotation)object).isValid()) {
					ret = "icons/check.png";
				} else {
					ret = "icons/error.png";
				}
			} else if (object instanceof SemanticAnnotation && column == 3) {
				/*
				 * TODO context image; concept images if necessary
				 */
			}
			return ret == null ? 
					null :
					ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", ret);
		}

		@Override
		public String getName(Object object, int column) {
			
			String ret = "";
			if (object instanceof SemanticAnnotationContainer && column == 1) {
				ret = ((SemanticAnnotationContainer)object).getNamespace();
			} else if (object instanceof SemanticAnnotation) {
				switch (column) {
					/*
					 * TODO add other columns
					 */
					case 1: ret = ((SemanticAnnotation)object).getId(); break;
				}
			}
			return ret;
		}

		/* (non-Javadoc)
		 * @see org.integratedmodelling.thinkscape.TreeModel#handleDoubleClick(java.lang.Object, org.eclipse.ui.IWorkbenchPage)
		 */
		@Override
		public void handleDoubleClick(Object obj, IWorkbenchPage page) {
			TreeObject o = (TreeObject) obj;
			if (o.data instanceof SemanticAnnotationContainer) {
				
				SemanticAnnotationContainer container = (SemanticAnnotationContainer) o.data;
				
				/*
				 * fire up concept annotation editor; create concept axiom file if not there
				 */
				ThinkscapeSemanticAnnotationContainer ann = 
					(ThinkscapeSemanticAnnotationContainer) 
						ThinkScape.getActiveProject().getAnnotationNamespace(container.getNamespace(), false);
				
				IEditorDescriptor desc = 
					PlatformUI.getWorkbench().
					getEditorRegistry().getDefaultEditor(ann.getFile().getName());
				try {
					page.openEditor(new FileEditorInput(ann.getFile()), desc.getId());
				} catch (PartInitException e) {
					throw new ThinklabRuntimeException(e);
				}
				
			} else if (o.data instanceof SemanticAnnotation) {
				/*
				 * TODO use marker and open specified annotation
				 */
			}
		}
		
		
	}
	
	public AnnotationsView() {
		ThinkScape.getDefault().addPropertyChangeListener(this);
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		ps = parent;
		parent.setLayout(new GridLayout(1, false));
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		container.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolBar.setBounds(0, 0, 89, 23);
		
		ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
		tltmNew.setText("New");
		tltmNew.setToolTipText("Create a new annotation namespace");
		tltmNew.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_add.png"));
		tltmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IWorkbenchPage pg = getSite().getPage();
		        NewAnnotation wizard = new NewAnnotation();
				wizard.init(getSite().getWorkbenchWindow().getWorkbench(),
				            (IStructuredSelection)null);
		        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
		        dialog.setBlockOnOpen(true);
		        int returnCode = dialog.open();
		        
		        if (returnCode == WizardDialog.OK) {

		        	ThinklabProject project = ThinkScape.getProject(wizard.getProject(), true);
		        	
		        	ThinkscapeSemanticAnnotationContainer ann = 
		        		(ThinkscapeSemanticAnnotationContainer) 
		        			project.getAnnotationNamespace(wizard.getNamespace(), true);
		        	IEditorDescriptor desc = 
		        		PlatformUI.getWorkbench().
		        			getEditorRegistry().getDefaultEditor(ann.getFile().getName());
		        	try {
		        		pg.openEditor(new FileEditorInput(ann.getFile()), desc.getId());
		        	} catch (PartInitException ee) {
		        		throw new ThinklabRuntimeException(ee);
		        	}
		        }
			}
		});
		tltmNew.setHotImage(ResourceManager.getPluginImage("org.eclipse.ui", "icons/add.png"));
		
		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage pg = getSite().getPage();
		        ImportAnnotationWizard wizard = new ImportAnnotationWizard();
//				wizard.init(getSite().getWorkbenchWindow().getWorkbench(),
//				            (IStructuredSelection)null);
		        WizardDialog dialog = new WizardDialog(ps.getShell(), wizard);
		        dialog.setBlockOnOpen(true);
		        dialog.open();

			}
		});
		tltmImport.setImage(ResourceManager.getPluginImage("org.integratedmodelling.thinkscape", "icons/application_go.png"));
		tltmImport.setText("Import");
		
		this.treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				treeModel.handleDoubleClick(
						((TreeSelection)treeViewer.getSelection()).getFirstElement(),
						getSite().getPage());
			}
		});
		tree.setSortDirection(SWT.DOWN);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setBounds(0, 0, 85, 85);
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnObservationType = treeViewerColumn_1.getColumn();
		trclmnObservationType.setWidth(31);
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnId = treeViewerColumn.getColumn();
		trclmnId.setWidth(83);
		trclmnId.setText("ID");
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnObservable = treeViewerColumn_2.getColumn();
		trclmnObservable.setWidth(305);
		trclmnObservable.setText("Observable");
		
		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnExtents = treeViewerColumn_3.getColumn();
		trclmnExtents.setWidth(100);
		trclmnExtents.setText("Extents");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setBounds(0, 0, 76, 21);

		this.treeModel = new AnnotationModel(treeViewer, this);
		rescan();
		
		treeViewer.addDropSupport(
				DND.DROP_COPY, 
				new Transfer[] { TextTransfer.getInstance() }, 
				new DropTargetListener() {
					@Override
					public void dropAccept(DropTargetEvent arg0) {
					}
					@Override
					public void drop(DropTargetEvent arg0) {
						/*
						 * if on a namespace and it's a source, open editor, add source and set marker to 
						 * it.
						 */
					}
					@Override
					public void dragOver(DropTargetEvent arg0) {
					}
					@Override
					public void dragOperationChanged(DropTargetEvent arg0) {
					}
					@Override
					public void dragLeave(DropTargetEvent arg0) {
					}
					@Override
					public void dragEnter(DropTargetEvent arg0) {
						arg0.detail = DND.DROP_COPY;
					}
				});
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(ThinkscapeEvent.WORKSPACE_CHANGE)) {
			ThinkScape.getDefault().getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					rescan();
				}
			});
	}
	}

	protected void rescan() {
		treeModel.instrumentConceptTree(ThinkScape.getActiveProject().getAnnotationNamespaces());
	}
}
