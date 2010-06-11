package org.integratedmodelling.modelling.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.integratedmodelling.geospace.Geospace;
import org.integratedmodelling.multidimensional.MultidimensionalCursor;
import org.integratedmodelling.multidimensional.MultidimensionalCursor.StorageOrdering;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.command.Command;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.annotations.ThinklabCommand;
import org.integratedmodelling.thinklab.interfaces.applications.ISession;
import org.integratedmodelling.thinklab.interfaces.commands.ICommandHandler;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.integratedmodelling.time.literals.DurationValue;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.cs.AxisDirection;

import cern.colt.Arrays;

@ThinklabCommand(name = "mfuck", argumentDescriptions = "zio", argumentNames = "arg", argumentTypes = "thinklab-core:Text")
public class Fuck implements ICommandHandler {

	@Override
	public IValue execute(Command command, ISession session)
			throws ThinklabException {

		IConcept cc = KnowledgeManager.get().retrieveConcept(
				command.getArgumentAsString("arg"));

		System.out.println("Concept is " + cc);

		// MultidimensionalCursor cursor =
		// new MultidimensionalCursor(StorageOrdering.ROW_FIRST);
		// cursor.defineDimensions(2, 3);
		//		
		// int i = 0;
		// for (int t = 0; t < 2; t++)
		// for (int s = 0; s < 3; s++) {
		//				
		// System.out.println(
		// "cursor at " + i + " (" + t + "," + s + ") " +
		// " gives " + Arrays.toString(cursor.getElementIndexes(i)));
		//				
		// i++;
		// }

		return null;
	}

}