package org.integratedmodelling.thinkcap.core;

import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapRuntimeException;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.ISessionManager;

public class ThinkcapSessionManager implements ISessionManager {

	@Override
	public ISession createNewSession() throws ThinklabException {
		return new ThinkcapSession();
	}

	@Override
	public ISession getCurrentSession() {
		throw new ThinkcapRuntimeException("internal error: no current session is available without servlet context");
	}

	@Override
	public void notifySessionDeletion(ISession session) {
	}

}
