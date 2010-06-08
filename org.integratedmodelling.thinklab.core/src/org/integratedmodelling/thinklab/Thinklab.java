package org.integratedmodelling.thinklab;


public class Thinklab extends ThinklabActivator {

	static Thinklab _this;
	static KnowledgeManager _km;
	
	public static Thinklab get() {
		return (Thinklab)getSelf();
	}
	
	@Override
	protected void doStart() throws Exception {
		_km = new KnowledgeManager();
	}

	@Override
	protected void doStop() throws Exception {
	}

}
