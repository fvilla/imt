package org.integratedmodelling.thinklab.shell;

import org.integratedmodelling.thinklab.Thinklab;
import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.commandline.CommandActivator;

public class Activator extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.shell";

	public static Activator get() {
		return (Activator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		CommandActivator.get();	
		new GraphicalShell().startConsole();
	}

	@Override
	protected void doStop() throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.ThinklabActivator#preStart()
	 */
	@Override
	protected void preStart() throws Exception {
		Thinklab.get().getId();
		CommandActivator.get().getId();
	}
	

}
