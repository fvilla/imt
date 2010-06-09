package org.integratedmodelling.thinklab.shell;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.literals.BooleanValue;


public class Activator extends ThinklabActivator {

	private static final String GRAPHICAL_SHELL_PROPERTY = "commandline.graphical.shell";
	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.shell";
	
	public static Activator get() {
		return (Activator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		
		boolean isGraphical = BooleanValue.parseBoolean(
				getProperties().getProperty(GRAPHICAL_SHELL_PROPERTY, "true"));
		
		if (isGraphical) {
			GraphicalShell shell = new GraphicalShell();
			shell.startConsole();
		} else {
			Shell shell = new Shell();
			shell.startConsole();
		}
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
