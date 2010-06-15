package org.integratedmodelling.aries.core;

import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.currency.CurrencyPlugin;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.geospace.gazetteers.SimpleGazetteer;
import org.integratedmodelling.knowledge.foundational.FoundationalKnowledgeActivator;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.modelling.visualization.VisualizationFactory;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.storage.IKBox;
import org.integratedmodelling.thinklab.kbox.KBoxManager;
import org.integratedmodelling.thinklab.transformations.TransformationFactory;
import org.integratedmodelling.time.TimePlugin;

public class ARIESCorePlugin extends ThinklabActivator {

	static public final String ARIES_KBOX_PROPERTY = "aries.kbox";
	
	IKBox workingKbox = null;
	
	public static final String PLUGIN_ID = 
		"org.integratedmodelling.aries.core";
	
	public static ARIESCorePlugin get() {
		return (ARIESCorePlugin) getPlugin(PLUGIN_ID);
	}

	@Override
	protected void doStart() throws Exception {
		Geospace.get().addGazetteer("aries-inline", new SimpleGazetteer(getProperties()));
		VisualizationFactory.get().loadColormapDefinitions(getProperties());
		TransformationFactory.get().loadTransformationMappings(getProperties());
	}

	@Override
	protected void doStop() throws Exception {
	}

	public IKBox getWorkingKBox() throws ThinklabException {

		if (workingKbox == null) {
			
			String kboxname = getProperties().getProperty(ARIES_KBOX_PROPERTY);
			if (kboxname != null)
				workingKbox = KBoxManager.get().requireGlobalKBox(kboxname);
			if (workingKbox /* still */ == null) {
				workingKbox = KBoxManager.get();
			}
		}
		return workingKbox;
	}

	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.ThinklabActivator#preStart()
	 */
	@Override
	protected void preStart() throws Exception {
		
		// force core plugins to load
		// TODO check we have them all
		// FOCK I wish I didn't need to do this
		ClojureActivator.get().getId();
		CoreScience.get().getId();
		Geospace.get().getId();
		TimePlugin.get().getId();
		ModellingPlugin.get().getId();
		CurrencyPlugin.get().getId();
		FoundationalKnowledgeActivator.get().getId();
	}
	
	
	

}
