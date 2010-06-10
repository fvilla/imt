package org.integratedmodelling.thinklab.commandline;

import org.integratedmodelling.thinklab.ThinklabActivator;

public class CommandActivator extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.commands";
	
	public static CommandActivator get() {
		return (CommandActivator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
	}


}
