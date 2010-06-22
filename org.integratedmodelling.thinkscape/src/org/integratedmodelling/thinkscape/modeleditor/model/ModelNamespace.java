package org.integratedmodelling.thinkscape.modeleditor.model;

import org.eclipse.core.resources.IFile;
import org.integratedmodelling.modelling.visualization.model.ModelDiagram;

/**
 * Represent the full contents of a model namespace. It is associated to a model file and layout descriptor
 * in the models/ folder of a project. ThinklabProject maintains a list of these, which can be defined 
 * inside Thinkscape and contain stuff imported from Clojure.
 * 
 * @author Ferdinando
 *
 */
public class ModelNamespace extends ModelDiagram {

	private String namespace;
	private IFile modelFile;
	

	public ModelNamespace(String namespace, IFile modelFile) {
		this.namespace = namespace;
		this.modelFile = modelFile;
	}
	
	public String getNamespace() {
		return this.namespace;
	}
	
	public IFile getFile() {
		return modelFile;
	}
}
