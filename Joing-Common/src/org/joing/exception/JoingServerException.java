/*
 * JoingException.java
 *
 * Created on 25-jul-2007, 14:28:42
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
package org.joing.exception;

/**
 * Common root for all Joing Exceptions.
 * 
 * @author Francisco Morero Peyrona.
 */
public class JoingServerException extends RuntimeException
{
    public final static String ACCESS_DB  = "Error accessing database.";
    public final static String ACCESS_NFS = "Error accessing native file system.";
    
    private String sLocalizedMessage;
    
    //------------------------------------------------------------------------//
    
    public JoingServerException()
    {
        super();
    }
    
    public JoingServerException( String message )
    {
        super( message );
    }
    
    public JoingServerException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    @Override
    public String getLocalizedMessage()
    {
        return (sLocalizedMessage == null ? getMessage() : sLocalizedMessage);
    }
    
    public void setLocalizedMessage( String sLocalizedMessage )
    {
        this.sLocalizedMessage = sLocalizedMessage;
    }
    
    public boolean isThirdParty()
    {
        return getCause() != null;
    }
}