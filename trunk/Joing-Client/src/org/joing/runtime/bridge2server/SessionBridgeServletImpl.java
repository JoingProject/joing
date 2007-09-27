/*
 * SessionBridgeServletImpl.java
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

import ejb.JoingServerException;
import ejb.session.LoginResult;

/**
 * Access the Server (EJBs) by using WebServices.
 * <p>
 * This is the best option when the system is behind a fierewall.
 *
 * @author Francisco Morero Peyrona
 */
public class SessionBridgeServletImpl
       extends BridgeServletBaseImpl
       implements SessionBridge
{
    /**
     * Creates a new instance of SessionBridgeServletImpl
     * 
     * Package scope: only Bridge2Server class can create instances of this class.
     */
    SessionBridgeServletImpl()
    {
    }
    
    public LoginResult login( String sAccount, String sPassword )
           throws JoingServerException
    {
        LoginResult result = null;
        
        Channel channel = new Channel( SESSION_LOGIN );
                channel.write( sAccount );
                channel.write( sPassword );
        result = (LoginResult) channel.read();
                channel.close();

        if( result != null && result.isLoginValid() )
            // Store Session ID to be used in all invocations to Server
            platform.getBridge().setSessionId( result.getSessionId() );
        
        return result;
    }
    
    public void logout()
           throws JoingServerException
    {
        Channel channel = new Channel( SESSION_LOGOUT );
                channel.write( platform.getBridge().getSessionId() );
                channel.close();
    }
}