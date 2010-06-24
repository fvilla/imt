package org.integratedmodelling.thinklab.plugin;

import org.integratedmodelling.thinklab.interfaces.IThinklabPlugin;

public interface IPluginLifecycleListener {

	public abstract void onPluginLoaded(IThinklabPlugin plugin);

	public abstract void onPluginUnloaded(IThinklabPlugin plugin);

	public abstract void prePluginLoaded(IThinklabPlugin thinklabPlugin);

	public abstract void prePluginUnloaded(IThinklabPlugin thinklabPlugin);
}
