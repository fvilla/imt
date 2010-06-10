package org.integratedmodelling.thinklab.shell;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.commandline.CommandActivator;

public class Activator extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.shell";
	
	public static Activator get() {
		return (Activator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		
		// how are you
		CommandActivator.get();
		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
