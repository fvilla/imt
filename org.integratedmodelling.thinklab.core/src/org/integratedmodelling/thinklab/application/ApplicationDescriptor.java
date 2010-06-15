package org.integratedmodelling.thinklab.application;


import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.owlapi.Session;
import org.integratedmodelling.utils.JPFUtils;


public class ApplicationDescriptor {

	ThinklabActivator registeringPlugin;
	private String id;
	private String description;
	String taskClass;
	String code;
	ArrayList<URL> scripts = new ArrayList<URL>();
	String language;
	String sessionClass;

	public ApplicationDescriptor(String id, ThinklabActivator plugin, Properties prop) throws ThinklabException {
		
		this.registeringPlugin = plugin;	
		this.setId(id);
		
		this.setDescription(prop.getProperty(ThinklabActivator.TASK_DECLARATION_PREFIX + id + ".description",
			""));

		this.language = 
			prop.getProperty(ThinklabActivator.TASK_DECLARATION_PREFIX + id + ".language");

		this.code = 
			prop.getProperty(ThinklabActivator.TASK_DECLARATION_PREFIX + id + ".inline_code");

		
		this.sessionClass = 
			prop.getProperty(ThinklabActivator.TASK_DECLARATION_PREFIX + id + ".session_class",
				Session.class.getCanonicalName());

		String urls = 
			prop.getProperty(ThinklabActivator.TASK_DECLARATION_PREFIX + id + ".code");
		
		if (urls != null) {
			
			if (language == null)
				throw new ThinklabValidationException(
						"task " + id + " has bindings but the language is unspecified");
			
			String[] res = urls.split(",");
			for (String r : res) {

				URL url = plugin.getResourceURL(r);
				if (url == null) {
					throw new ThinklabIOException("task script " + r + " not found in classpath");
				}
				this.scripts.add(url);
				
			}
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
