package org.integratedmodelling.modelling.interfaces;

import java.util.Collection;

import org.integratedmodelling.corescience.interfaces.IState;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.utils.image.ColorMap;

/**
 * An adapter that helps use and visualize an observation as a dataset with many
 * possible data and uniform context. Should be initializable from a "live" observation or from any kind of persistent 
 * storage. We may also want a factory that gives us a IDataset for any kind of
 * suitable input.
 * 
 * -------------------------------------------------------------------------------------------------------
 * TODO should have an initialize(IObservation) and a persist/restore(URL) method. Should also compile an index
 * (persisted) that can be queried for contexts, sizes - the ideal would be producing a IContext for the
 * dataset.
 * 
 * Making plots should be handled by visualization adapters that take a dataset and a set of options for
 * size, colormaps etc. Defaults should be handled by intelligent concept lookup.
 * -----------------------------------------------------------------------------------------------------------
 * 
 * @author Ferdinando Villa
 *
 */
public interface IDataset {

	public abstract boolean isSpatial();
	
	public abstract boolean isTemporal();
	
	public abstract int getStateCount();
	
	public abstract Collection<IConcept> getObservables();
	
	public abstract Collection<IConcept> getStatefulObservables();
	
	public abstract IState getState(IConcept observable);
	
	public abstract String makeContourPlot(
			IConcept observable, String fileOrNull, int x, int y, int ... flags);
	
	public abstract String makeSurfacePlot(
			IConcept observable, String fileOrNull, int x, int y, int ... flags) throws ThinklabException;

	/**
	 * Call with same parameter as makeSurfacePlot (except the file name) and it
	 * will return an uncertainty mask for the concept if any uncertainty data are
	 * in the corresponding state.
	 * 
	 * @param observable
	 * @param fileOrNull
	 * @param x
	 * @param y
	 * @param flags
	 * @return
	 * @throws ThinklabException
	 */
	public abstract String makeUncertaintyMask(
			IConcept observable, String fileOrNull, int x, int y, int ... flags) throws ThinklabException;

	public abstract String makeTimeSeriesPlot(
			IConcept observable, String fileOrNull, int x, int y, int ... flags);

	public abstract String makeHistogramPlot(
			IConcept observable, String fileOrNull, int x, int y, int ... flags);
	
	public abstract void dump(IConcept concept);
	
	public abstract void dumpAll();

	/**
	 * Used to select the appropriate colormap for the numeric states of an observable.
	 * The number of colors/levels to show can be computed as (maxIndex-minIndex)
	 * 
	 * @param observable
	 * @param nlevels the number of levels to be represented
	 * @return
	 * @throws ThinklabException 
	 */
	public abstract ColorMap chooseColormap(IConcept observable, int nlevels, boolean isCategorical) throws ThinklabException;

}
