package org.integratedmodelling.thinklab.graph;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IKnowledgeSubject;
import org.integratedmodelling.thinklab.interfaces.knowledge.IProperty;
import org.integratedmodelling.thinklab.interfaces.knowledge.IRelationship;

public class InheritanceGraph extends KnowledgeGraph {

	private static final long serialVersionUID = -9056024014801829963L;
	
	public InheritanceGraph(IConcept root) throws ThinklabException {
		setRoot(root);
		buildGraph(root);
	}

	@Override
	protected boolean followRelationship(IKnowledgeSubject source,
			IRelationship relationship, IConcept target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean followProperty(IConcept source, IConcept target,
			IProperty property) {
		// let only isa relationships through
		return property == null;
	}

}
