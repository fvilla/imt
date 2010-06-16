/*
 * 
 */
package org.integratedmodelling.thinkcap.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapException;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapRuntimeException;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.plugin.ThinklabPlugin;
import org.java.plugin.PluginLifecycleException;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

public class ThinkcapCore extends ThinklabPlugin {

	private Server server = null;
	
	public static final String PLUGIN_ID = "org.integratedmodelling.thinkcap.core";
	
	public static ThinkcapCore get() {
		
		ThinkcapCore ret = null;
		try {
			ret = (ThinkcapCore) Thinkcap.get().getPluginManager().getPlugin(PLUGIN_ID);
		} catch (PluginLifecycleException e) {
			// screw it
		}
		return ret;
	}
	
	public Log logger() {
		return log;
	}
	

//	@Override
//	protected void doStop() throws Exception {
//		// nothing, the server is stopped by the main application (ThinkcapApp) calling
//		// stopServer explicitly.
//	}
	
	public void stopServer() {

		if (server != null)
			try {
				log.info("stopping Jetty server");
				server.stop();
			} catch (Exception e) {
				throw new ThinkcapRuntimeException(e);
			} finally {
				server = null;
			}
	}

	public void startServer(String host, int port) throws Exception {
		
		log.info("starting Jetty server on " + host + ":" + port);
		
		/*
		 * do all preparatory chores before starting the server
		 */
		Thinkcap.get().setBaseUrl("http://" + host + ":" + port);
		
		if (server != null)
			throw new ThinkcapException("thinkcap server is already active");
		
		server = new Server(); 
		SelectChannelConnector connector = new SelectChannelConnector(); 
		connector.setHost(host);
		connector.setPort(port); 
		server.setConnectors (new Connector[]{connector}); 
		
		WebAppContext wah = new WebAppContext(); 
		wah.setContextPath("/"); 
		wah.setWar(Thinkcap.get().getWebSpace().toString()); 
		
		ClassLoader cl = this.getClass().getClassLoader(); 
		WebAppClassLoader wacl = new WebAppClassLoader(cl, wah); 
		wah.setClassLoader(wacl); 
		 
		server.addHandler(wah); 
		server.setStopAtShutdown(true);

		server.start();
		server.join();
	}

	@Override
	protected void load(KnowledgeManager km) throws ThinklabException {
		
		// TODO Auto-generated method stub
		/*
		 * we don't go very far without this, so do it anyway
		 */
		requirePlugin("org.integratedmodelling.thinklab.core");
		
		/*
		 * tell thinklab to generate ThinkcapSessions
		 */
		KnowledgeManager.get().setSessionManager(new ThinkcapSessionManager());
		
		Thinkcap.get().setPluginManager(getManager());
		
		/*
		 * recover path of webapp in plugin dir. FIXME there must be a better way, and
		 * if not, at least put this in a method.
		 */
		String lf = getDescriptor().getLocation().getFile();
		try {
			lf = URLDecoder.decode(lf.substring(0, lf.lastIndexOf("/")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ThinklabValidationException(e);
		}
		
		Thinkcap.get().setWebSpace(new File(lf + "/webapp"));

	}

	@Override
	protected void unload() throws ThinklabException {
		// TODO Auto-generated method stub
		
	}
}
