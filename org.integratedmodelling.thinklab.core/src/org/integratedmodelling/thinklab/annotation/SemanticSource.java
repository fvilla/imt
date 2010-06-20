package org.integratedmodelling.thinklab.annotation;

import java.util.Properties;

public class SemanticSource extends Properties {

	private static final long serialVersionUID = 283002194030928734L;
	private static final String LAST_MODIFICATION_TIME_PROPERTY = "last_modified";
	
	private String id;
	
	public SemanticSource(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
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
	
	public void set(String s, Object o) {
		put(id + "." + s, o);
	}
}
