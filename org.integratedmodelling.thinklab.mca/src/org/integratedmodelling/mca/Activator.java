package org.integratedmodelling.mca;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.mca";
	
	public static Activator get() {
		return (Activator)getPlugin(PLUGIN_ID);
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
