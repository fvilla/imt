package org.integratedmodelling.modelling.annotations.csv;

import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationProvider;
import org.integratedmodelling.thinklab.annotation.ISemanticAnnotationProvider;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

@ISemanticAnnotationProvider(id="CSV", dataSourceConcept="source:CSVDataSource")
public class CSVAnnotationProvider implements SemanticAnnotationProvider {

	@Override
	public void addObjectFromXML(Node n, SemanticAnnotationContainer ann) {
		// TODO Auto-generated method stub
	}

	@Override
	public SemanticAnnotationContainer annotateSource(String sourceUrl) throws ThinklabException {
		return null;
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
		"This translator allows to decompose a comma-separated file " +
		"(CSV) into one or more observations and annotate each one " +
		"associating spatial and temporal contexts with it.";
	}

	@Override
	public String getLabel() {
		return "Comma separated file from a spreadsheet";
	}

	@Override
	public SemanticAnnotation createEmptyAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}


}
