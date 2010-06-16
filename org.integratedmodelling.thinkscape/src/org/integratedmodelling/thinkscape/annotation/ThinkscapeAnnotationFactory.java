package org.integratedmodelling.thinkscape.annotation;

import org.eclipse.core.resources.IFile;
import org.integratedmodelling.thinklab.interfaces.knowledge.IKnowledgeSubject;
import org.integratedmodelling.thinkscape.ThinkScape;
import org.integratedmodelling.thinkscape.project.ThinklabProject;

public class ThinkscapeAnnotationFactory {

	/**
	 * Get a file to hold the concept's axioms. These are collected and their axioms 
	 * put into the project's ontology by the builder.
	 *  
	 * @param concept
	 * @return
	 */
	public static IFile getConceptAnnotationFile(IKnowledgeSubject concept) {

		IFile ret = null;
		ThinklabProject tp = ThinkScape.getActiveProject();
		if (tp != null) {
			
			String fname = 
				"ontologies/" + 
				concept.toString().replace(':', '-') + 
				// TODO TEMP SWITCH BACK TO .concept
				".model";
			
			ret = tp.project.getFile(fname);
			
		}
		return ret;
	}

	public static IFile getSourceAnnotationFile(String concept) {

		IFile ret = null;
		ThinklabProject tp = ThinkScape.getActiveProject();
		if (tp != null) {
			
			String fname = 
				"annotations/" + 
				concept.toString().replace(':', '-') + 
				".ann";
			
			ret = tp.project.getFile(fname);
		}
		return ret;
	}
	

	public static IFile getModelFile(String modelId) {

		IFile ret = null;
		ThinklabProject tp = ThinkScape.getActiveProject();
		if (tp != null) {
			
			String fname = 
				"models/" + 
				modelId.replace('/', '.') + 
				".model";
			
			ret = tp.project.getFile(fname);
			
		}
		return ret;
	}

	
}
