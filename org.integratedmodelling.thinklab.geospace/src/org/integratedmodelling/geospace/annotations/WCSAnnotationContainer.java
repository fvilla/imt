package org.integratedmodelling.geospace.annotations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.geospace.coverage.WCSCoverage;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotationContainer;
import org.integratedmodelling.thinklab.annotation.SemanticSource;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.utils.xml.XMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WCSAnnotationContainer implements SemanticAnnotationContainer {

	private String serviceUrl;
	private HashMap<String, WCSSource> sources = new HashMap<String, WCSSource>();
	private HashMap<String, SemanticAnnotation> annotations = new HashMap<String, SemanticAnnotation>();
	
	public class WCSSource extends SemanticSource {

		public WCSSource(String id) {
			super(id);
		}

		private static final long serialVersionUID = 4003537124729951143L;
	}
	
	public WCSAnnotationContainer(String sourceUrl) throws ThinklabException {
		super();
		serviceUrl = sourceUrl;
		initialize();
	}

	private void initialize() throws ThinklabException {

		XMLDocument cap = null;
		try {
			 cap = 
				 new XMLDocument(
					new URL(serviceUrl + "?service=WCS&version=1.0.0&request=getCapabilities"));
		} catch (MalformedURLException e) {
			throw new ThinklabValidationException(e);
		}
		
		Node n = cap.findNode("ContentMetadata");
		if (n == null)
			throw new ThinklabIOException("service " + serviceUrl + " is offline or not a WCS service");
		
		parseMetadata(n, null);
	}
	
	private int parseMetadata(Node n, String match) throws ThinklabException {

		int i = 0; Node child; Node next = (Node)n.getFirstChild();
		while ((child = next) != null) {
				 
			  next = child.getNextSibling(); 
			  if (child.getNodeName().equals("CoverageOfferingBrief")) {
				  
				  String covId = XMLDocument.getTextValue((Element) child, "name");
				  if (match != null && !covId.startsWith(match))
					  continue;
				  try {
					  parseDescriptor(covId);
				  } catch (ThinklabException e)	{
					  Geospace.get().error(e.toString());
				  }
				  i++;
			  }
		  }
		  
		  return i;
	}
	
	private void parseDescriptor(String coverageId) throws ThinklabException {

		WCSSource source = new WCSSource(coverageId);
		URL url = null;
		try {
			url = new URL(
					serviceUrl
							+ "?service=WCS&version=1.0.0&request=DescribeCoverage&coverage="
							+ coverageId);
		} catch (MalformedURLException e) {
			throw new ThinklabValidationException(e);
		}

		String[] dimSpecs = new String[2];

		XMLDocument desc = new XMLDocument(url);
		// desc.dump(System.out);

		Node n = desc.findNode("gml:Envelope");

		if (n == null)
			throw new ThinklabIOException("no envelope found for coverage " + coverageId);
			
		String srs = XMLDocument.getAttributeValue(n, "srsName").trim();

		if (srs != null) {
			source.set("srs", srs);
		} else {
			if (n == null)
				throw new ThinklabIOException("no coordinate reference system found for coverage " + coverageId);
		}
		
		int i = 0;
		Node child;
		Node next = (Node) n.getFirstChild();
		while ((child = next) != null) {

			next = child.getNextSibling();
			if (child.getNodeName().equals("gml:pos"))
				dimSpecs[i++] = XMLDocument.getNodeValue(child);
		}

		/*
		 * process dims
		 */
		double x1, y1, x2, y2;
		String[] z = dimSpecs[0].split("\\ ");
		x1 = Double.parseDouble(z[0]);
		y1 = Double.parseDouble(z[1]);
		z = dimSpecs[1].split("\\ ");
		x2 = Double.parseDouble(z[0]);
		y2 = Double.parseDouble(z[1]);

		n = desc.findNode("gml:GridEnvelope");
		i = 0;
		next = (Node) n.getFirstChild();
		while ((child = next) != null) {

			next = child.getNextSibling();
			if (child.getNodeName().equals("gml:low"))
				dimSpecs[0] = XMLDocument.getNodeValue(child).trim();
			else if (child.getNodeName().equals("gml:high"))
				dimSpecs[1] = XMLDocument.getNodeValue(child).trim();
		}

		source.set("x1", x1);
		source.set("y1", y1);
		source.set("x2", x2);
		source.set("y2", y2);
		
		/*
		 * process pixel size
		 */
		int sx1, sy1, sx2, sy2;
		z = dimSpecs[0].split("\\ ");
		sx1 = Integer.parseInt(z[0]);
		sy1 = Integer.parseInt(z[1]);
		z = dimSpecs[1].split("\\ ");
		sx2 = Integer.parseInt(z[0]);
		sy2 = Integer.parseInt(z[1]);

		source.set("sx1", sx1);
		source.set("sy1", sy1);
		source.set("sx2", sx2);
		source.set("sy2", sy2);
		
		/*
		 * read no data values.
		 */
		ArrayList<Double> noData = null;
		n = desc.findNode("nullValues");
		if (n != null) {

			next = (Node) n.getFirstChild();
			while ((child = next) != null) {
				next = child.getNextSibling();
				if (child.getNodeName().equals("singleValue")) {
					noData = new ArrayList<Double>();
					noData.add(Double.parseDouble(XMLDocument.getNodeValue(
							child).toString()));
				}
			}
		}

		if (noData != null)
			source.set("nodata", noData);

		/*
		 * keywords: recognize KVP and instantiate properties from them
		 */
		n = desc.findNode("keywords");
		i = 0;
		next = n == null ? null : (Node) n.getFirstChild();
		while ((child = next) != null) {

			next = child.getNextSibling();
			if (child.getNodeName().equals("keyword")) {
				String kw = XMLDocument.getNodeValue(child).trim();
				String[] zoz = kw.split("\\ ");
				for (String kz : zoz) {
					if (kz.contains("=")) {
						String[] kvp = kz.split("=");
						if (kvp.length == 2)
							source.setProperty("keyword." + kvp[0], kvp[1]);
					}
				}
			}
		}

		source.set("xCellSize", (x2 - x1) / (sx2 - sx1));
		source.set("yCellSize", (y2 - y1) / (sy2 - sy1));
		
		/*
		 * TODO validate:
		 * 	1. must have SRS
		 * 	2. check concepts from KWs, leave other kws unaltered
		 */
		
		sources.put(coverageId, source);

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
		return annotations.get(sourceId);
	}

	@Override
	public Collection<String> getAnnotationIds() {
		return annotations.keySet();
	}

	@Override
	public SemanticSource getSource(String id) {
		return sources.get(id);
	}

	@Override
	public Collection<String> getSourceIds() {
		return sources.keySet();
	}

	@Override
	public String getSourceUrl() {
		return serviceUrl;
	}

	@Override
	public void synchronize() {

		// for now we don't bother with REST; just ensure that all annotations we do have
		// reflect what the latest source contains.
		
		// TODO Auto-generated method stub

	}

	@Override
	public SemanticAnnotation startAnnotation(SemanticSource source, String initialId) {
		
		RasterAnnotation ann = new RasterAnnotation(initialId);
		ann.putAll(source);
		return ann;
		
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAnnotation(SemanticAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

}
