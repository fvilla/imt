package org.integratedmodelling.thinklab.application;

import java.net.URL;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabResourceNotFoundException;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.ITask;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;

public class Application {

	ApplicationDescriptor appdesc = null;
	
	public Application(String id) throws ThinklabResourceNotFoundException {
		
		/*
		 * load settings from declared apps
		 */
		appdesc = ApplicationManager.get().requireApplicationDescriptor(id);
	}
	
	public IValue run() throws ThinklabException {
		return run((ISession)null);
	}
	
	public IValue run(ISession session) throws ThinklabException {
		
		IValue ret = null;
		ITask task = null;
		
			
		/* 
		 * must be a script
		 */
		task = new RunScript();
		((RunScript)task).setLanguage(appdesc.language);
		
		if (appdesc.code != null) 
			((RunScript)task).setCode(appdesc.code);
				
		for (URL url : appdesc.scripts)
			((RunScript)task).setCode(url);
		
		/*
		 * Create session as specified
		 */
		if (session == null) {

			try {
				session =
					(ISession) Class.forName(
								appdesc.sessionClass, 
								true, 
								appdesc.registeringPlugin.getClassLoader()).newInstance();
			} catch (Exception e) {
				throw new ThinklabResourceNotFoundException(
					"application: " + 
					appdesc.getId() + 
					": error creating session of class " + 
					appdesc.sessionClass);
			}
		}

		/*
		 * Run task and return 
		 */
		task.run(session, appdesc.registeringPlugin.getClassLoader());
		
		/*
		 * Find return value
		 */
		
		return ret;
	}
	
	/**
	 * Run the passed application and return its value.
	 * 
	 * @param application
	 * @return
	 * @throws ThinklabException 
	 */
	public static IValue run(String application) throws ThinklabException {
		
		return new Application(application).run();
		
	}
}
