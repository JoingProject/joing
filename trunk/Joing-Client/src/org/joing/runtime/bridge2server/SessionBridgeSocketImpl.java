/*
 * SessionBridgeDirectImpl.java
 *
 * Created on 18 de junio de 2007, 13:34
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
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

package org.joing.runtime.bridge2server;

import org.joing.common.clientAPI.runtime.SessionBridge;
import org.joing.common.dto.session.LoginResult;

// NEXT: Implementar esta clase
import org.joing.common.dto.session.SystemInfo;
import org.joing.common.exception.JoingServerSessionException;

/**
 * Access directrly to EJBs.
 * <p>
 * This is the best option when the system is running inside an Intranet or
 * there is not a firewall.
 *
 * @author Francisco Morero Peyrona
 */
public class SessionBridgeSocketImpl 
       extends BridgeSocketBaseImpl
       implements SessionBridge
{    
    /**
     * Creates a new instance of SessionBridgeDirectImpl
     * 
     * Package scope: only Bridge2Server class can create instances of this class.
     */
    SessionBridgeSocketImpl()
    {
        super();
    }

    public LoginResult login( String sAccount, String sPassword ) throws JoingServerSessionException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void logout()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean isValidPassword( String sPassword ) throws JoingServerSessionException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public SystemInfo getSystemInfo()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public String getSessionId()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}