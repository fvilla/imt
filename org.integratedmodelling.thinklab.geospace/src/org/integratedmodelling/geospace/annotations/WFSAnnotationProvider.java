package org.integratedmodelling.geospace.annotations;

import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationProvider;
import org.integratedmodelling.thinklab.annotation.ISemanticAnnotationProvider;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

@ISemanticAnnotationProvider(id="WFS", dataSourceConcept="geospace:WFSDataSource")
public class WFSAnnotationProvider implements SemanticAnnotationProvider {

	@Override
	public void addObjectFromXML(Node n, SemanticAnnotationContainer ann) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SemanticAnnotationContainer annotateSource(String sourceUrl) throws ThinklabException {
		return new WCSAnnotationContainer(sourceUrl);
	}

	@Override
	public SemanticAnnotationContainer createEmptyAnnotationContainer(String source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceURL(Node dn) throws ThinklabException {
		Node nn = XMLDocument.findNode(dn, "geospace:hasServiceURL");
		if (nn != null)
			return XMLDocument.getNodeValue(nn);
		return null;
	}

	@Override
	public String getDescription() {
		return 
		"This service connects to a Web Feature server and allows to annotate each " +
		"attribute of a feature store as a separate, spatially explicit observation.";
	}

	@Override
	public String getLabel() {
		return "Web Feature Service - feature map server URL";
	}

	@Override
	public SemanticAnnotation createEmptyAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}


}
