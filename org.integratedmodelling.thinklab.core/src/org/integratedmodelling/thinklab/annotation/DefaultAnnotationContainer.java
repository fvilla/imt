package org.integratedmodelling.thinklab.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.utils.Path;
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

	@Override
	public void load(InputStream inp) throws ThinklabException {
		
		Properties p = new Properties();
		try {
			p.load(inp);
		} catch (IOException e) {
			throw new ThinklabIOException(e);
		}
		
		for (Object pp : p.keySet()) {
			String aname = Path.getFirst(pp.toString(), '.');
			SemanticAnnotation ann = requireAnnotation(aname, p);
			ann.put(pp, p.get(pp));
		}
		
	}

	private SemanticAnnotation requireAnnotation(String aname, Properties p) throws ThinklabValidationException {

		SemanticAnnotation ret = annotations.get(aname);
		if (ret == null) {
			String pclass = p.getProperty(aname + "." + SemanticAnnotation.ANNOTATION_PROVIDER_ID);
			if (pclass == null)
				throw new ThinklabValidationException(
						"internal: annotation storage does not contain class for annotation " + aname);
			
			SemanticAnnotationProvider prv = SemanticAnnotationFactory.get().getAnnotator(pclass);
			ret = prv.createEmptyAnnotation();
			annotations.put(aname, ret);
		}
		return ret;
	}

	@Override
	public void store(OutputStream out) throws ThinklabException {
		for (SemanticAnnotation a : annotations.values()) {
			try {
				a.store(out, null);
			} catch (IOException e) {
				throw new ThinklabIOException(e);
			}
		}
		
	}

}
