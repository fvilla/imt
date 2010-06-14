package org.integratedmodelling.clojure;

import java.io.File;
import java.net.URL;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.utils.MiscUtilities;

import clojure.lang.Compiler;

public class ClojureActivator extends ThinklabActivator {

	public final static String PLUGIN_ID = "org.integratedmodelling.thinklab.clojure";
	private boolean runtimeInited = false;
	
	public static ClojureActivator get() {
		return (ClojureActivator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {

		if (!runtimeInited) {
			info("initializing Clojure runtime....");
			ClassLoader cls = null;
			try {
//				cls = swapClassloader();
				Compiler.loadFile(MiscUtilities.resolveUrlToFile(getResourceURL("clj/thinklab.clj").toString()).toString());
				Compiler.loadFile(MiscUtilities.resolveUrlToFile(getResourceURL("clj/utils.clj").toString()).toString());
				Compiler.loadFile(MiscUtilities.resolveUrlToFile(getResourceURL("clj/knowledge.clj").toString()).toString());
			} catch (NoClassDefFoundError e) {
				System.out.println("PORCO DIO: " + e);
				throw new ThinklabPluginException(e);
			} catch (Exception e) {
				System.out.println("PORCA MADONNA: " + e);
				throw new ThinklabPluginException(e);	
			} finally {
//				resetClassLoader(cls);
			}
			info("Clojure runtime initialized");
			runtimeInited = true;
		}
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
