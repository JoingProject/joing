/*
 * JoingVFSException.java
 * 
 * Created on 25-jul-2007, 16:40:02
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
public class JoingServerUserException extends JoingServerException
{
    public final static String NOT_ATTRIBUTES_OWNER = "It looks like you are trying to update another user's\n"+
                                                      "information: an user can change only its own attributes.";
    public final static String INVALID_ACCOUNT      = JoingServerUserException.getAccountRestrictions();
    public final static String INVALID_PASSWORD     = JoingServerUserException.getPasswordRestrictions();
    
    //------------------------------------------------------------------------//
    
    public JoingServerUserException()
    {
        super();
    }
    
    public JoingServerUserException( String message )
    {
        super( message );
    }
    
    public JoingServerUserException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    //------------------------------------------------------------------------//
   // NEXT: Hay que leer los tamaños max y min para account y password de algún sitio
    private static String getAccountRestrictions()
    {
        return "Invalid account (user name). It has to follow these rules:"+ 
               "\n   * Minimum length =  4"+
               "\n   * Maximum length = 32"+
               "\n   * Numbers and lowercase letters"+
               "\n   * Following characters: '.' '_'";
    }
    
    private static String getPasswordRestrictions()
    {
        return "Invalid password length, minimum = 6 and maximum = 32 characters.";
    }
}