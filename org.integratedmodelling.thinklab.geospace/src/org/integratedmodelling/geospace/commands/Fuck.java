package org.integratedmodelling.geospace.commands;

import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;

@ThinklabCommand(name="fuck")
public class Fuck implements ICommandHandler {

	@Override
	public IValue execute(Command command, 
						  ISession session)
			throws ThinklabException {
		
		ClassLoader clsl = null;
		
		try {
		
			clsl = Geospace.get().swapClassloader();
			Geospace.getCRSFromID("ESRI:102745");					

		} catch (Exception e) {
			throw new ThinklabException(e);
		} finally {
			Geospace.get().resetClassLoader(clsl);
		}
		return null;
	}

}
