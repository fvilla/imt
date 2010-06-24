package org.integratedmodelling.thinklab.command;

import java.util.Collection;
import java.util.HashMap;

import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabMalformedCommandException;
import org.integratedmodelling.thinklab.exception.ThinklabNoKMException;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.applications.ISessionManager;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.commands.IListingProvider;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;

public class CommandManager {
	
	HashMap<String, IListingProvider> listingProviders = new HashMap<String, IListingProvider>();
	HashMap<String, IListingProvider> itemListingProviders = new HashMap<String, IListingProvider>();
	
	/**
	 * command declarations are kept in a hash, indexed by command ID
	 */
	HashMap<String, CommandDeclaration> commands = new HashMap<String, CommandDeclaration>();

	/**
	 * each command has an action associated, also kept in a hash indexed by
	 * command ID
	 */
	HashMap<String, ICommandHandler> actions = new HashMap<String, ICommandHandler>();

	/**
	 * Register a command for use in the Knowledge Manager. The modality of
	 * invocation and execution of commands depends on the particular
	 * IKnowledgeInterface installed.
	 * 
	 * @param command
	 *            the CommandDeclaration to register
	 * @param action
	 *            the Action executed in response to the command
	 * @throws ThinklabException
	 * @see CommandDeclaration
	 * @see ISessionManager
	 */
	public void registerCommand(CommandDeclaration command, ICommandHandler action)
			throws ThinklabException {

		// TODO throw exception if command is installed
		commands.put(command.ID, command);
		actions.put(command.ID, action);
	}

	public void registerListingProvider(String label, String itemlabel, IListingProvider provider) {
		listingProviders.put(label, provider);
		if (!itemlabel.equals(""))
			itemListingProviders.put(itemlabel, provider);
	}
	
	public IListingProvider getListingProvider(String label) {
		return listingProviders.get(label);
	}
	
	public IListingProvider getItemListingProvider(String label) {
		return itemListingProviders.get(label);
	}
	
	public CommandDeclaration getDeclarationForCommand(String tok) {
		return commands.get(tok);
	}
	
    
	public Collection<CommandDeclaration> getCommandDeclarations() {
		return commands.values();
	}

	public CommandDeclaration requireDeclarationForCommand(String tok)
			throws ThinklabMalformedCommandException {
		CommandDeclaration cd = commands.get(tok);
		if (cd == null)
			throw new ThinklabMalformedCommandException("unknown command "
					+ tok);
		return cd;
	}

	/**
	 * Check if a command with a particular name has been registered.
	 * 
	 * @param commandID
	 * @return
	 */
	public boolean hasCommand(String commandID) {
		return actions.get(commandID) != null;
	}

	/**
	 * Submit and execute the passed command. Command is assumed validated so no
	 * checking is done. Returns a Value as result value, answered by execute()
	 * called on the corresponding action.
	 * 
	 * @param cmd
	 *            the command
	 * @param session
	 *            the session t
	 * @return a literal containing a result value and the associated concept,
	 *         or null if the command is void.
	 * @throws ThinklabException
	 *             if anything happens in command execution
	 */
	public IValue submitCommand(Command cmd, ISession session)
			throws ThinklabException {

		/*
		 * happens at times with botched commands (e.g., strange eof from
		 * shutting down the VM)
		 */
		if (cmd == null || cmd.getDeclaration() == null)
			return null;

		ICommandHandler a = actions.get(cmd.getDeclaration().ID);
		return a.execute(cmd, session);

	}

	/**
	 * Get the only instance of the plugin registry.
	 * 
	 * @return the plugin registry
	 * @throws ThinklabNoKMException
	 *             if no knowledge manager was initialized.
	 */
	static public CommandManager get() throws ThinklabNoKMException {
		return KnowledgeManager.get().getCommandManager();
	}

}
