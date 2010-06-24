package org.integratedmodelling.opal;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OPALPlugin extends ThinklabActivator {

	public final static String PLUGIN_ID = "org.integratedmodelling.thinklab.opal";
	
	public static OPALPlugin get() {
		return (OPALPlugin) getPlugin(PLUGIN_ID);
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
