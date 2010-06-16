package org.integratedmodelling.geospace.annotations;

import org.integratedmodelling.thinklab.annotation.Annotation;
import org.integratedmodelling.thinklab.annotation.AnnotationProvider;
import org.integratedmodelling.thinklab.annotation.IAnnotationProvider;
import org.w3c.dom.Node;

@IAnnotationProvider(id="WCS",description="Web Coverage Service",
			dataSourceConcept="geospace:WCSDataSource")
public class WCSAnnotationProvider implements AnnotationProvider {

	@Override
	public Annotation getAnnotation(String sourceUrl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceURL(Node dn) {
		// TODO Auto-generated method stub
		return null;
	}

}
