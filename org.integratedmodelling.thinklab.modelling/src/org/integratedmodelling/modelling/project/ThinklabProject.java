package org.integratedmodelling.modelling.project;

import org.osgi.framework.Bundle;

/**
 * A thinklab project is a user resource bundle that provides models, ontologies, annotations,
 * contexts/gazetteers, kboxes, and visualization properties in predefined folders. It should not 
 * be a thinklab bundle using the thinklab activator (or any activator), nor should it provide any 
 * binary code. This is intended to keep a clean separation between API development and modeling 
 * with thinklab.
 * 
 * ThinkScape (in development) is an Eclipse environment to manage thinklab projects.
 * 
 * The modelling plugin is notified when a thinklab-enabled bundle is started, and will load its
 * resources appropriately.
 * 
 * @author Ferdinando
 * @date June 2010
 */
public class ThinklabProject {

	private Bundle bundle;

	public ThinklabProject(Bundle b) {

		this.bundle = b;		
		initialize();
	}

	private void initialize() {

		/*
		 * read ontologies
		 */

		/*
		 * read models
		 */
		
	}
	
}
