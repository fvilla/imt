package org.integratedmodelling.thinkscape.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.graphics.Color;

public class SingleAnnotationEditor extends Composite {
	private Composite contents;
	private SemanticAnnotationContainer container;
	private SemanticSource source;
	private ExpandItem barContent;
	private Composite parentContainer;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SingleAnnotationEditor(Composite parent, int style, SemanticAnnotationContainer container, SemanticSource source, Composite main) {
		super(parent, style);
		this.parentContainer = main;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ExpandBar expandBar = new ExpandBar(this, SWT.NONE);
		expandBar.addExpandListener(new ExpandAdapter() {
			@Override
			public void itemCollapsed(ExpandEvent e) {
				parentContainer.layout(true);
			}
			@Override
			public void itemExpanded(ExpandEvent e) {
				parentContainer.layout(true);
			}
		});

		this.barContent = new ExpandItem(expandBar, SWT.NONE);
		barContent.setExpanded(true);
		barContent.setText("Annotation");
		
		contents = new Composite(expandBar, SWT.NONE);
		barContent.setControl(contents);
		barContent.setHeight(60);
		contents.setLayout(new GridLayout(1, false));

		AnnotationConceptChooser cchoose = new AnnotationConceptChooser(contents, SWT.NONE);
		cchoose.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.container = container;
		this.source = source;
		
		setup();
	}

	private void setup() {
		
		this.barContent.setText(source.id);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	public Composite getContents() {
		return contents;
	}
}
