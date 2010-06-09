package org.integratedmodelling.thinklab;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.integratedmodelling.thinklab.command.CommandDeclaration;
import org.integratedmodelling.thinklab.command.CommandManager;
import org.integratedmodelling.thinklab.configuration.LocalConfiguration;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabIOException;
import org.integratedmodelling.thinklab.exception.ThinklabInternalErrorException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.extensions.IKBoxHandler;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinklab.interfaces.annotations.DataTransformation;
import org.integratedmodelling.thinklab.interfaces.annotations.InstanceImplementation;
import org.integratedmodelling.thinklab.interfaces.annotations.KBoxHandler;
import org.integratedmodelling.thinklab.interfaces.annotations.ListingProvider;
import org.integratedmodelling.thinklab.interfaces.annotations.LiteralImplementation;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.commands.IListingProvider;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstanceImplementation;
import org.integratedmodelling.thinklab.kbox.KBoxManager;
import org.integratedmodelling.thinklab.literals.ParsedLiteralValue;
import org.integratedmodelling.thinklab.plugin.IPluginLifecycleListener;
import org.integratedmodelling.thinklab.transformations.ITransformation;
import org.integratedmodelling.thinklab.transformations.TransformationFactory;
import org.integratedmodelling.utils.CopyURL;
import org.integratedmodelling.utils.MiscUtilities;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
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
	
	/*
	 * intercepts the beginning of doStart()
	 */
	protected void preStart() throws Exception {	
	}
	
	public static String resolvePluginName(String name, boolean complain) throws ThinklabException {
		
		String ret = null;
		
		// look for exact match first
		for (Bundle b : _this.context.getBundles() ) {
			if (b.getSymbolicName().equals(name)) {
				ret = b.getSymbolicName();
			}
		}
		
		if (ret == null) {
			
			/*
			 * automatically disambiguate partial word matches, which should not be found
			 */
			if (!name.startsWith("."))
				name = "." + name;
			
			for (Bundle b : _this.context.getBundles() ) {
				if (b.getSymbolicName().startsWith("org.integratedmodelling") &&
						b.getSymbolicName().endsWith(name)) {
					if (ret != null) {
						ret = null;
						break;
					}
					ret = b.getSymbolicName();
				}
			}
		}
		
		if (ret == null && complain)
			throw new ThinklabPluginException("plugin name " + name + " unresolved or ambiguous");
		
		return ret;
	}
	
	public static IThinklabPlugin resolvePlugin(String name, boolean complain) throws ThinklabException {
	
		IThinklabPlugin ret = null;
		
		String pid = resolvePluginName(name, complain);

		if (pid != null) {
			try {
				Bundle bb = _this.context.installBundle(pid);
				bb.start();
			} catch (BundleException e) {
				throw new ThinklabPluginException(e);
			}
			
		}
			
		return ret;
	}

	
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
		
		preStart();
		
		loadOntologies();
		loadLiteralValidators();
		loadKboxHandlers();
