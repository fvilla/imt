
package org.integratedmodelling.thinkcap;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.integratedmodelling.thinkcap.core.Thinkcap;
import org.integratedmodelling.thinkcap.core.ThinkcapApplication;
import org.integratedmodelling.thinkcap.core.ThinkcapModel;
import org.integratedmodelling.thinkcap.core.ThinkcapPlugin;
import org.integratedmodelling.thinkcap.core.ThinkcapSession;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

/**
 * Servlet implementation class ThinkcapServlet
 */
public class ThinkcapServlet extends DHtmlLayoutServlet implements Servlet {
       
	private static final long serialVersionUID = -5141079121652466048L;

	/**
	 * This listener should ensure proper cleanup and notification of application 
	 * end/timeout.
	 * 
	 * @author Ferdinando Villa
	 *
	 */
	class ApplicationCleanupListener implements HttpSessionBindingListener {

		private ThinkcapApplication application;
		private ThinkcapSession tksession;

		public ApplicationCleanupListener(ThinkcapSession tkSession,
				ThinkcapApplication app) {
			
			this.tksession = tkSession;
			this.application = app;
		}

		@Override
		public void valueBound(HttpSessionBindingEvent e) {
		}

		@Override
		public void valueUnbound(HttpSessionBindingEvent e) {

			/*
			 * TODO check event 
			 */
			
			/*
			 * TODO check webspace for session, spawn a cleanup thread if necessary
			 */
			
			/*
			 * TODO should enable TK-wide listeners for user management, logging etc.
			 */
		}
	}
	
	/**
     * @see DHtmlLayoutServlet#DHtmlLayoutServlet()
     */
    public ThinkcapServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {

		/*
		 * this is crucial to properly initialize ZK
		 */
		super.init(config);

	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return super.getServletConfig();
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return super.getServletInfo(); 
	}

    
    /**
     * Handle all requests that we need to handle; return true if we need to call
     * the handler in super after doing so.
     * 
     * TODO we should have a servlet parameter to decide if we want to handle commands
     * using URIs for cmds and parameters. We may even want to only enable specific 
     * commands, but then we must bring back the command stuff and use well-designed
     * value transport interfaces.
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException 
     * @throws ServletException 
     */
    private boolean catchThinkcapRequest(ServletRequest req, ServletResponse res) throws ServletException, IOException {
    	
    	if ( !( req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse))
    		return true;
    	
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse)res;
    	
		HttpSession session = ((HttpServletRequest)request).getSession(true);
		ThinkcapSession tkSession = 
			Thinkcap.getThinkcapSessionFromHttpSession(session);

		if (tkSession == null) {
			
			/*
			 * this should have been done by the session listener, so warn
			 */
			ThinkcapPlugin.get().warn(
					"internal: no thinkcap session in http session. Check session listener.");

			try {
				tkSession = Thinkcap.get().instrumentSession(session);
			} catch (ThinklabException e) {
				throw new ThinkcapRuntimeException(e);
			}
		}
		
		if (request.getRequestURI().endsWith(".app")) {
        	
    		/* switch to application */
    		ThinkcapApplication app;
    		
			try {
				
				app = Thinkcap.get().publishApplication(request.getRequestURI());
				
				ThinkcapPlugin.get().info(
						request.getRemoteAddr() +
						": publishing application: " +
						app.getId());
				
			} catch (ThinklabException e) {
				throw new ThinkcapRuntimeException(e);
			}; 	
    		
			
			
			/* 
			 * TODO if we have an active application, we may need to
			 * save state or warn - possibly according to preferences or
			 * configuration. THIS SHOULD BE ITS OWN checkCurrentApplication(app) function.
			 */
			if (tkSession.getApplication() != null) {
				
				// TODO - redirect to confirmation page, setting new application as link for
				// proceed to
			}
			
    		/* 
           	 * set application into session 
           	 */	
           	tkSession.setApplication(app);
//			tkSession.getApplication().notifyUserConnected(tkSession);
			
			session.setAttribute("bindings.listener",
                     new ApplicationCleanupListener(tkSession, app));

           	
           	/*
           	 * if application has an associated model, create it and restore any persistent state
           	 * based on authentication info.
           	 */
           	try {	
           		
           		ThinkcapModel model = app.createModelInstance();
           		model.initialize(tkSession);
           		tkSession.setModel(model);
           		
			} catch (Exception e) {
				throw new ThinkcapRuntimeException(e);
			}
           	
			response.sendRedirect(app.getLocalUrl());
    		return false;
    		
    	} else if (request.getRequestURI().contains(".app?")) {
    		
    		/*
    		 * TODO if there is no active app, redirect to .app; otherwise
    		 * ensure the app is the active one; if so let it go, otherwise
    		 * ask if the current app should be abandoned and the other one
    		 * started.
    		 */
    	}
    	
    	return true;
    }
	
	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

		/*
		 * check if we're servicing the entry point of an application or anything 
		 * we need to know.
		 */
		if (catchThinkcapRequest(request, response))
			super.service(request, response);
	}

}
