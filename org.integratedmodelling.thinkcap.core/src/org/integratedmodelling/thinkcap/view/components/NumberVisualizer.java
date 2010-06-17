/**
 * NumberVisualizer.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of Thinkcap.
 * 
 * Thinkcap is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thinkcap is distributed in the hope that it will be useful,
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
 * @author    Ferdinando
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.thinkcap.view.components;

import org.integratedmodelling.thinkcap.interfaces.IVisualizationComponentConstructor;
import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.interfaces.knowledge.IInstance;
import org.integratedmodelling.thinklab.interfaces.knowledge.IProperty;
import org.integratedmodelling.thinklab.interfaces.literals.IValue;
import org.zkoss.zk.ui.Component;

public class NumberVisualizer extends LiteralVisualizer implements IVisualizationComponentConstructor {

	private static final long serialVersionUID = -7882278377506046859L;

	public NumberVisualizer(IProperty p) {
		super(p);
	}
	
	public NumberVisualizer() {
		super();
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Component createVisualizationComponent(IProperty property, String label, int indentLevel) throws ThinklabException {
		NumberVisualizer ret = new NumberVisualizer(property);
		ret.setWidth("100%");
		return ret;
	}

}
