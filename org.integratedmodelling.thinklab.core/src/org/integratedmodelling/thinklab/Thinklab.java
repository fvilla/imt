package org.integratedmodelling.thinklab;

public class Thinklab extends ThinklabActivator {

	public static Thinklab get() {
		return (Thinklab)(ThinklabActivator.get());
	}
	
	@Override
	protected void doStart() throws Exception {
	}

	@Override
	protected void doStop() throws Exception {
	}
}
