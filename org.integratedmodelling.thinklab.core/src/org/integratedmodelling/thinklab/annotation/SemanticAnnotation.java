package org.integratedmodelling.thinklab.annotation;

import java.util.ArrayList;
import java.util.Collection;

import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabRuntimeException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;


/**
 * A semantic annotation is a specialized Properties object.
 * 
 * @author Ferdinando
 *
 */
public class SemanticAnnotation extends SemanticSource {
	
	private static final long serialVersionUID = 4178258864664558292L;
	static final String ANNOTATION_PROVIDER_ID = "provider_id";
	
	public static final String ID_PROPERTY = "id";
	public static final String TYPE_PROPERTY = "type";
	
	public SemanticAnnotation(String id, String serviceProvider) {
		super(id);
		setProperty(ANNOTATION_PROVIDER_ID, serviceProvider);
	}

	/**
	 * Return 0 if no validation problems exist, or the number of problems. getValidationMessages()
	 * should return as many messages as there are problems. The default returns one problem, that
	 * the annotation is undefined.
	 * 
	 * @return
	 */
	public int validate() {
		return getValidationMessages().size();
	}
	
	/**
	 * Return as many messages as there are validation problems. By default it returns
	 * undefined annotation. Should only be called just after calling validate().
	 * 
	 * @return
	 */
	public Collection<String> getValidationMessages() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("Observation is undefined.");
		return ret;
	}
	
	public void setType(Object concept) {
		put("type", concept.toString());
	}
	
	public IConcept getType() {
		try {
			return get("type") == null ?
					null :
					KnowledgeManager.get().requireConcept(get("type").toString());
		} catch (ThinklabException e) {
			throw new ThinklabRuntimeException(e);
		}
	}

	/**
	 * This should return a stored flag, without having to run "validate". Implementations of 
	 * validate should call setValid(true);
	 * @return
	 */
	public boolean isValid() {
		return get("valid") != null && get("valid").equals("true");
	}
	
	public void setValid(boolean b) {
		set("valid", b ? "true" : "false");
	}
}
