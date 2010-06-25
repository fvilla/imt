package org.integratedmodelling.modelling;

import java.util.HashSet;

import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.modelling.visualization.VisualizationFactory;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.time.TimePlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class ModellingPlugin extends ThinklabActivator {


	private static final String USE_CACHE_PROPERTY = "modelling.use.cache";

	// this is set externally in a session to mean that we are annotating 
	// models, so concepts should be created for them when undefined instead
	// of complaining.
	public static final String ANNOTATION_UNDERWAY = "annotation.underway";

	public static String PLUGIN_ID = "org.integratedmodelling.thinklab.modelling";
	
	private ModelFactory manager = null;
	private HashSet<String> modelPluginIds = new HashSet<String>();
	
	// TODO move cache to ModelFactory
	private ObservationCache cache = null;

	public static final String STATEFUL_MERGER_OBSERVATION = "modeltypes:MergerObservation";

	// PROPERTY IDs for thinklab.metadata in model plugins
	public static final String SEMANTIC_ANNOTATION_SOURCES_PROPERTY = "thinklab.annotation.sources";
	
	public static ModellingPlugin get() {
		return (ModellingPlugin) getPlugin(PLUGIN_ID);
	}
	
	public class ModelPluginListener implements BundleListener {

		@Override
		public void bundleChanged(BundleEvent event) {
			
			if (event.getType() != BundleEvent.STARTED)
				return;
			
			Bundle b = event.getBundle();
			if (!modelPluginIds.contains(b.getSymbolicName())) {
				if (isModelPlugin(b)) {
					ModellingPlugin.get().info("reading Thinklab project: " + b.getSymbolicName());
					loadProject(b);
					modelPluginIds.add(b.getSymbolicName());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.ThinklabActivator#preStart()
	 */
	@Override
	@SuppressWarnings("unused")
	protected void preStart() throws Exception {
		// force core plugins to load
		ClojureActivator cj = ClojureActivator.get();
		IConcept c = CoreScience.Ranking();
		IConcept o = Geospace.RasterObservationSpace();	
		IConcept t = TimePlugin.DateTime();
	}

	/**
	 * Check if a plugin is a Thinklab user plugin, which provides models, ontologies
	 * and semantic annotations.
	 * 
	 * @param b
	 * @return
	 */
	public boolean isModelPlugin(Bundle b) {
		return b.findEntries("THINKLAB-INF", "thinklab.properties", false) != null;
	}

	/**
	 * load all modeling resources from a plugin that has the THINKLAB-INF extensions.
	 * @param b
	 */
	private void loadProject(Bundle b) {
	}

	
	@Override
	protected void doStart() throws Exception {

		/**
		 * Install a listener to load models and annotations from all plugins that
		 * provide them.
		 */
		this.bundle.getBundleContext().addBundleListener(new ModelPluginListener());
		
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
