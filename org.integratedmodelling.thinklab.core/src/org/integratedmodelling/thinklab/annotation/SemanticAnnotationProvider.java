package org.integratedmodelling.thinklab.annotation;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.w3c.dom.Node;

/**
 * The annotation factory maintains a register of these, which must be annotated
 * with the IAnnotationProvider class and specify which URL format they handle
 * and which type of datasource they handle. In order to be recognized they must
 * be in the xxx.annotations package.
 * 
 * @author Ferdinando
 *
 */
public interface SemanticAnnotationProvider {

	/**
	 * Produce an annotation from the given URL, parsing all content. This is called after the 
	 * AnnotationFactory has chosen this provider based on a user choice of type.
	 * 
	 * @param sourceUrl
	 * @return
	 * @throws ThinklabException
	 */
	SemanticAnnotationContainer annotateSource(String sourceUrl) throws ThinklabException;

	/**
	 * Get the URL of the source from the given XML datasource specification.
	 * 
	 * @param dn
	 * @return
	 * @throws ThinklabException
	 */
	String getSourceURL(Node dn) throws ThinklabException;

	/**
	 * Reverse-engineer an object from the corresponding XML annotation and add it to the passed annotation
	 * container.
	 * 
	 * @param n
	 * @param ann
	 */
	void addObjectFromXML(Node n, SemanticAnnotationContainer ann);

	
	/**
	 * Create an empty annotation for the given source.
	 * 
	 * @param source
	 * @return
	 */
	SemanticAnnotationContainer createEmptyAnnotation(String source);

}
