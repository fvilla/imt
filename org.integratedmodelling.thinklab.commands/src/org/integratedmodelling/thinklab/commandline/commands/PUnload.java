package org.integratedmodelling.thinklab.commandline.commands;

import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Just deactivate the named plugin.
 * 
 * @author Ferdinando
 *
 */
@ThinklabCommand(
		name="punload", 
		argumentNames="plugin",
		argumentTypes="thinklab-core:Text", 
		argumentDescriptions="plugin name or unambiguous suffix")
public class PUnload implements ICommandHandler {

	@Override
	public IValue execute(Command command, ISession session) throws ThinklabException {

		String plugin = command.getArgumentAsString("plugin");		
		plugin = Thinklab.resolvePluginName(plugin, true);

		for (Bundle b : Thinklab.getThinklabPlugins()) {
			if (b.getSymbolicName().equals(plugin)) {
				try {
					b.stop();
				} catch (BundleException e) {
					throw new ThinklabPluginException(e);
				}
				break;
			}
		}		
		
		return null;
	}

}
