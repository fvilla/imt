package org.integratedmodelling.knowledge.foundational;

import org.integratedmodelling.thinklab.ThinklabActivator;

public class FoundationalKnowledgeActivator extends ThinklabActivator {

	public final static String PLUGIN_ID = "org.integratedmodelling.knowledge.foundational";
	
	public static FoundationalKnowledgeActivator get() {
		return (FoundationalKnowledgeActivator) getPlugin(PLUGIN_ID);
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
