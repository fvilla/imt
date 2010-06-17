package org.integratedmodelling.thinkcap;

import java.io.File;

import org.integratedmodelling.thinkcap.core.Thinkcap;
import org.integratedmodelling.thinkcap.core.ThinkcapSessionManager;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapException;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapRuntimeException;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

public class ThinkcapPlugin extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinkcap.core";
	
	private Server server;

	public static ThinkcapPlugin get() {
		return (ThinkcapPlugin)getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {

		/*
		 * tell thinklab to generate ThinkcapSessions
		 */
		KnowledgeManager.get().setSessionManager(new ThinkcapSessionManager());

		/*
		 * TODO setup web space
		 */

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 
	 * @param subdir
	 * @return
	 */
	public File getPluginWebSpace() {
		
		File outfile =
			new File(
					Thinkcap.get().getWebSpace() + File.separator + getId());

		return outfile;
	}
	
	/**
	 * 
	 * @param subdir
	 * @return
	 */
	public File getPluginWebSpace(String subdir) {
		
		File outfile =
			new File(
					Thinkcap.get().getWebSpace()+  File.separator + 
					getId() + File.separator + subdir);
		outfile.mkdirs();
		return outfile;
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	public File getPluginWebSpace(ISession session) {
		
		File outfile =
			new File(
					Thinkcap.get().getWebSpace()+ File.separator + 
					getId() + File.separator + session.getSessionWorkspace());
		outfile.mkdirs();
		return outfile;
	}

	/**
	 * 
	 * @param subdir
	 * @return
	 */
	public String getPluginWebSpaceUrl(String subdir) {
		return Thinkcap.get().getBaseUrl() + File.separator + 
			getId() + File.separator +
			subdir;
	}
	
	/**
	 * 
	 * @param subdir
	 * @return
	 */
	public String getPluginWebSpaceUrl() {
		return Thinkcap.get().getBaseUrl() + File.separator + 
			getId();
	}
	
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public String getPluginWebSpaceUrl(ISession session) {
		return Thinkcap.get().getBaseUrl() + File.separator + 
			getId() + File.separator +
			session.getSessionWorkspace();
	}
	
	/**
	 * Get the last part of the plugin id if it's in dot notation
	 * 
	 * @param pluginId
	 * @return
	 */
	public static String getPluginName(String pluginId) {
		String[] pids = pluginId.split("\\.");
		return pids[pids.length - 1];
	}

	public void stopServer() {

		if (server != null)
			try {
				info("stopping Jetty server");
				server.stop();
			} catch (Exception e) {
				throw new ThinkcapRuntimeException(e);
			} finally {
				server = null;
			}
	}

	public void startServer(String host, int port) throws Exception {
		
		info("starting Jetty server on " + host + ":" + port);
		
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


}
