package org.integratedmodelling.clojure;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.utils.MiscUtilities;

import clojure.lang.RT;

public class Activator extends ThinklabActivator {

	public final static String PLUGIN_ID = "org.integratedmodelling.thinklab.clojure";
	
	public static Activator get() {
		return (Activator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		info("initializing Clojure runtime");
		RT.loadResourceScript(
				MiscUtilities.resolveUrlToFile(
						getResourceURL("clj/thinklab.clj").toString()).toString());			
		RT.loadResourceScript(
				MiscUtilities.resolveUrlToFile(
						getResourceURL("clj/utils.clj").toString()).toString());			
		RT.loadResourceScript(
				MiscUtilities.resolveUrlToFile(
						getResourceURL("clj/knowledge.clj").toString()).toString());			
		info("Clojure initialized successfully");
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
