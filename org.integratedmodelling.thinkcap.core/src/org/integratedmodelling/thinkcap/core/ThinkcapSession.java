/**
 * ThinkcapSession.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of Thinkcap.
 * 
 * Thinkcap is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thinkcap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the software; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * ----------------------------------------------------------------------------------
 * 
 * @copyright 2008 www.integratedmodelling.org
 * @author    Ferdinando
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.thinkcap.core;

import javax.servlet.http.HttpSession;

import org.integratedmodelling.thinkcap.core.exceptions.ThinkcapException;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.owlapi.Session;

/**
 * 
 * 
 * @author Ferdinando
 *
 */
public class ThinkcapSession extends Session {

	public ThinkcapSession() throws ThinklabException {
		super();
	}

	@Override
	public String toString() {
		return "[" + httpSession.getId() + " owner: " + getUserModel() + "]";
	}
	
	HttpSession httpSession = null;
	private ThinkcapApplication application = null;
	
	public void initialize(HttpSession session) {
		httpSession = session;
	}
	
	HttpSession getHttpSession() {
		return httpSession;
	}

	public ThinkcapApplication getApplication() {
		return application;
	}
	
	public void setApplication(ThinkcapApplication app) {
		application  = app;
	}

	public void setModel(ThinkcapModel model) throws ThinkcapException {
		
		setUserModel(model);
		
		if (model != null) {
			model.initialize(this);
		}
	}

}
