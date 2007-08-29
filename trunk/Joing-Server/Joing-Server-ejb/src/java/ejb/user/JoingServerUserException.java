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
package ejb.user;

import ejb.vfs.*;
import ejb.JoingServerException;

/**
 *
 * @author fmorero
 */
public class JoingServerUserException extends JoingServerException
{
    final static String NOT_ATTRIBUTES_OWNER = "It looks like you are trying to update another user's\n"+
                                               "information: an user can change only its own attributes.";
    final static String INVALID_ACCOUNT      = UserManagerBean.getAccountRestrictions();
    final static String INVALID_PASSWORD     = UserManagerBean.getPasswordRestrictions();
    
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
}