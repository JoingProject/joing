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

import java.util.List;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;
import org.joing.kernel.api.bridge.UserBridge;
import org.joing.common.exception.JoingServerUserException;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class UserBridgeServletImpl
       extends BridgeServletBaseImpl
       implements UserBridge
{
    /**
     * 
     * Creates a new instance of UserBridgeServletImpl
     * 
     * Package scope: only Runtime class can create instances of this class.
     */
    UserBridgeServletImpl()
    {
    }
    
    public User getUser()
           throws JoingServerUserException
    {
        User user = null;
 
        Channel channel = new Channel( USER_GET_USER );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
        user = (User) channel.read();
                channel.close();

        return user;
    }
    
    public User updateUser( User user )
           throws JoingServerUserException
    {
        User user2Ret = null;
        
        Channel channel = new Channel( USER_UPDATE_USER );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( user );
        user2Ret = (User) channel.read();
                channel.close();

        return user2Ret;
    }
    
    public List<Local> getAvailableLocales()
           throws JoingServerUserException
    {
        List<Local> list = null;
        
        Channel channel = new Channel( USER_LOCALS );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
        list = (List<Local>) channel.read();
                channel.close();
        
        return list;
    }
}