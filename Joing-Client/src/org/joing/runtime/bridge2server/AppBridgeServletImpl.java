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

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.exception.JoingServerException;

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
    
    public List<AppGroup> getAvailableForUser( int nEnvironment, int nGroup )
           throws JoingServerException
    {
        List<AppGroup> apps = null;
        
        Channel channel = new Channel( APP_GET_AVAILABLES );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( nEnvironment );
                channel.write( nGroup );
        apps = (List<AppGroup>) channel.read();
                channel.close();
        
        return apps;
    }
    
    public List<AppGroup> getNotInstalledForUser( int nEnvironment, int nGroup )
           throws JoingServerException
    {
        List<AppGroup> apps = null;
        
        Channel channel = new Channel( APP_GET_NOT_INSTALLED );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( nEnvironment );
                channel.write( nGroup );
        apps = (List<AppGroup>) channel.read();
                channel.close();
                
        return apps;
    }
    
    public List<AppGroup> getInstalledForUser( int nEnvironment, int nGroup )
           throws JoingServerException
    {
        List<AppGroup> apps = null;
        
        Channel channel = new Channel( APP_GET_INSTALLED );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( nEnvironment );
                channel.write( nGroup );
        apps = (List<AppGroup>) channel.read();
                channel.close();
        
        return apps;
    }

    public boolean install( AppDescriptor app )
           throws JoingServerException
    {
        boolean bSuccess = false;
        
        Channel channel = new Channel( APP_INSTALL );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( app );
        bSuccess = (Boolean) channel.read();
                channel.close();
        
        return bSuccess;
    }
    
    public boolean uninstall( AppDescriptor app )
           throws JoingServerException
    {
        boolean bSuccess = false;
       
        Channel channel = new Channel( APP_UNINSTALL );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( app );
        bSuccess = (Boolean) channel.read();
                channel.close();
        
        return bSuccess;
    }

    public AppDescriptor getPreferredForType( String sFileExtension )
           throws JoingServerException
    {
        AppDescriptor appDescriptor = null;
        
        Channel channel = new Channel( APP_GET_PREFERRED );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( sFileExtension );
        appDescriptor = (AppDescriptor) channel.read();
                channel.close();
        
        return appDescriptor;
    }
    
    public Application getApplication( int nAppId )
           throws JoingServerException
    {
        Application application = null;
        
        Channel channel = new Channel( APP_GET_APPLICATION );
                channel.write( platform.getBridge().getSessionId() );
                channel.write( nAppId );
        application = (Application) channel.read();
                channel.close();
        
        return application;
    }  
}