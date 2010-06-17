package org.integratedmodelling.thinklab.interfaces;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;


public interface IThinklabPlugin {
	

	public abstract ClassLoader getClassLoader();

	/**
	 * Load bindings for all languages. Because this must be called by hand (see reason in
	 * loadLanguageBindings(String), typically you want to use the language-specific version,
	 * but here goes.
	 * 
	 * @throws ThinklabException
	 */
	public abstract void loadLanguageBindings() throws ThinklabException;

	public abstract ClassLoader swapClassloader();

	public abstract void resetClassLoader(ClassLoader clsl);

	public abstract void writeConfiguration() throws ThinklabIOException;
	
	public abstract URL getResourceURL(String resource) throws ThinklabIOException;

	public abstract Object createInstance(String clazz)
			throws ThinklabPluginException;

	/**
	 * Use to check if a specific resource has been found in the JAR
	 * @param name
	 * @return
	 */
	public abstract boolean hasResource(String name);

	/**
	 * Return the plugin properties, read from any .properties file in distribution.
	 * @return the plugin Properties. It's never null.
	 */
	public abstract Properties getProperties();

	public abstract File getScratchPath() throws ThinklabException;

	public abstract File getLoadPath() throws ThinklabException;

	public abstract File getConfigPath();

	/**
	 * All registered ontologies are cached in this local directory, where they can be read from
	 * and modified. This should return null if there are no ontologies in the plugin.
	 * 
	 * @return
	 */
	public abstract File getOntologiesLocation();

	void info(String message);

	void warn(String message);

	void error(String message);

	void debug(String message);

	public String getId();
	
	public Version getVersion();

	public abstract String getStatus();

	public abstract Bundle getBundle();

}