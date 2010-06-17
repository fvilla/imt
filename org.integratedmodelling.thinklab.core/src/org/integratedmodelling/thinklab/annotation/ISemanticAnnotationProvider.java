/**
 * 
 */
package org.integratedmodelling.thinklab.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Ferdinando
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ISemanticAnnotationProvider {
	public String id();
	public String description();
	public String dataSourceConcept();
}
