/*
 * AppBridgeServletImpl.java
 *
 * Created on 18 de junio de 2007, 16:04
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

import ejb.app.Application;
import ejb.app.AppsByGroup;
import java.util.List;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class AppBridgeServletImpl
       extends BridgeServletBaseImpl
       implements AppBridge
{    
    /**
     * Creates a new instance of AppBridgeServletImpl
     * 
     * Package scope: only Runtime class can create instances of this class.
     */
    AppBridgeServletImpl()
    {
    }

    public List<AppsByGroup> getAvailableForUser()
    {
        List<AppsByGroup> apps = null;
        
        try
        {
            Channel channel = new Channel( APP_GET_AVAILABLES );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    apps = (List<AppsByGroup>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return apps;
    }
    
    public List<AppsByGroup> getNotInstalledForUser()
    {
        List<AppsByGroup> apps = null;
        
        try
        {
            Channel channel = new Channel( APP_GET_NOT_INSTALLED );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    apps = (List<AppsByGroup>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return apps;
    }
    
    public List<AppsByGroup> getInstalledForUser()
    {
        List<AppsByGroup> apps = null;
        
        try
        {
            Channel channel = new Channel( APP_GET_INSTALLED );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    apps = (List<AppsByGroup>) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return apps;
    }

    public boolean install( Application app )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( APP_INSTALL );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( app );
                    bSuccess = (Boolean) channel.read();
                    channel.close();            
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }
    
    public boolean uninstall( Application app )
    {
        boolean bSuccess = false;
        
        try
        {
            Channel channel = new Channel( APP_UNINSTALL );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( app );
                    bSuccess = (Boolean) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return bSuccess;
    }

    public Application getPreferredForType( String sFileExtension )
    {
        Application application = null;
        
        try
        {
            Channel channel = new Channel( APP_GET_PREFERRED );
                    channel.write( Bridge2Server.getInstance().getSessionId() );
                    channel.write( sFileExtension );
                    application = (Application) channel.read();
                    channel.close();
        }
        catch( Exception exc )
        {
            org.joing.runtime.Runtime.getRuntime().showException( exc, "Error communicating with the server" );
        }
        
        return application;
    }
}