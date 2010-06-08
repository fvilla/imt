package org.integratedmodelling.thinklab;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.integratedmodelling.thinklab.configuration.LocalConfiguration;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinklab.interfaces.annotations.InstanceImplementation;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstanceImplementation;
import org.integratedmodelling.thinklab.plugin.ThinklabPlugin;
import org.integratedmodelling.utils.CopyURL;
import org.integratedmodelling.utils.MiscUtilities;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public abstract class ThinklabActivator implements BundleActivator, IThinklabPlugin {

	protected ClassLoader _classloader;
	static ThinklabActivator _this;
	
	private File plugFolder = null;
	private File confFolder = null;
	private File dataFolder = null;
	
	private Properties properties = new Properties();
	
	private String id = null;
	private Bundle bundle = null; 

	public static ThinklabActivator getSelf() {
		return _this;
	}

	protected abstract void doStart() throws Exception;
	
	protected abstract void doStop() throws Exception;
	
	@Override
	public void start(BundleContext context) throws Exception {

		this.bundle = context.getBundle();
		this.id = context.getBundle().getSymbolicName();
		
		loadConfiguration();

		_this = this;
		_classloader = this.getClass().getClassLoader();		
		
		
		doStart();
		
	}

	
	protected void loadConfiguration() throws ThinklabIOException {
		
//		loadFolder = getLoadDirectory();

        plugFolder = LocalConfiguration.getDataDirectory(id);
        confFolder = new File(plugFolder + File.separator + "config");
        dataFolder = new File(plugFolder + File.separator + "data");
	
       /*
        * make sure we have all paths
        */
       if (
    		   (!plugFolder.isDirectory() && !plugFolder.mkdirs()) || 
    		   (!confFolder.isDirectory() && !confFolder.mkdirs()) || 
    		   (!dataFolder.isDirectory() && !dataFolder.mkdirs()))
    	   throw new ThinklabIOException("problem writing to plugin directory: " + plugFolder);
       
		/*
		 * check if plugin contains a plugin.properties file
		 */
       File pfile = new File(confFolder + File.separator + "plugin.properties");
       
       if (!pfile.exists()) {
    	   
    	   /*
    	    * copy stock properties if existing
    	    */
    	   URL sprop = getResourceURL("plugin.properties");
    	   if (sprop != null)
    		   CopyURL.copy(sprop, pfile);
       } 
       
       /*
        * load all non-customized properties files directly from plugin load dir
        */
       for (URL f : getResources("config", "*.properties", false)) {
			if (f.toString().endsWith(".properties") &&
			    !(f.toString().endsWith("plugin.properties"))) {
				try {
					logger().info("reading additional properties from " + f);
					InputStream inp = f.openStream();
					properties.load(inp);
					inp.close();
				} catch (Exception e) {
					throw new ThinklabIOException(e);
				}
			}
		}
       
       // load custom properties, overriding any in system folder.
       if (pfile.exists()) {
    	   try {
    		FileInputStream inp = new FileInputStream(pfile);
			properties.load(inp);
			inp.close();
			logger().info("plugin customized properties loaded from " + pfile);
		} catch (Exception e) {
			throw new ThinklabIOException(e);
		}
       }
	}

	
	
	@Override
	public void stop(BundleContext arg0) throws Exception {
		doStop();
	}

	@Override
	public Object createInstance(String clazz) throws ThinklabPluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return _classloader;
	}

	@Override
	public File getConfigPath() {
		return confFolder;
	}

	@Override
	public File getLoadPath() throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public URL getResourceURL(String resource) throws ThinklabIOException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<URL> getResources(String location, String pattern, boolean recurse) {
			
		Enumeration<URL> r = bundle.findEntries(location, pattern, recurse);
		ArrayList<URL> ret = new ArrayList<URL>();
		if (r != null) {
			while (r.hasMoreElements())	
				ret.add(r.nextElement());
		}
		return ret;
	}
	
	@Override
	public File getScratchPath() throws ThinklabException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasResource(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadLanguageBindings() throws ThinklabException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadLanguageBindings(String language) throws ThinklabException {
		// TODO Auto-generated method stub
	}

	@Override
	public Log logger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetClassLoader(ClassLoader clsl) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassLoader swapClassloader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeConfiguration() throws ThinklabIOException {
		// TODO Auto-generated method stub
	}


	protected void loadInstanceImplementationConstructors() throws ThinklabPluginException {
		
		String ipack = this.getClass().getPackage().getName() + ".implementations";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(IInstanceImplementation.class, ipack, getClassLoader())) {	
			
			String concept = null;

			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			/*
			 * lookup implemented concept
			 */
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof InstanceImplementation) {
					concept = ((InstanceImplementation)annotation).concept();
				}
			}
			
			if (concept != null) {

				String[] cc = concept.split(",");
				
				for (String ccc : cc) {
					logger().info("registering class " + cls + " as implementation for instances of type " + ccc);				
					KnowledgeManager.get().registerInstanceImplementationClass(ccc, cls);
				}
			}
		}
	}
	

	
}
