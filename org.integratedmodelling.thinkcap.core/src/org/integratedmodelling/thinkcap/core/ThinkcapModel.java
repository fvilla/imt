package org.integratedmodelling.thinkcap.core;


import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapException;
//import org.integratedmodelling.thinkcap.view.components.ThinkcapComponent;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.IUserModel;

/**
 * Helper class to build persistent MVC models for thinkcap. Any ThinkcapModel works with Portlets and portals in the view
 * package to help build clean MVC applications.
 * 
 * @author Ferdinando Villa
 *
 */
public abstract class ThinkcapModel implements IUserModel {

	ISession tlSession = null;
	
	public abstract void initialize(ThinkcapSession session) throws ThinkcapException;

	public abstract void restore(String authenticatedUser) throws ThinkcapException;	

	public abstract void persist(String authenticatedUser) throws ThinkcapException;

	@Override
	public void initialize(ISession session) {
		tlSession = session;
	}	
	
	public ISession getSession() {
		return tlSession;
	}

}
