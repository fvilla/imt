package org.integratedmodelling.thinkcap.core;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapException;
//import org.integratedmodelling.thinkcap.view.LayoutDescriptor;
//import org.integratedmodelling.thinkcap.view.PortletDescriptor;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.utils.MiscUtilities;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;


/*
 * A global register for anything that the thinkcap plugins send our way.
 */
public class Thinkcap {

	public static final String THINKCAP_SESSION_PROPERTY = "thinkcap.session.thinkcapsession";
	public static Thinkcap _this;
	
	private HashMap<String, ThinkcapApplication> applications =
		new HashMap<String, ThinkcapApplication>();

	private File serverWebSpace = null;
	private String baseUrl = "http://127.0.0.1:8080";
	
	public static Thinkcap get() {
		
		if (_this == null)
			_this = new Thinkcap();
		return _this;
	}
	
	void setWebSpace(File ws) {
		serverWebSpace = ws;
	}
	
	void setBaseUrl(String s) {
		baseUrl = s;
	}

	public void registerThinkcapApplication(ThinkcapApplication app) {
		applications.put(app.getId(), app);
	}
	
	public File getWebSpace() {
		return serverWebSpace;
	}
		
	public ThinkcapApplication getApplication(String appname) {
		return applications.get(appname);
	}
	
	public Collection<ThinkcapApplication> getApplications() {
		/*
		 * TODO these would be nice sorted, as they're mostly used for display
		 */
		return applications.values();
	}
	
	public ThinkcapSession instrumentSession(HttpSession session) throws ThinklabException {
		
		ThinkcapSession tlsession = (ThinkcapSession) KnowledgeManager.get().requestNewSession();
		session.setAttribute(THINKCAP_SESSION_PROPERTY, tlsession);
		tlsession.initialize(session);
		
		/*
		 * TODO lookup authentication cookies and set session appropriately
		 */
		
		return tlsession;
	}
	
	/**
	 * Retrieve the thinkcap session associated with the passed http session
	 * @param session
	 * @return
	 */
	public static ThinkcapSession getThinkcapSessionFromHttpSession(HttpSession session) {
		
		return session == null ? 
				null :
				(ThinkcapSession)session.getAttribute(Thinkcap.THINKCAP_SESSION_PROPERTY);
	}

	
	/**
	 * To be used in XUL scripts and actions to retrieve everything about the current Thinkcap session
	 * @param zSession
	 * @return
	 */
	static public ThinkcapSession getThinkcapSession(Session zSession) {

		return getThinkcapSessionFromHttpSession((HttpSession) zSession.getNativeSession());
		
	}
	
	/**
	 * This should return the Thinkcap session handled by the calling thread.
	 * @return
	 */
	static public ThinkcapSession getCurrentThinkcapSession() {
		return getThinkcapSession(Sessions.getCurrent());
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public ThinkcapApplication publishApplication(String requestURI) throws ThinklabException {

		String appName = MiscUtilities.getURLBaseName(requestURI);
		ThinkcapApplication app = applications.get(appName);
		
		if (app == null) {
			throw new ThinkcapException("no application named " + appName + " has been registered");	
		}
		
		app.publish(getWebSpace());
		
		return app;
	}
	
	/**
	 * Ensure that all plugins declaring applications are activated before the server starts.
	 * 
	 * @throws PluginLifecycleException
	 */
	public void publishApplications() {
//		
//		ExtensionPoint toolExtPoint = 
//			pluginManager.getRegistry().
//				getExtensionPoint("org.integratedmodelling.thinkcap.core", "thinkcap-application");
//		
//		for (Iterator<Extension> it =  toolExtPoint.getConnectedExtensions().iterator(); it.hasNext(); ) {
//			Extension ext = it.next();
//			pluginManager.activatePlugin(ext.getDeclaringPluginDescriptor().getId());
//		}
	}

}
