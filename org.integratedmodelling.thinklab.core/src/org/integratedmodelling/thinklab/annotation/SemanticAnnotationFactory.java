package org.integratedmodelling.thinklab.annotation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabResourceNotFoundException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.datastructures.IntelligentMap;
import org.integratedmodelling.utils.MiscUtilities;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

/**
 * Driver of annotation functions - turning external resources into observation
 * specifications and back.
 * 
 * @author Ferdinando
 *
 */
public class SemanticAnnotationFactory {

	private Hashtable<String, SemanticAnnotationProvider> byId = 
		new Hashtable<String, SemanticAnnotationProvider>();
	private IntelligentMap<SemanticAnnotationProvider> byConcept = 
		new IntelligentMap<SemanticAnnotationProvider>();
	
	private static SemanticAnnotationFactory _this = null;
	
	private SemanticAnnotationFactory() {
	}
	
	public static SemanticAnnotationFactory get() {
		if (_this == null)
			_this = new SemanticAnnotationFactory();
		return _this;
	}
	
	public SemanticAnnotationContainer createAnnotationContainer(
			String annotationServiceId, URL sourceUrl) throws ThinklabException {
		
		SemanticAnnotationProvider prv = byId.get(annotationServiceId);
		
		if (prv == null) {
			throw new ThinklabResourceNotFoundException(
					"no annotation provider of type " + annotationServiceId + " was registered");
		}
		
		return prv.annotateSource(sourceUrl.toString());
	}
	
	/**
	 * Read a property file containing the class of the container plus a number of annotation
	 * definitions. Create the container using Class.newInstance() and have it create all the annotations, then
	 * return it.
	 * 
	 * @param sourceUrl
	 * @throws ThinklabException 
	 */
	public SemanticAnnotationContainer getAnnotationContainer(URL sourceUrl) throws ThinklabException {

		SemanticAnnotationContainer ret = 
			new DefaultAnnotationContainer(MiscUtilities.getNameFromURL(sourceUrl.toString()));
		
		try {
			ret.load(sourceUrl.openStream());
		} catch (IOException e) {
			throw new ThinklabIOException(e);
		}
		return ret;
	}
	
	/**
	 * Reverse-engineer an XML document into one or more annotations, reconstructing
	 * the sources from the datasource. If a collection of annotations is passed,
	 * add to them when the source is the same.
	 * 
	 * @param doc
	 * @return
	 */
	public Collection<SemanticAnnotationContainer> parseXML(URL f, Collection<SemanticAnnotationContainer> annotations) throws ThinklabException {
		
		ArrayList<SemanticAnnotationContainer> ret = new ArrayList<SemanticAnnotationContainer>();
		HashMap<String,SemanticAnnotationContainer> anmap = new HashMap<String, SemanticAnnotationContainer>();
		XMLDocument	doc = new XMLDocument(f);

		for (Node n = doc.root().getFirstChild(); n != null; n = n
				.getNextSibling()) {
			
			Node dn = XMLDocument.findNode(n, "observation:hasDataSource");
			if (dn == null)
				// for now
				continue;
			
			/*
			 * get the annotator based on the ds concept
			 */
			IConcept c =
				KnowledgeManager.get().requireConcept(dn.getNodeName());

			SemanticAnnotationProvider prv = byConcept.get(c);
			if (prv == null)
				continue;

			String source = prv.getSourceURL(dn);
			SemanticAnnotationContainer ann = anmap.get(source);
			if (ann == null)
				ann = prv.createEmptyAnnotationContainer(source);

			prv.addObjectFromXML(n, ann);
		}		
		
		for (SemanticAnnotationContainer ann : anmap.values())
			ret.add(ann);
		
		Collections.sort(ret, new Comparator<SemanticAnnotationContainer>() {
			@Override
			public int compare(SemanticAnnotationContainer o1, SemanticAnnotationContainer o2) {
				return o1.getSourceUrl().compareTo(o2.getSourceUrl());
			}
		});
		
		return ret;
	}
	
	public void registerAnnotationProvider(IConcept dataconcept, String id, SemanticAnnotationProvider annotation) {
		byConcept.put(dataconcept, annotation);
		byId.put(id, annotation);
	}

	public Collection<String> getAnnotatorIds() {
		return byId.keySet();
	}
	
	public SemanticAnnotationProvider getAnnotator(String id) {
		return byId.get(id);
	}
	
}
