package org.integratedmodelling.thinklab.annotation;

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
public interface AnnotationProvider {

	Annotation getAnnotation(String sourceUrl);

	String getSourceURL(Node dn);

}
