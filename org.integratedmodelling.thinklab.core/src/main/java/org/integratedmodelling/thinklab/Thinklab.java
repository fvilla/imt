package org.integratedmodelling.thinklab;

import java.util.ArrayList;
import java.util.Collection;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabPluginException;
import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class Thinklab extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.core";
	
	public static Thinklab get() {
		return (Thinklab)(getPlugin(PLUGIN_ID));
	}
	
	@Override
	protected void doStart() throws Exception {
	}

	@Override
	protected void doStop() throws Exception {
	}
	
	public static String resolvePluginName(String name, boolean complain) throws ThinklabException {
		
		String ret = null;
		
		// look for exact match first
		for (Bundle b : get().context.getBundles() ) {
			if (b.getSymbolicName().equals(name)) {
				ret = b.getSymbolicName();
			}
		}
		
		if (ret == null) {
			
			/*
			 * automatically disambiguate partial word matches, which should not be found
			 */
			if (!name.startsWith("."))
				name = "." + name;
			
			for (Bundle b : get().context.getBundles() ) {
				if (b.getSymbolicName().startsWith("org.integratedmodelling") &&
						b.getSymbolicName().endsWith(name)) {
					if (ret != null) {
						ret = null;
						break;
					}
					ret = b.getSymbolicName();
				}
			}
		}
		
		if (ret == null && complain)
			throw new ThinklabPluginException("plugin name " + name + " unresolved or ambiguous");
		
		return ret;
	}

	public static Collection<Bundle> getThinklabPlugins() throws ThinklabException {
		
		ArrayList<Bundle> ret = new ArrayList<Bundle>();
		
			for (Bundle b : get().context.getBundles() ) {
				if (b.getSymbolicName().startsWith("org.integratedmodelling.")) {
					ret.add(b);
				}
			}
		
		return ret;
	}

	
	public static Bundle resolvePlugin(String name, boolean complain) throws ThinklabException {
	
		Bundle ret = null;
		
		String pid = resolvePluginName(name, complain);

		if (pid != null) {
			try {
				ret = get().context.installBundle(pid);
			} catch (BundleException e) {
				throw new ThinklabPluginException(e);
			}	
		}
			
		return ret;
	}
}
