/**
 * Clear.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of Thinklab.
 * 
 * Thinklab is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thinklab is distributed in the hope that it will be useful,
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
 * @author    Ferdinando Villa (fvilla@uvm.edu)
 * @author    Ioannis N. Athanasiadis (ioannis@athanasiadis.info)
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.clojure.commands;

import java.net.URL;

import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.clojure.REPL;
import org.integratedmodelling.clojure.interpreters.ClojureInterpreter;
import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabScriptException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.osgi.framework.Bundle;

/**
 * Run an application configured in plugin.xml, or even pass a list to run inline
 *  
 * @author Ferdinando Villa, Ecoinformatics Collaboratory, UVM
 * 
 */
@ThinklabCommand(name="clj")
public class Clojure implements ICommandHandler {

	public IValue execute(Command command, ISession session) throws ThinklabException {

		IValue ret = null;
		String arg = null;
		IThinklabPlugin plugin = ClojureActivator.get();
		ClassLoader cloader = ClojureActivator.get().getClassLoader();

// TODO see if anyone needs this stuff
//		if (command.hasOption("context")) {
//
//			String contextplugin = command.getOptionAsString("context");
//			Bundle plug = Thinklab.resolvePlugin(contextplugin, true);
//			plugin = ClojureActivator.getPlugin(plug.getSymbolicName());
//			cloader = plugin.getClassLoader();
//		}
//
//		
//		if (!command.hasArgument("resource")) {
//			
//			if (session.getInputStream() == null) {
//				/* not interactive: just ignore command */
//				ClojureActivator.get().warn("Clojure interpreter invoked by a non-interactive application");
//				return null;
//			}
//			
//		} else {
//
//			arg = command.getArgumentAsString("resource");
//		}

		try {
			
			if (arg == null) {
				
				REPL repl = new REPL();
				repl.setInput(session.getInputStream());
				repl.setOutput(session.getOutputStream());
				repl.setSession(session);
				repl.setClassloader(cloader);
				repl.run(null);
				
			} else {
				
				URL url = plugin.getResourceURL(arg);
				ClojureInterpreter intp = new ClojureInterpreter();
				intp.setInput(session.getInputStream());
				intp.setOutput(session.getOutputStream());
				ret = intp.eval(url);
			}
			
		} catch (Exception e) {
			throw new ThinklabScriptException(e);
		}
		
		return ret;
	}
}
