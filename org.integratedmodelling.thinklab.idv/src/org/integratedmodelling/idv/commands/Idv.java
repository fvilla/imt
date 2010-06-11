package org.integratedmodelling.idv.commands;

import java.util.ArrayList;

import org.integratedmodelling.idv.IDV;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;

@ThinklabCommand(
		name="idv",
		description="run the Integrated Data Viewer with arguments",
		optionalArgumentNames="p1,p2,p3,p4,p5,p6",
		optionalArgumentTypes="thinklab-core:Text,thinklab-core:Text,thinklab-core:Text,thinklab-core:Text,thinklab-core:Text,thinklab-core:Text",
		optionalArgumentDescriptions="p1,p2,p3,p4,p5,p6",
		optionalArgumentDefaultValues="_,_,_,_,_,_",
		optionArgumentLabels="p1,p2,p3,p4,p5,p6",
		optionNames="o",
		optionDescriptions="output file (when appropriate)",
		optionLongNames="output",
		optionTypes="thinklab-core:Text"
)
public class Idv implements ICommandHandler {

	@Override
	public IValue execute(Command command, ISession session)
			throws ThinklabException {

		// String outfile = command.hasOption("output") ? command.getOptionAsString("output") : null;

		ArrayList<String> args = new ArrayList<String>();
		for (int i = 1; i < 7; i++) {
			if (command.getArgumentAsString("p"+i).equals("_"))
				break;
			args.add(command.getArgumentAsString("p"+i));
		}
		
		IDV.run(args.toArray(new String[args.size()]));
		
		return null;
	}

}
