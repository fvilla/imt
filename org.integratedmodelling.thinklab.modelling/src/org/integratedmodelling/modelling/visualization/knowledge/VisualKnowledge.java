/**
 * VisualKnowledge.java
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
package org.integratedmodelling.modelling.visualization.knowledge;


public abstract class VisualKnowledge {
	
	/* quite idiotic */
	public static String checkUpperCamel(String ret) {
		
		if (ret.length() > 2 && Character.isUpperCase(ret.charAt(0)) && Character.isLowerCase(ret.charAt(1))) {
			ret = ret.replaceAll("([A-Z])", " $1");
		}
		
		return ret;
	}

	/* quite idiotic */
	public static String checkLowerCamel(String ret) {
		
		/* FIXME 
		 * check should be: starts with a lowercase and contains uppercase in 
		 * positions different from last.
		 */
		
		if (ret.length() > 2 && Character.isLowerCase(ret.charAt(0)) && Character.isLowerCase(ret.charAt(1))) {
			ret = ret.replaceAll("([A-Z])", " $1");
		}
		
		return ret;
	}

	public abstract String getDescription();

	public abstract String getLabel();

	
	
	
}
