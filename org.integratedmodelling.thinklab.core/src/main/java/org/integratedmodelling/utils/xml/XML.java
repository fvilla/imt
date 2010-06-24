package org.integratedmodelling.utils.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.utils.Pair;
import org.integratedmodelling.utils.Polylist;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Make XML encoding as fun as possible using a functional style. Also automatically
 * converts (suitable) polylists to XML nodes.
 * 
 * Example:
 * 
 * XML.document("myns=http://my.namespace.org/ns"
 * 		XML.node("root",
 * 			XML.node("myns:content1", "Hi!")
 * 			XML.node("myns:content2", "Hi!")
 * 			XML.node("myns:content3", "Hi!")
 *      )).write("file.xml");
 * 
 * @author Ferdinando Villa
 * @see HTML for an HTML-specialized version.
 */
public class XML {
	
	public static class XmlNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -3750169814447901831L;
		String tag = null;
		ArrayList<Pair<String, String>> attrs = null;
		ArrayList<Object> contents = new ArrayList<Object>();
		
		protected XmlNode() {
		}
		
		public XmlNode attr(String s, String v) {
			if (attrs == null)
				attrs = new ArrayList<Pair<String,String>>();
			attrs.add(new Pair<String,String>(s,v));
			return this;
		}
		
		public void text(String text) {
			contents.add(text);
		}
		
		public XmlNode(String tag) {
			this.tag = tag;
		}
		
		Node create(Node parent, Document doc) throws ThinklabException {
			
			Node ret = doc.createElement(tag);
			
			if (attrs != null)
				for (Pair<String,String> a : attrs) {
					Attr attr = doc.createAttribute(a.getFirst());		
					attr.setValue(a.getSecond());
					((Element)ret).setAttributeNode(attr);					
				}
		
			for (Object o : contents) {

				if (o instanceof String) {
					String text = (String)o;
					XMLDocument.setTextContent(doc, ret, text);
				} else if (o instanceof Collection<?>) {
					for (Iterator<?> it = ((Collection<?>)o).iterator(); it.hasNext(); ) {
						Object no = it.next();
						if (!(no instanceof XmlNode)) {
							throw new ThinklabValidationException(
								"XML.node: collections must be of XmlNode");	
						}
						ret.appendChild(((XmlNode)no).create(ret, doc));
					}
				} else if (o instanceof XmlNode) {
					ret.appendChild(((XmlNode)o).create(ret, doc));
				}
			}
			
			return ret;
		}

		void define(Node self, Document doc) throws ThinklabException {
					
			if (attrs != null)
				for (Pair<String,String> a : attrs) {
					Attr attr = doc.createAttribute(a.getFirst());		
					attr.setValue(a.getSecond());
					((Element)self).setAttributeNode(attr);					
				}
			
			for (Object o : contents) {

				if (o instanceof String) {
					String text = (String)o;
					XMLDocument.setTextContent(doc, self, text);
				} else if (o instanceof Collection<?>) {
					for (Iterator<?> it = ((Collection<?>)o).iterator(); it.hasNext(); ) {
						Object no = it.next();
						if (no instanceof XmlNode) {
							self.appendChild(((XmlNode)no).create(self, doc));
						} else if (no instanceof Polylist) {
							self.appendChild(((Polylist)no).createXmlNode().create(self, doc));
						} else {
							throw new ThinklabValidationException(
								"XML.node: collections must be of XmlNode or Polylist");	
						}
					}
				} else if (o instanceof XmlNode) {
					self.appendChild(((XmlNode)o).create(self, doc));
				} else if (o instanceof Polylist) {
					self.appendChild(((Polylist)o).createXmlNode().create(self, doc));					
				}
			}			
		}
	}
	
	public static XMLDocument document(Object ...objects) throws ThinklabException {
	
		XmlNode root = null;
		ArrayList<String> namespaces = null;
		
		for (Object o : objects) {
			if (o instanceof String) {
				
				/*
				 * namespace
				 */
				if (namespaces == null)
					namespaces = new ArrayList<String>();
				
				//namespaces.add((String)o);
				
			} else if (o instanceof XmlNode) {
			 
				/*
				 * must be only root node
				 */
				if (root != null)
					throw new ThinklabValidationException(
							"XML document: non-unique root node");
				root = (XmlNode)o;
				
			} 
		}
		
		if (root == null)
			throw new ThinklabValidationException(
					"XML.document: no root node specified");	
		
		XMLDocument doc = new XMLDocument(root.tag);
		
		if (namespaces != null) {
			for (String ns : namespaces) {
				String[] nss = ns.split("=");
				if (nss.length != 2)
					throw new ThinklabValidationException(
						"XML.document: bad namespace specification: must be name=uri: " + ns);	
				doc.addNamespace(nss[0], nss[1]);
			}
		}
		
		root.define(doc.root(), doc.dom);
		
		return doc;
	}
	
	public static XmlNode node(String tag, Object ... objects) throws ThinklabException {
		
		XmlNode ret = new XmlNode(tag);
		
		if (objects == null)
			return ret;
		
		for (Object o : objects) {
			ret.contents.add(o);
		}
		
		return ret;
	}
	
	/*
	 * used only to implement derived classes such as HTML or GeoRSS
	 */
	protected static XmlNode node(XmlNode ret, String tag, Object ... objects) throws ThinklabException {
				
		ret.tag = tag;
		
		if (objects == null)
			return ret;

		for (Object o : objects) {
			ret.contents.add(o);
		}
				
		return ret;
	}
}
