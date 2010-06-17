package org.integratedmodelling.thinklab.annotation;

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
public interface AnnotationContainer {

	/**
	 * Synchronize our contents with our source.
	 */
	public void synchronize();
	
	/**
	 * Produce an XML document describing all of our content.
	 * @return
	 */
	public XMLDocument asXML();

	/**
	 * Add the given object from the given node. If object exists, synchronize with our content.
	 * 
	 * @param n
	 */
	public void addObjectFromXML(Node n);

	/**
	 * Get the URL of our source. Must never return null.
	 * 
	 * @return
	 */
	public String getSourceUrl();
}
