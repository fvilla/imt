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

import org.integratedmodelling.thinklab.configuration.LocalConfiguration;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinklab.interfaces.annotations.InstanceImplementation;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstanceImplementation;
import org.integratedmodelling.thinklab.kbox.KBoxManager;
import org.integratedmodelling.thinklab.plugin.IPluginLifecycleListener;
import org.integratedmodelling.utils.CopyURL;
import org.integratedmodelling.utils.MiscUtilities;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public abstract class ThinklabActivator implements BundleActivator, IThinklabPlugin {

	protected ClassLoader _classloader;
	static ThinklabActivator _this;
	
	private File plugFolder = null;
	private File confFolder = null;
	private File dataFolder = null;
	private File ontoFolder = null;
	
	private Properties properties = new Properties();
	
	private String id = null;
	private Bundle bundle = null;
	private BundleContext context = null;
	private LogService logservice; 
	protected static KnowledgeManager _km;
	
	public static ThinklabActivator getSelf() {
		return _this;
	}

	protected abstract void doStart() throws Exception;
	
	protected abstract void doStop() throws Exception;
	
	@Override
	public void start(BundleContext context) throws Exception {

        ServiceTracker logServiceTracker = 
        	new ServiceTracker(context, org.osgi.service.log.LogService.class.getName(), null);
        logServiceTracker.open();
        this.logservice = (LogService) logServiceTracker.getService();
       
		this.context  = context;
		this.bundle = context.getBundle();
		this.id = context.getBundle().getSymbolicName();
		
		loadConfiguration();

		_this = this;
		_classloader = this.getClass().getClassLoader();		
		
		if (_km == null)
			_km = new KnowledgeManager();
		
		for (IPluginLifecycleListener lis : KnowledgeManager.getPluginListeners()) {
			lis.prePluginLoaded(this);
		}
		
//		preStart();
//		
		loadOntologies();
//		loadLiteralValidators();
//		loadKBoxHandlers();
//		loadKnowledgeImporters();
//		loadKnowledgeLoaders();
//		loadLanguageInterpreters();
//		loadCommandHandlers();
//		loadListingProviders();
//		loadTransformations();
//		loadCommands();
		loadInstanceImplementationConstructors();
//		loadPersistentClasses();
//		loadSessionListeners();
		loadKboxes();
//		loadApplications();
		loadLanguageBindings();
		
		doStart();

//		loadExtensions();
		
		for (IPluginLifecycleListener lis : KnowledgeManager.getPluginListeners()) {
			lis.onPluginLoaded(this);
		}

	}

	
	private void loadOntologies() throws ThinklabException {
		
		// cache in local dir; set ontoFolder to null if no ontologies in plugin.
		boolean found = false;
		for (URL f : getResources("ontologies", "*.owl", true)) {
			if (!found) {
				if (!ontoFolder.isDirectory() && !ontoFolder.mkdirs()) {
					throw new ThinklabIOException("problem writing to ontology directory: " + 
								plugFolder);
				}
				found = true;
			}
		}
		
		if (!found)
			ontoFolder = null;
	}

	protected void loadConfiguration() throws ThinklabIOException {
		
//		loadFolder = getLoadDirectory();
		
        plugFolder = LocalConfiguration.getDataDirectory(id);
        confFolder = new File(plugFolder + File.separator + "config");
        dataFolder = new File(plugFolder + File.separator + "data");
        ontoFolder = new File(plugFolder + File.separator + "ontologies");
	
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
					info("reading additional properties from " + f);
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
			info("plugin customized properties loaded from " + pfile);
		} catch (Exception e) {
			throw new ThinklabIOException(e);
		}
       }
	}

	private void loadKboxes() throws ThinklabException {

		/*
		 * load .kbox files from data dir
		 */
		for (String s : this.getScratchPath().list()) {
			
			if (s.endsWith(".kbox")) {
				// load from kbox properties 
				File pfile = new File(this.getScratchPath() + File.separator + s);
				try {
					KBoxManager.get().requireGlobalKBox(pfile.toURI().toURL().toString());
				} catch (Exception e) {
					throw new ThinklabIOException(e);
				}
			}
		}
	}
	
	@Override
	public void stop(BundleContext arg0) throws Exception {
		doStop();
		this.bundle = null;
		this.context = null;
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
		return bundle.getResource(resource);
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
	public void info(String message) {
        if (logservice != null)
            logservice.log(LogService.LOG_INFO, message);
	}
	
	@Override
	public void warn(String message) {
        if (logservice != null)
            logservice.log(LogService.LOG_WARNING, message);
	}
	
	@Override
	public void error(String message) {
        if (logservice != null)
            logservice.log(LogService.LOG_ERROR, message);
	}

	@Override
	public void debug(String message) {
        if (logservice != null)
            logservice.log(LogService.LOG_DEBUG, message);
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
					info("registering class " + cls + " as implementation for instances of type " + ccc);				
					KnowledgeManager.get().registerInstanceImplementationClass(ccc, cls);
				}
			}
		}
	}
	
	@Override
	public File getOntologiesLocation() {
		return ontoFolder;
	}

	
}
