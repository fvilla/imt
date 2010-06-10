package org.integratedmodelling.thinkscape.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.integratedmodelling.thinklab.shell.GraphicalShell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShellHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ShellHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		try {
			GraphicalShell shell = new GraphicalShell();
			shell.startConsole();
		} catch (Exception e) {
			throw new ExecutionException(e.getMessage());
		}
		return null;
	}
}