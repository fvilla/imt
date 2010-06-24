/**
 * AlgorithmInterpreterFactory.java
 * ----------------------------------------------------------------------------------
 * 
 * Copyright (C) 2008 www.integratedmodelling.org
 * Created: Jan 17, 2008
 *
 * ----------------------------------------------------------------------------------
 * This file is part of Thinklab.
 * 
 * Thinklab is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thinklab is distributed in the hope that it will be useful,
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
 * @author    Ioannis N. Athanasiadis (ioannis@athanasiadis.info)
 * @date      Jan 17, 2008
 * @license   http://www.gnu.org/licenses/gpl.txt GNU General Public License v3
 * @link      http://www.integratedmodelling.org
 **/
package org.integratedmodelling.thinklab.interpreter;

import java.util.Hashtable;

import org.integratedmodelling.thinklab.exception.ThinklabException;
import org.integratedmodelling.thinklab.exception.ThinklabValidationException;
import org.integratedmodelling.thinklab.extensions.Interpreter;

public class InterpreterManager {

	private static InterpreterManager AIF = null;

	Hashtable<String, Class<?>> interpreterClass = new Hashtable<String, Class<?>>();


	public Interpreter newInterpreter(String language) throws ThinklabException {
		
		Class<?> iclass = interpreterClass.get(language);
		
		if (iclass == null)
			throw new ThinklabValidationException(
					"no interpreter registered for language " + language);
		
		
		Interpreter ret = null;
		
		try {
			ret = (Interpreter) iclass.newInstance();
		} catch (Exception e) {
			throw new ThinklabValidationException(e);
		}
		
		return ret;
	}
	
	public void registerInterpreter(String language, Class<?> interpreterClass) {
		this.interpreterClass.put(language, interpreterClass);			
	}
	

	public static InterpreterManager get() {

		if (AIF == null) {
			AIF = new InterpreterManager();
		}
		return AIF;
		
	}


}
