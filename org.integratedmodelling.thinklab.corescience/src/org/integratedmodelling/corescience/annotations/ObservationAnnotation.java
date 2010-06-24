package org.integratedmodelling.corescience.annotations;

import java.util.ArrayList;
import java.util.Collection;

import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.annotation.SemanticAnnotation;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;


public class ObservationAnnotation extends SemanticAnnotation {

	private static final long serialVersionUID = 2936928105568553678L;

	public static final String OBSERVABLE_PROPERTY = "observable";

	ArrayList<String> val = new ArrayList<String>();
	
	public ObservationAnnotation(String id, String provider) {
		super(id, provider);
	}

	public void setObservable(Object concept) {
		put(OBSERVABLE_PROPERTY, concept.toString());
	}
	
	public IConcept getObservable() {
		try {
			return get(OBSERVABLE_PROPERTY) == null ?
					null :
					KnowledgeManager.get().requireConcept(get(OBSERVABLE_PROPERTY).toString());
		} catch (ThinklabException e) {
			throw new ThinklabRuntimeException(e);
		}
	}

	public Collection<String> validateType() {
		ArrayList<String> ret = new ArrayList<String>();

		if (getType() == null) {
			ret.add("Observation concept is undefined");
		} else {
		}
		return ret;
	}

	public Collection<String> validateObservable() {
		ArrayList<String> ret = new ArrayList<String>();

		if (getProperty(OBSERVABLE_PROPERTY) == null) {
			ret.add("Observable concept is undefined");
		} else if (getType() != null) {
			if (getType().is(CoreScience.CLASSIFICATION)) {
				
				/*
				 * TODO must be a classification storage type of some kind
				 */
				
				/*
				 * TODO must have disjoint concrete subclasses
				 */
				
			} else if (getType().is(CoreScience.MEASUREMENT)) {
				
				/*
				 * TODO must be a physical property
				 */
				if (!getObservable().is(CoreScience.PHYSICAL_PROPERTY)) {
					ret.add("Measured observable is not a physical property.");
				}
				
			}

		}
		return ret;
	}

	public Collection<String> validateUnits() {
		ArrayList<String> ret = new ArrayList<String>();
		if (getType() != null && getType().is(CoreScience.MEASUREMENT)) {
			if (getProperty("units") == null) {
				
				ret.add("No measurement units are defined.");			

			} else {
				
				if (getObservable() != null && getObservable().is(CoreScience.EXTENSIVE_PHYSICAL_PROPERTY)) {

					/*
					 * TODO units must agree with extents
					 */
				}

			}
		}
		return ret;
	}

	public Collection<String> validateContexts() {
		ArrayList<String> ret = new ArrayList<String>();
		return ret;
	}

	
	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.annotation.SemanticAnnotation#getValidationMessages()
	 */
	@Override
	public Collection<String> getValidationMessages() {
		return val;
	}

	/* (non-Javadoc)
	 * @see org.integratedmodelling.thinklab.annotation.SemanticAnnotation#validate()
	 */
	@Override
	public int validate() {
		
		val.clear();
		val.addAll(validateType());
		val.addAll(validateObservable());
		val.addAll(validateUnits());
		val.addAll(validateContexts());
		
		return val.size();
	}

	
	
}
