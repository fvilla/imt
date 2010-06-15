package org.integratedmodelling.modelling.annotation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.integratedmodelling.opal.OPALLoader;
import org.integratedmodelling.opal.OPALPlugin;
import org.integratedmodelling.opal.OPALValidator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.datastructures.IntelligentMap;
import org.integratedmodelling.utils.xml.XMLDocument;

/**
 * Driver of annotation functions - turning external resources into observation
 * specifications and back.
 * 
 * @author Ferdinando
 *
 */
public class AnnotationFactory {

	private Hashtable<String, Annotation> byId = 
		new Hashtable<String, Annotation>();
	private IntelligentMap<Annotation> byConcept = 
		new IntelligentMap<Annotation>();
	
	public Annotation annotate(URL sourceUrl) throws ThinklabException {
		return null;
	}
	
	/**
	 * Reverse-engineer an XML document into one or more annotations, reconstructing
	 * the sources from the datasource. If a collection of annotations is passed,
	 * add to them when the source is the same.
	 * 
	 * @param doc
	 * @return
	 */
	public Collection<Annotation> parseXML(URL doc, Collection<Annotation> annotations) throws ThinklabException {
		
		ArrayList<Annotation> ret = new ArrayList<Annotation>();
		
		OPALValidator opal = new OPALValidator();
		
		
		
		return ret;
	}
	
	public void registerAnnotation(IConcept dataconcept, String id, Annotation annotation) {
		
	}
	
}
