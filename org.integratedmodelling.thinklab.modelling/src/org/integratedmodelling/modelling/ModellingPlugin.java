package org.integratedmodelling.modelling;

import org.integratedmodelling.modelling.visualization.VisualizationFactory;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstance;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ModellingPlugin extends ThinklabActivator {


	private static final String USE_CACHE_PROPERTY = "modelling.use.cache";

	// this is set externally in a session to mean that we are annotating 
	// models, so concepts should be created for them when undefined instead
	// of complaining.
	public static final String ANNOTATION_UNDERWAY = "annotation.underway";

	public static String PLUGIN_ID = "org.integratedmodelling.thinklab.modelling";
	
	private ModelFactory manager = null;
	
	// TODO move cache to ModelFactory
	private ObservationCache cache = null;

	public static final String STATEFUL_MERGER_OBSERVATION = "modeltypes:MergerObservation";
	
	public static ModellingPlugin get() {
		return (ModellingPlugin) getPlugin(PLUGIN_ID);
	}
	

	@Override
	protected void doStart() throws Exception {

		boolean persistent = false;
		manager = new ModelFactory();
		if (getProperties().containsKey(USE_CACHE_PROPERTY) &&
			Boolean.parseBoolean(getProperties().getProperty(USE_CACHE_PROPERTY))) {
			persistent = true;
		}
		cache = new ObservationCache(getScratchPath(), persistent);
		
		/*
		 * add whatever defaults we have in the colormap chooser
		 */
		VisualizationFactory.get().loadColormapDefinitions(getProperties());

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public ModelFactory getModelManager() {
		return manager;
	}
	
	// TODO move cache to ModelFactory
	public ObservationCache getCache() {
		return cache;
	}



}
