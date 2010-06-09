package org.integratedmodelling.thinklab.graph;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IKnowledgeSubject;
import org.integratedmodelling.thinklab.interfaces.knowledge.IProperty;
import org.integratedmodelling.thinklab.interfaces.knowledge.IRelationship;

public class ConceptMap extends KnowledgeGraph {

	private static final long serialVersionUID = -5150733549820114934L;

	public ConceptMap(IConcept root) throws ThinklabException {
		this.forceTreeGeometry = true;
		buildGraph(root);
	}

	@Override
	protected boolean followProperty(IConcept source, IConcept target,
			IProperty property) {
		// everything goes
		return true;
	}

	@Override
	protected boolean followRelationship(IKnowledgeSubject source,
			IRelationship relationship, IConcept type) {
		// TODO Auto-generated method stub
		return false;
	}


}
