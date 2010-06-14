package org.integratedmodelling.thinklab.shell;

import org.integratedmodelling.thinklab.ThinklabActivator;
import org.integratedmodelling.thinklab.commandline.CommandActivator;
import org.integratedmodelling.thinklab.exception.ThinklabException;

public class Activator extends ThinklabActivator {

	public static final String PLUGIN_ID = "org.integratedmodelling.thinklab.shell";
	
	class ShellThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				new GraphicalShell().startConsole();
			} catch (ThinklabException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static Activator get() {
		return (Activator) getPlugin(PLUGIN_ID);
	}
	
	@Override
	protected void doStart() throws Exception {
		CommandActivator.get();	

		Thread thread = new Thread("longTaskName")
		{
			 public void run() {
				 int i = 0;
				 while (true) {
					 try {
						Thread.sleep(3000);
						if (i == 0)
							new ShellThread().start();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 info("son qua " + i++);
				 }
			 }
		};
	
		thread.start();
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
