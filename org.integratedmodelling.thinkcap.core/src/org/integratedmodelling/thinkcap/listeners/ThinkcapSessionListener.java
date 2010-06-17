package org.integratedmodelling.thinkcap.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.integratedmodelling.thinkcap.core.Thinkcap;
import org.integratedmodelling.thinkcap.core.ThinkcapApplication;
import org.integratedmodelling.thinkcap.core.ThinkcapSession;
import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabException;

public class ThinkcapSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {

		/*
		 * register session with Thinkcap
		 */
		try {
			Thinkcap.get().instrumentSession(sessionEvent.getSession());
		} catch (ThinklabException e) {
			throw new ThinkcapRuntimeException(e);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
		/*
		 * get the model - if any - and call persistence hookups.
		 */
		ThinkcapSession sess = 
			Thinkcap.getThinkcapSessionFromHttpSession(sessionEvent.getSession());		

		ThinkcapApplication app = sess.getApplication();
//		if (app != null)
//			app.notifyUserDisconnected(sess);

		if (sess.getUserModel() != null ) {
			/*
			 * TODO notify user model 
			 */
		}
	}

}
