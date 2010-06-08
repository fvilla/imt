/**
 * LogicalConnector.java
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
package org.integratedmodelling.utils;

/**
 * <p>A logical connector, representing one of the possible four connectors.</p>
 * <p>A bit complicated as a wrapper for an int, but it solves several problems related to string conversions and
 * equality. Private constructors force use of parseLogicalConnector to obtain one of the four possible static
 * instances. Because only the static members are used, == can be used to check for equality, although equals()
 * can be also used with both integers, LogicalConnectors and Strings. toString() will work properly.</p>
 * @author villa
 */
public class LogicalConnector {

    static public final int _UNION = 0;
    static public final int _INTERSECTION = 1;
    static public final int _EXCLUSION  = 2;
    static public final int _DISJOINT_UNION = 3;

    static public LogicalConnector UNION = new LogicalConnector(_UNION);
    static public LogicalConnector INTERSECTION = new LogicalConnector(_INTERSECTION);
    static public LogicalConnector EXCLUSION = new LogicalConnector(_EXCLUSION);
    static public LogicalConnector DISJOINT_UNION = new LogicalConnector(_DISJOINT_UNION);
    
    /**
     * The value of a connector. Use this one in switch statements, or use equality 
     * with static connector members in if statements.
     */
    public int value;
    
    /**
     * Checks if string is a valid representation of a logical connector.
     * @param s a string
     * @return true if string represents a connector.
     */
    public static boolean isLogicalConnector(String s) {
        // TODO
        boolean ret = true;
        try {
            parseLogicalConnector(s);
        } catch (MalformedLogicalConnectorException e) {
            ret = false;
        }
        return ret;
    } 
    
    /**
     * Parses string into connector and returns result. Will never allocate new connectors, but only
     * return the static instance corresponding to the string. 
     * @param s A string representing a logical connector.
     * @return a LogicalConnector
     * @throws MalformedLogicalConnectorException if string is silly.
     */
    public static LogicalConnector parseLogicalConnector(String s) throws MalformedLogicalConnectorException {
 
        LogicalConnector value = null;
        
        s = s.trim().toLowerCase();
        
        // TODO support formal notation quantifiers too
        if (s.equals("and") || s.equals("intersection"))
            value = INTERSECTION;
        else if (s.equals("or") || s.equals("union"))
            value = UNION;
        else if (s.equals("not") || s.equals("exclusion"))
            value = EXCLUSION;
        else if (s.equals("xor") || s.equals("disjoint-union"))
            value = DISJOINT_UNION;
        else 
           throw new MalformedLogicalConnectorException(s + " is not a valid logical connector");
        
        return value;
    }
    
    private LogicalConnector(int i) {
        value = i;
    }

    /**
     * 
     * @param c
     * @return
     */
    public boolean equals(LogicalConnector c) {
        return c.value == value;
    }
    
    /**
     * 
     * @param c
     * @return
     */
    public boolean equals(int c) {
        return value == c;
    }

    /**
     * 
     * @param s
     * @return
     * @throws MalformedLogicalConnectorException
     */
    public boolean equals(String s) throws MalformedLogicalConnectorException {
        return value == parseLogicalConnector(s).value;
    }

    
    public String toString() {
        
        String ret = "";
        switch (value) {
        case _UNION: ret = "or"; break;
        case _INTERSECTION: ret = "and"; break;
        case _EXCLUSION: ret = "not"; break;
        case _DISJOINT_UNION: ret = "xor"; break;
        }
        
        return ret;
    }
}
