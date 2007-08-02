/*
 * LoginResult.java
 *
 * Created on 15-jul-2007, 0:50:54
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
package ejb.session;

import java.io.Serializable;

/**
 * A class representing a data structure to inform the Client the result of the
 * login operation.
 * 
 * @author Francisco Morero Peyrona
 */
public class LoginResult implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private boolean bAccountValid;
    private boolean bPasswordValid;
    private String  sSessionId;
    
    /**
     * Package scope constructor
     */
    LoginResult()
    {
        this.bAccountValid  = false;
        this.bPasswordValid = false;
        this.sSessionId     = null;
    }
    
    public boolean isAccountValid()
    {
        return bAccountValid;
    }
    
    public boolean isPasswordValid()
    {
        return bPasswordValid;
    }
    
    public String getSessionId()
    {
        return sSessionId;
    }
    
    /**
     * Convenience method.
     */
    public boolean isLoginValid()
    {
        return isAccountValid() && isPasswordValid();
    }

    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    void setAccountValid( boolean bAccountValid )
    {
        this.bAccountValid = bAccountValid;
    }
    
    void setPasswordValid( boolean bPasswordValid )
    {
        this.bPasswordValid = bPasswordValid;
    }
    
    void setSessionId( String sSessionId )
    {
        this.sSessionId = sSessionId;
    }
}