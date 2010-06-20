package org.integratedmodelling.thinklab.annotation;

import java.util.Collection;
import java.util.HashMap;

import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

public class DefaultAnnotationContainer implements
		SemanticAnnotationContainer {

	String namespace;
	
	HashMap<String, SemanticAnnotation> annotations = 
		 new HashMap<String, SemanticAnnotation>();
	
	public DefaultAnnotationContainer(String namespace) {
		this.namespace = namespace;
	}
	
	@Override
	public SemanticAnnotation annotateFromXML(Node n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLDocument asXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SemanticAnnotation getAnnotation(String id) {
		return annotations.get(id);
	}

	@Override
	public SemanticAnnotation getAnnotationForSource(String sourceId) {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public Collection<String> getAnnotationIds() {
		return annotations.keySet();
	}

	public void addAnnotation(SemanticAnnotation ann) {
		annotations.put(ann.getId(), ann);
	}
	
	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return namespace;
	}

	@Override
	public SemanticSource getSource(String id) {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public Collection<String> getSourceIds() {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public String getSourceUrl() {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public SemanticAnnotation startAnnotation(SemanticSource source,
			String initialId) {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public void synchronize() {
		throw new ThinklabRuntimeException("internal: method should not be called on observation container");
	}

	@Override
	public void putAnnotation(SemanticAnnotation annotation) {
		annotations.put(annotation.getId(), annotation);
	}

}
