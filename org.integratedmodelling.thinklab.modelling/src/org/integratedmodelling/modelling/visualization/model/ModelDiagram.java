package org.integratedmodelling.modelling.visualization.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.integratedmodelling.modelling.Model;
import org.integratedmodelling.modelling.interfaces.IModel;

/**
 * Helper class suitable to become a descriptor for a model diagram in a MVC paradigm. Should represent
 * a model namespace. Maintains a list of models and it can identify dependencies and root models as new models
 * are added.
 * 
 * Graphically enabled classes may be derived that implement layout or visualization. 
 * 
 * @author Ferdinando
 *
 */
public class ModelDiagram {

	// all models that were added
	ArrayList<Model> models = new ArrayList<Model>();
	
	// only the models that do not depend on other models.
	ArrayList<Model> rootModels = new ArrayList<Model>();
	
	public void addModel(Model m) {
		models.add(m);
		computeDependencies();
	}

	public List<Model> getRootModels() {
		return rootModels;
	}
	
	public List<Model> getModels() {
		return models;
	}
	
	private void computeDependencies() {

		HashSet<Model> dependents = new HashSet<Model>();
		
		for (Model m : models) {
			for (IModel c : m.getDefinitions()) {
				for (IModel d : c.getDependencies())
					if (d instanceof Model)
						dependents.add((Model)d);
			}
		}
		
		rootModels.clear();
		for (Model m : models) {
			if (!dependents.contains(m)) 
				rootModels.add(m);
		}
	}
	
}
