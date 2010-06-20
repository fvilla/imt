package org.integratedmodelling.thinklab.annotation;

import java.util.Collection;

import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Node;

/**
 * An annotation is a container of observation descriptors describing the 
 * semantics of the contents of a single source. It can be exported as 
 * XML or any other format that allows specifying instances, but for now we
 * support XML.
 * 
 * @author Ferdinando
 *
 */
public interface SemanticAnnotationContainer {

	/**
	 * Synchronize our contents with our source as much as supported.
	 */
	public void synchronize();
	
	/**
	 * Produce an XML document describing all of our content.
	 * @return
	 */
	public XMLDocument asXML();

	/**
	 * Reverse-engineer a single annotation object from the given XML node. If 
	 * object with same ID exists, synchronize with our content as appropriate for
	 * this source.
	 * 
	 * @param n
	 */
	public SemanticAnnotation annotateFromXML(Node n);

	/**
	 * Get the URL of our source. Must never return null.
	 * 
	 * @return
	 */
	public String getSourceUrl();
	
	/**
	 * Get a collection of all the IDs of the annotations. No assumptions made on the order. If 
	 * container is empty, return an empty collection, not null.
	 * 
	 * @return
	 */
	public Collection<String> getAnnotationIds();
	
	/**
	 * Get the annotation identified by the given ID, or null if not there.
	 * @param id
	 * @return
	 */
	public SemanticAnnotation getAnnotation(String id);
	
	/**
	 * Get the IDs of all sources that can be annotated, no matter if there is an annotation
	 * for them or not.
	 * 
	 * @return
	 */
	public Collection<String> getSourceIds();
	
	/**
	 * Get the source for the specific ID, or null.
	 * @param id
	 * @return
	 */
	public SemanticSource getSource(String id);
	
	/**
	 * Get the annotation for the given source ID, if any.
	 * @param sourceId
	 * @return
	 */
	public SemanticAnnotation getAnnotationForSource(String sourceId);

	/**
	 * Create the minimal annotation for this source, ready for editing.
	 * 
	 * @param source
	 * @param initialId 
	 */
	public SemanticAnnotation startAnnotation(SemanticSource source, String initialId);

	/**
	 * The global ID of this collection is a namespace string.
	 * @return
	 */
	public String getNamespace();
	
	/**
	 * Add or replace the given annotation.
	 * 
	 * @param annotation
	 */
	public void putAnnotation(SemanticAnnotation annotation);
	
}
