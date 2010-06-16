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
public interface Annotation {

	
	
	public XMLDocument asXML();

	public void addObjectFromXML(Node n);

	public String getSourceUrl();
}
