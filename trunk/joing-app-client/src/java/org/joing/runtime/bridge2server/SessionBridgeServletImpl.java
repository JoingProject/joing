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
    
    public boolean login( String sAccount, String sPassword )
    {
        String sSessionId = null;
        
        try
        {
            Channel channel = new Channel( SESSION_LOGIN );
                    channel.write( sAccount );
                    channel.write( sPassword );
                    sSessionId = (String) channel.read();
                    channel.close();
            
            // Store Session ID to be used by all calls to Server
            Bridge2Server.getInstance().setSessionId( sSessionId );
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return sSessionId != null;
    }
    
    public void logout()
    {
        try
        {
            Channel channel = new Channel( SESSION_LOGOUT );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
    }
}