/*
 * JoingServerSessionException.java
 * 
 * Created on 31-jul-2007, 9:18:31
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
 * @author Francisco Morero Peyrona
 */
public class JoingServerSessionException extends JoingServerException
{
    public final static String LOGIN_EXISTS = "Sorry, the combination of account (user name) and password,\n"+
                                              "already exists. Please try another one.";
    
    public JoingServerSessionException() 
    {
        super();
    }
    
    public JoingServerSessionException( String message )
    {
        super( message );
    }
    
    public JoingServerSessionException( String message, Throwable cause )
    {
        super( message, cause );
    }
}