/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.kernel.runtime.bridge2server;

import org.joing.kernel.api.bridge.SessionBridge;
import org.joing.common.dto.session.LoginResult;
import org.joing.common.dto.session.SystemInfo;
import org.joing.common.exception.JoingServerSessionException;

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
    private static String sSessionId = null;
        
    /**
     * Creates a new instance of SessionBridgeServletImpl
     * 
     * Package scope: only Bridge2Server class can create instances of this class.
     */
    SessionBridgeServletImpl()
    {
    }
    
    @Override
    public LoginResult login( String sAccount, String sPassword )
           throws JoingServerSessionException
    {
        LoginResult result = null;
        
        Channel channel = new Channel( SESSION_LOGIN );
                channel.write( sAccount );
                channel.write( sPassword );
        result = (LoginResult) channel.read();
                channel.close();

        if( result != null && result.isLoginValid() )
            // Store Session ID to be used in all invocations to Server
            sSessionId = result.getSessionId();
        
        return result;
    }
    
    @Override
    public void logout()
           throws JoingServerSessionException
    {
        if (sSessionId != null)
        {
            Channel channel = new Channel( SESSION_LOGOUT );
                    channel.write( sSessionId );
                    channel.read();    // NEXT: I do not know why, but if I do not read something from Servlet, the Servlet is not invoked
                    channel.close();
        }
    }
    
    @Override
    public boolean isValidPassword( String sPassword )
            throws JoingServerSessionException
    {
        boolean bValid = false;
        
        Channel channel = new Channel( SESSION_IS_VALID_PASSWORD );
                channel.write( sSessionId );
                channel.write( sPassword );
        bValid = (Boolean) channel.read();
                channel.close();
                
        return bValid;
    }
    
    @Override
    public SystemInfo getSystemInfo()
           throws JoingServerSessionException
    {
        SystemInfo result = null;
        
        Channel channel = new Channel( SESSION_GET_SYSTEM_INFO );
        result = (SystemInfo) channel.read();
                channel.close();
                
        return result;
    }
    
    @Override
    public String getSessionId()
    {
        return sSessionId;
    }
}