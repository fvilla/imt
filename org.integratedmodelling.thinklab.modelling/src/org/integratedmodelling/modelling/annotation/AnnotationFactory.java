package org.integratedmodelling.modelling.annotation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.opal.OPALLoader;
import org.integratedmodelling.opal.OPALPlugin;
import org.integratedmodelling.opal.OPALValidator;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.annotation.Annotation;
import org.integratedmodelling.thinklab.annotation.AnnotationProvider;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabStorageException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.datastructures.IntelligentMap;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

/**
 * Driver of annotation functions - turning external resources into observation
 * specifications and back.
 * 
 * @author Ferdinando
 *
 */
public class AnnotationFactory {

	private Hashtable<String, AnnotationProvider> byId = 
		new Hashtable<String, AnnotationProvider>();
	private IntelligentMap<AnnotationProvider> byConcept = 
		new IntelligentMap<AnnotationProvider>();
	
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
	public Collection<Annotation> parseXML(URL f, Collection<Annotation> annotations) throws ThinklabException {
		
		ArrayList<Annotation> ret = new ArrayList<Annotation>();
		HashMap<String,Annotation> anmap = new HashMap<String, Annotation>();
		XMLDocument	doc = new XMLDocument(f);

		for (Node n = doc.root().getFirstChild(); n != null; n = n
				.getNextSibling()) {
			
			Node dn = XMLDocument.findNode(n, CoreScience.HAS_DATASOURCE);
			if (dn == null)
				// for now
				continue;
			
			/*
			 * get the annotator based on the ds concept
			 */
			IConcept c =
				KnowledgeManager.get().requireConcept(dn.getNodeName());

			AnnotationProvider prv = byConcept.get(c);
			if (prv == null)
				continue;

			String source = prv.getSourceURL(dn);
			Annotation ann = anmap.get(source);
			if (ann == null)
				ann = prv.getAnnotation(source);

			ann.addObjectFromXML(n);
		}		
		
		for (Annotation ann : anmap.values())
			ret.add(ann);
		
		Collections.sort(ret, new Comparator<Annotation>() {
			@Override
			public int compare(Annotation o1, Annotation o2) {
				return o1.getSourceUrl().compareTo(o2.getSourceUrl());
			}
		});
		
		return ret;
	}
	
	public void registerAnnotation(IConcept dataconcept, String id, AnnotationProvider annotation) {
		byConcept.put(dataconcept, annotation);
		byId.put(id, annotation);
	}
	
}
