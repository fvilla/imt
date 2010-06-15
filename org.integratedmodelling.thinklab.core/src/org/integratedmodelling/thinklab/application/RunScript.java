package org.integratedmodelling.thinklab.application;

import java.net.URL;
import java.util.ArrayList;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.extensions.Interpreter;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.ITask;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.integratedmodelling.thinklab.interpreter.InterpreterManager;

/**
 * A task that runs a script, specified either as a 
 * @author Ferdinando
 *
 */
public class RunScript implements ITask {

	private String code = null;
	private ArrayList<URL> codeUrl = new ArrayList<URL>();
	private String language = null;
	private IValue result = null;
	
	public void setCode(Object code) {
		if (code instanceof URL)
			this.codeUrl.add((URL) code);
		else 
			this.code = code.toString();
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	
	@Override
	public void run(ISession session, ClassLoader loader) throws ThinklabException {
		
		/*
		 * retrieve interpreter for language
		 */
		Interpreter intp = InterpreterManager.get().newInterpreter(language);
		
		intp.setSession(session);
		intp.setInput(session.getInputStream());
		intp.setOutput(session.getOutputStream());
		
		/*
		 * run whatever
		 */
		for (URL url : codeUrl) {
			intp.loadBindings(url, loader);
		}
		
		/*
		 * if there's any inline code, run it last
		 */
		if (code != null) {
			result = intp.eval(code);
		}

	}

	public IValue getResult() {
		return result;
	}
	
}
