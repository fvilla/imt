package org.integratedmodelling.thinklab.commandline.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.integratedmodelling.thinklab.application.ApplicationDescriptor;
import org.integratedmodelling.thinklab.application.ApplicationManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.annotations.ListingProvider;
import org.integratedmodelling.thinklab.interfaces.commands.IListingProvider;

@ListingProvider(label="tasks")
public class TaskLister implements IListingProvider {

	@Override
	public Collection<String> getListing() throws ThinklabException {
		
		ArrayList<String> ret = new ArrayList<String>();
		for (ApplicationDescriptor o : ApplicationManager.get().getApplicationDescriptors()) {
			ret.add(o.getId() + " \t" + o.getDescription());
		}
		Collections.sort(ret);
		return ret;
	}

	@Override
	public void listItem(String item, PrintStream out) throws ThinklabException {
		// TODO implement listing of single model
	}

}
