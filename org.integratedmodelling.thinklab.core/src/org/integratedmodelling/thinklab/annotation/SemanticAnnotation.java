package org.integratedmodelling.thinklab.annotation;

import java.util.Properties;

/**
 * A semantic annotation is a specialized Properties object.
 * 
 * @author Ferdinando
 *
 */
public class SemanticAnnotation extends Properties {
	
	private static final long serialVersionUID = 4178258864664558292L;
	
	public static final String LAST_MODIFICATION_TIME_PROPERTY = "thinklab.annotation.last_modified";
	public static final String DIRECT_TYPE_PROPERTY = "thinklab.annotation.direct_type";
	
	public String id;
	
	public SemanticAnnotation(String id) {
		this.id = id;
	}
	
	
	/**
	 * Annotation must be able to return their time of last modification.
	 * 
	 * @return
	 */
	public long getLastModificationTime() {
		return Long.parseLong(this.getProperty(LAST_MODIFICATION_TIME_PROPERTY));
	}

	@Override
	public String getProperty(String key) {
		return super.getProperty(id + "." + key);
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		return super.setProperty(id + "." + key, value);
	}
		
}
