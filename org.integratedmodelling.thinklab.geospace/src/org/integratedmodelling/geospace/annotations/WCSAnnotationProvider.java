package org.integratedmodelling.geospace.annotations;

import org.integratedmodelling.thinklab.annotation.AnnotationContainer;
import org.integratedmodelling.thinklab.annotation.AnnotationProvider;
import org.integratedmodelling.thinklab.annotation.IAnnotationProvider;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.w3c.dom.Node;

@IAnnotationProvider(id="WCS",description="Web Coverage Service",
			dataSourceConcept="geospace:WCSDataSource")
public class WCSAnnotationProvider implements AnnotationProvider {

	@Override
	public void addObjectFromXML(Node n, AnnotationContainer ann) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnnotationContainer annotateSource(String sourceUrl) throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationContainer createEmptyAnnotation(String source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceURL(Node dn) throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}


}
