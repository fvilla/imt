package org.integratedmodelling.thinklab.interfaces;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;


public interface IThinklabPlugin {
	

	public abstract ClassLoader getClassLoader();

	public abstract Log logger();

	/**
	 * Load bindings for all languages. Because this must be called by hand (see reason in
	 * loadLanguageBindings(String), typically you want to use the language-specific version,
	 * but here goes.
	 * 
	 * @throws ThinklabException
	 */
	public abstract void loadLanguageBindings() throws ThinklabException;

	/**
	 * Load declared bindings for given language. Must be called by hand and must make sure
	 * that if the bindings refer to plugin classes, the plugin that calls the interpreter has 
	 * declared its dependencies with reverse-lookup=true, otherwise the bindings won't see the 
	 * Java classes in their own plugin. 
	 * 
	 * @param language
	 * @throws ThinklabException
	 */
	public abstract void loadLanguageBindings(String language)
			throws ThinklabException;

	public abstract ClassLoader swapClassloader();

	public abstract void resetClassLoader(ClassLoader clsl);

//	public abstract File getLoadDirectory();

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

}