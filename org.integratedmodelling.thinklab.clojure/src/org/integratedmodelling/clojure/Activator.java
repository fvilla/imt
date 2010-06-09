package org.integratedmodelling.clojure;

import org.integratedmodelling.thinklab.ThinklabActivator;

import clojure.lang.RT;

public class Activator extends ThinklabActivator {

	@Override
	protected void doStart() throws Exception {
		info("initializing Clojure runtime");
		RT.loadResourceScript("clj/thinklab.clj");			
		RT.loadResourceScript("clj/utils.clj");			
		RT.loadResourceScript("clj/knowledge.clj");			
		info("Clojure initialized successfully");
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
