package org.integratedmodelling.geospace.annotations;

import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationProvider;
import org.integratedmodelling.thinklab.annotation.ISemanticAnnotationProvider;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.w3c.dom.Node;

@ISemanticAnnotationProvider(id="WCS",description="Web Coverage Service",
			dataSourceConcept="geospace:WCSDataSource")
public class WCSAnnotationProvider implements SemanticAnnotationProvider {

	@Override
	public void addObjectFromXML(Node n, SemanticAnnotationContainer ann) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SemanticAnnotationContainer annotateSource(String sourceUrl) throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SemanticAnnotationContainer createEmptyAnnotation(String source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceURL(Node dn) throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}


}
