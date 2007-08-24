/*
 * JoingServerAppException.java
 * 
 * Created on 31-jul-2007, 10:05:32
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
package ejb.app;

import ejb.JoingServerException;

/**
 *
 * @author fmorero
 */
public class JoingServerAppException extends JoingServerException
{
    static final String INVALID_OWNER  = "The account does not has priviledges to execute this application.";
    static final String APP_NOT_EXISTS = "Requested application does not exists.";
    
    public JoingServerAppException() 
    {
        super();
    }
    
    public JoingServerAppException( String message )
    {
        super( message );
    }
    
    public JoingServerAppException( String message, Throwable cause )
    {
        super( message, cause );
    }
}