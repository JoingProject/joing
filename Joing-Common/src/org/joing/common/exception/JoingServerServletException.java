/*
 * JavaServerServletException.java
 * 
 * Created on 01-ago-2007, 13:42:39
 * 
 * Author: Francisco Morero Peyrona.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.common.exception;

/**
 *
 * @author fmorero
 */
public class JoingServerServletException extends JoingServerException
{
    public JoingServerServletException()
    {
        super();
    }
    
    public JoingServerServletException( Class clazz, String message )
    {
        super( "Error in servlet ["+ clazz.getName() +"]\n"+ message );
    }
    
    public JoingServerServletException( Class clazz, Throwable cause )
    {
        super( "Error in servlet ["+ clazz.getName() +"]", cause );
    }
}