//		loadKnowledgeImporters();
//		loadKnowledgeLoaders();
//		loadLanguageInterpreters();
		loadCommandHandlers();
		loadListingProviders();
		loadTransformations();
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
								ontoFolder);
				}
				found = true;

				File of = CopyURL.cache(f, ontoFolder, bundle.getLastModified());
				try {
					_km.getKnowledgeRepository().refreshOntology(
							of.toURI().toURL(), 
							MiscUtilities.getFileBaseName(of.toString()), 
							false);
				} catch (MalformedURLException e) {
					throw new ThinklabInternalErrorException(e);
				}
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
		Object ret = null;
		
		Class<?> cls = null;
		try {
			cls = _classloader.loadClass(clazz);
			ret = cls.newInstance();
			
		} catch (Exception e) {
			throw new ThinklabPluginException(e);
		}
		return ret;
	}

	public Object createInstance(Class<?> clazz) throws ThinklabPluginException {
		Object ret = null;
		try {
			ret = clazz.newInstance();
		} catch (Exception e) {
			throw new ThinklabPluginException(e);
		}
		return ret;
	}

	@Override
	public ClassLoader getClassLoader() {
		return _classloader;
	}

	@Override
	public File getConfigPath() {
		return confFolder;
	}

	@Override
	public File getLoadPath() throws ThinklabException {
		return plugFolder;
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
		return dataFolder;
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

	protected void loadLiteralValidators() throws ThinklabException {
		
		String ipack = this.getClass().getPackage().getName() + ".literals";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(ParsedLiteralValue.class, ipack, getClassLoader())) {	
			
			String concept = null;
			String xsd = null;

			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			/*
			 * lookup implemented concept
			 */
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof LiteralImplementation) {
					concept = ((LiteralImplementation)annotation).concept();
					xsd = ((LiteralImplementation)annotation).xsd();
				}
			}
			
			if (concept != null) {
				
				info("registering class " + cls + " as implementation for literals of type " + concept);
				
				KnowledgeManager.get().registerLiteralImplementationClass(concept, cls);
				if (!xsd.equals(""))
					
					info("registering XSD type mapping: " + xsd + " -> " + concept);
					KnowledgeManager.get().registerXSDTypeMapping(xsd, concept);
			}
			
		}

	}

	protected void loadCommandHandlers() throws ThinklabException {
		
		String ipack = this.getClass().getPackage().getName() + ".commands";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(ICommandHandler.class, ipack, getClassLoader())) {	
			
			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			/*
			 * lookup implemented concept
			 */
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof ThinklabCommand) {
					
					String name = ((ThinklabCommand)annotation).name();
					String description = ((ThinklabCommand)annotation).description();
					
					CommandDeclaration declaration = new CommandDeclaration(name, description);
					
					String retType = ((ThinklabCommand)annotation).returnType();
					
					if (!retType.equals(""))
						declaration.setReturnType(KnowledgeManager.get().requireConcept(retType));
					
					String[] aNames = ((ThinklabCommand)annotation).argumentNames().split(",");
					String[] aTypes = ((ThinklabCommand)annotation).argumentTypes().split(",");
					String[] aDesc =  ((ThinklabCommand)annotation).argumentDescriptions().split(",");

					for (int i = 0; i < aNames.length; i++) {
						if (!aNames[i].isEmpty())
							declaration.addMandatoryArgument(aNames[i], aDesc[i], aTypes[i]);
					}
					
					String[] oaNames = ((ThinklabCommand)annotation).optionalArgumentNames().split(",");
					String[] oaTypes = ((ThinklabCommand)annotation).optionalArgumentTypes().split(",");
					String[] oaDesc =  ((ThinklabCommand)annotation).optionalArgumentDescriptions().split(",");
					String[] oaDefs =  ((ThinklabCommand)annotation).optionalArgumentDefaultValues().split(",");

					for (int i = 0; i < oaNames.length; i++) {
						if (!oaNames[i].isEmpty())
							declaration.addOptionalArgument(oaNames[i], oaDesc[i], oaTypes[i], oaDefs[i]);				
					}

					String[] oNames = ((ThinklabCommand)annotation).optionNames().split(",");
					String[] olNames = ((ThinklabCommand)annotation).optionLongNames().split(",");
					String[] oaLabel = ((ThinklabCommand)annotation).optionArgumentLabels().split(",");
					String[] oTypes = ((ThinklabCommand)annotation).optionTypes().split(",");
					String[] oDesc = ((ThinklabCommand)annotation).optionDescriptions().split(",");

					for (int i = 0; i < oNames.length; i++) {
						if (!oNames[i].isEmpty())
								declaration.addOption(
										oNames[i],
										olNames[i], 
										(oaLabel[i].equals("") ? null : oaLabel[i]), 
										oDesc[i], 
										oTypes[i]);
					}
					
					try {
						CommandManager.get().registerCommand(declaration, (ICommandHandler) cls.newInstance());
					} catch (Exception e) {
						throw new ThinklabValidationException(e);
					}
					
					break;
				}
			}
		}

	}


	protected void loadListingProviders() throws ThinklabException {
		
		String ipack = this.getClass().getPackage().getName() + ".commands";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(IListingProvider.class, ipack, getClassLoader())) {	
			
			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof ListingProvider) {
					
					String name = ((ListingProvider)annotation).label();
					String sname = ((ListingProvider)annotation).itemlabel();
					try {
						CommandManager.get().registerListingProvider(name, sname, (IListingProvider) cls.newInstance());
					} catch (Exception e) {
						throw new ThinklabValidationException(e);
					}
					
					break;
				}
			}
		}

	}

	protected void loadTransformations() throws ThinklabException {
		
		String ipack = this.getClass().getPackage().getName() + ".transformations";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(ITransformation.class, ipack, getClassLoader())) {	
			
			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof DataTransformation) {
					
					String name = ((DataTransformation)annotation).id();
					try {
						TransformationFactory.get().registerTransformation(name, (ITransformation)cls.newInstance());
					} catch (Exception e) {
						throw new ThinklabValidationException(e);
					}
					
					break;
				}
			}
		}

	}

	protected void loadKboxHandlers() throws ThinklabException {
		
		String ipack = this.getClass().getPackage().getName() + ".kbox";
		
		for (Class<?> cls : MiscUtilities.findSubclasses(IKBoxHandler.class, ipack, getClassLoader())) {	
			
			/*
			 * lookup annotation, ensure we can use the class
			 */
			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;
			
			for (Annotation annotation : cls.getAnnotations()) {
				if (annotation instanceof KBoxHandler) {
					
					String[] format = ((KBoxHandler)annotation).protocol().split(",");
					for (String f : format) {
						KBoxManager.get().registerKBoxProtocol(
							f, 
							(IKBoxHandler)createInstance(cls));
					}
					
					break;
				}
			}
		}

	}
}
