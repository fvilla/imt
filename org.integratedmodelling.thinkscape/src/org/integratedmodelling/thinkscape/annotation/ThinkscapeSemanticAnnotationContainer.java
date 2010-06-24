package org.integratedmodelling.thinkscape.annotation;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.integratedmodelling.thinklab.annotation.DefaultAnnotationContainer;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;

public class ThinkscapeSemanticAnnotationContainer extends
		DefaultAnnotationContainer {

	private IFile file;

	/**
	 * The constructor associates with a workspace file but won't read it until initialize() is 
	 * called.
	 * 
	 * @param namespace
	 * @param file
	 */
	public ThinkscapeSemanticAnnotationContainer(String namespace, IFile file) {
		super(namespace);
		this.file = file;
	}

	public IFile getFile() {
		return file;
	}

	public void initialize() {
		InputStream inp;
		try {
			inp = file.getContents();
			this.load(inp);
			inp.close();
		} catch (Exception e) {
			throw new ThinklabRuntimeException(e);
		}
	}

}
