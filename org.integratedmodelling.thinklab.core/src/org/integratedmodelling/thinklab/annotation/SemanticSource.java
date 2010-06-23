package org.integratedmodelling.thinklab.annotation;

import java.util.Properties;

import org.integratedmodelling.utils.NameGenerator;
import org.integratedmodelling.utils.Path;

public class SemanticSource extends Properties {

	private static final long serialVersionUID = 283002194030928734L;
	private static final String LAST_MODIFICATION_TIME_PROPERTY = "last_modified";
	public static final String NAME_PROPERTY = "name";

	private String id;
	
	public SemanticSource(String name) {
		this.id = NameGenerator.newId("sm");
		super.setProperty(this.id + "." + NAME_PROPERTY, id);
	}
	
	protected String getId() {
		return id;
	}
	
	public void setName(String name) {
		setProperty(NAME_PROPERTY, name);		
	}
	
	public String getName() {
		return getProperty(NAME_PROPERTY);
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
		put(id + "." + s, o.toString());
	}
	
	public String get(String s) {	
		return super.get(id + "." + s).toString();
	}
	
	public void set(SemanticSource source) {
		
		for (Object s : source.keySet()) {
			String ss = Path.getTrailing(s.toString(), '.');
			this.setProperty(ss, source.get(ss));
		}
	}
}
