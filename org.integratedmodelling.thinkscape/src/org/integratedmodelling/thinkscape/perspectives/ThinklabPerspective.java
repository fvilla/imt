package org.integratedmodelling.thinkscape.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

public class ThinklabPerspective implements IPerspectiveFactory {

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
		layout.addView("org.integratedmodelling.thinkscape.views.ThinklabConsole", IPageLayout.BOTTOM, 0.8f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("org.integratedmodelling.thinkscape.views.Sources", IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("org.integratedmodelling.thinkscape.views.KnowledgeView", IPageLayout.RIGHT, 0.58f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("org.integratedmodelling.thinkscape.views.PluginView", IPageLayout.TOP, 0.18f, "org.integratedmodelling.thinkscape.views.Sources");
		layout.addView("org.integratedmodelling.thinkscape.views.ThinklabModels", IPageLayout.TOP, 0.5f, "org.integratedmodelling.thinkscape.views.Sources");
	}

	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
		layout.addFastView("org.eclipse.pde.runtime.LogView");
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}

}
