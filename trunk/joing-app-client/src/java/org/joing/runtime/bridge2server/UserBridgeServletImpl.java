/*
 * UserBridgeServletImpl.java
 *
 * Created on 18 de junio de 2007, 15:58
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

import ejb.user.Local;
import ejb.user.User;
import java.util.List;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class UserBridgeServletImpl
       extends BridgeServletBaseImpl
       implements UserBridge
{
    //------------------------------------------------------------------------//
        
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
    {
        User user = null;
        
        try
        {
            Channel channel = new Channel( USER_GET_USER );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    user = (User) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return user;
    }
    
    public void updateUser( User user )
    {
        try
        {
            Channel channel = new Channel( USER_UPDATE_USER );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( user );
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
    }
    
    public List<Local> getAvailableLocales()
    {
        List<Local> list = null;
        
        try
        {
            Channel channel = new Channel( USER_LOCALS );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    list = (List<Local>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return list;
    }   
}