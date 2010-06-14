package org.integratedmodelling.thinkscape;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.integratedmodelling.clojure.ClojureActivator;
import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.modelling.ModelFactory;
import org.integratedmodelling.modelling.ModellingPlugin;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinkscape.builder.ThinkscapeNature;
import org.integratedmodelling.time.TimePlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ThinkScape extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.integratedmodelling.thinkscape";

	// The shared instance
	private static ThinkScape plugin;
	
	/**
	 * The constructor
	 */
	public ThinkScape() {
	}

	
	public static boolean isThinklabProject(IProject pfile) {
		try {
			return pfile.hasNature(ThinkscapeNature.NATURE_ID);
		} catch (CoreException e) {
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		setup();

	}

	private void setup() {
		
		// force core plugins to load
		ClojureActivator cj = ClojureActivator.get();
		IConcept c = CoreScience.Ranking();
		Geospace.get().ArealLocation();
		TimePlugin.DateTime();
		ModelFactory manager = ModellingPlugin.get().getModelManager();

		/*
		 * set up global listeners
		 */
		
		for (Model m : manager.getModels()) {
			System.out.println("here is a model: " + m);
		}
		
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ThinkScape getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
