/**
 * ThinklabInternalErrorException.java
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
package org.integratedmodelling.thinklab.exception;

/**
 * This one is thrown when things happen that can only depend on the software itself.
 * @author Ferdinando Villa
 */
public class ThinklabInternalErrorException extends ThinklabException {

	private static final long serialVersionUID = 2692983228215195642L;

	public ThinklabInternalErrorException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ThinklabInternalErrorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public ThinklabInternalErrorException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public ThinklabInternalErrorException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

}
