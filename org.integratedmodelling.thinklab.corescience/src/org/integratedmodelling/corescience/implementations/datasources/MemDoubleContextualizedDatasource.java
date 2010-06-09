/**
 * DefaultState.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of ThinklabCoreSciencePlugin.
 * 
 * ThinklabCoreSciencePlugin is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ThinklabCoreSciencePlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the software; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * ----------------------------------------------------------------------------------
 * 
 * @copyright 2008 www.integratedmodelling.org
 * @author    Ferdinando Villa (fvilla@uvm.edu)
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.corescience.implementations.datasources;

import java.io.InputStream;
import java.io.OutputStream;

import org.integratedmodelling.corescience.CoreScience;
import org.integratedmodelling.corescience.context.ObservationContext;
import org.integratedmodelling.corescience.interfaces.IDataSource;
import org.integratedmodelling.corescience.interfaces.IObservationContext;
import org.integratedmodelling.corescience.interfaces.IState;
import org.integratedmodelling.corescience.interfaces.internal.IDatasourceTransformation;
import org.integratedmodelling.corescience.metadata.Metadata;
import org.integratedmodelling.thinklab.KnowledgeManager;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabValueConversionException;
import org.integratedmodelling.thinklab.interfaces.annotations.PersistentObject;
import org.integratedmodelling.thinklab.interfaces.knowledge.IConcept;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstance;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstanceImplementation;
import org.integratedmodelling.thinklab.interfaces.storage.IPersistentObject;
import org.integratedmodelling.utils.InputSerializer;
import org.integratedmodelling.utils.OutputSerializer;
import org.integratedmodelling.utils.Polylist;

@PersistentObject(extension="dst")
public class MemDoubleContextualizedDatasource 
 	implements IState, IInstanceImplementation, IPersistentObject {

	private static final long serialVersionUID = -6567783706189229920L;
	private IConcept _type;
	protected double[] data = null;
	protected Metadata metadata = new Metadata();
	private ObservationContext context;
	
	public MemDoubleContextualizedDatasource() {
		// only to be used by the serializer
	}
	
	public IConcept getType() {
		return _type;
	}
	
	public MemDoubleContextualizedDatasource(IConcept type, double[] data, ObservationContext context) {
		_type = type;
		this.data = data;
		metadata.put(Metadata.CONTINUOUS, Boolean.TRUE);
		this.context = context;
	}
	
	public MemDoubleContextualizedDatasource(IConcept type, double[][] d, ObservationContext context) {
		_type = type;
		this.context = context;
		this.data = new double[d.length*d[0].length];
		int k = 0;
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				data[k++] = d[i][j];
			}
		}
	}
	
	public MemDoubleContextualizedDatasource(IConcept type, int size, ObservationContext context) {
		_type = type;
		data = new double[size];
		this.context = context;
	}
	
	@Override
	public Object getInitialValue() {
		return null;
	}

	@Override
	public Object getValue(int index, Object[] parameters) {
		return data[index];
	}

	@Override
	public Object getDataAt(int offset) {
		return (offset >= 0 && offset < data.length) ? data[offset] : null;
	}
	
	@Override
	public IConcept getValueType() {
		return _type;
	}

	@Override
	public void addValue(int idx, Object o) {
		double d = o == null ? Double.NaN : ((Number)o).doubleValue();
		data[idx] = d;
	}

	@Override
	public Polylist conceptualize() throws ThinklabException {

		return Polylist.list(
				CoreScience.CONTEXTUALIZED_DATASOURCE,
				Polylist.list("@", this));
	}

	@Override
	public void initialize(IInstance i) throws ThinklabException {
	}

	@Override
	public void validate(IInstance i) throws ThinklabException {
	}
	
	public String toString() {
		return "MD[" + _type + ": " /*+ Arrays.toString(data)*/ + "]";
	}

	@Override
	public Object getRawData() {
		return data;
	}

	@Override
	public double[] getDataAsDoubles() throws ThinklabValueConversionException {
		return data;
	}
	
	@Override
	public Metadata getMetadata() {
		return metadata;
	}
	
	@Override
	public int getTotalSize() {
		return data.length;
	}

	@Override
	public IDataSource<?> transform(IDatasourceTransformation transformation)
			throws ThinklabException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void postProcess(IObservationContext context)
			throws ThinklabException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preProcess(IObservationContext context)
			throws ThinklabException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPersistentObject deserialize(InputStream fop) throws ThinklabException {
		InputSerializer in = new InputSerializer(fop);
		_type = KnowledgeManager.get().requireConcept(in.readString());
		data = in.readDoubles();
		metadata = Metadata.deserializeMetadata(fop);
		return this;
	}

	@Override
	public boolean serialize(OutputStream fop) throws ThinklabException {
		
		OutputSerializer out = new OutputSerializer(fop);
		out.writeString(_type.toString());
		out.writeDoubles(data);
		Metadata.serializeMetadata(metadata, fop);
		return true;
	}

	@Override
	public IConcept getObservableClass() {
		return _type;
	}

	@Override
	public ObservationContext getObservationContext() {
		return this.context;
	}

	@Override
	public double getDoubleValue(int index)
			throws ThinklabValueConversionException {
		return data[index];
	}


}
