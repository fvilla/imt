package org.integratedmodelling.thinkscape.annotation;

import org.eclipse.core.resources.IFile;
import org.integratedmodelling.thinklab.annotation.DefaultAnnotationContainer;

public class ThinkscapeSemanticAnnotationContainer extends
		DefaultAnnotationContainer {

	private IFile file;

	public ThinkscapeSemanticAnnotationContainer(String namespace, IFile file) {
		super(namespace);
		this.file = file;
	}

	public IFile getFile() {
		return file;
	}

}
