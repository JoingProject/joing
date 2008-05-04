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

import java.util.ArrayList;
import org.joing.common.clientAPI.runtime.AppBridge;
import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.AppEnvironment;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerException;
import org.joing.common.pkt.app.ApplicationReply;
import org.joing.common.pkt.app.ApplicationRequest;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class AppBridgeServletImpl
        extends BridgeServletBaseImpl
        implements AppBridge
{
    ApplicationCache cache =
            ApplicationCacheFactory.getCache( ApplicationCache.ID );

    /**
     * Creates a new instance of AppBridgeServletImpl
     * 
     * Package scope: only Runtime class can create instances of this class.
     */
    AppBridgeServletImpl()
    {
    }

    public List<AppGroup> getAvailableForUser( AppEnvironment environ, AppGroupKey groupKey )
            throws JoingServerException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_AVAILABLES );
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        channel.write( environ );
        channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
        channel.close();

        return apps;
    }

    public List<AppGroup> getNotInstalledForUser( AppEnvironment environ, AppGroupKey groupKey )
            throws JoingServerException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_NOT_INSTALLED );
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        channel.write( environ );
        channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
        channel.close();

        return apps;
    }

    public List<AppGroup> getInstalledForUser( AppEnvironment environ, AppGroupKey groupKey )
            throws JoingServerException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_INSTALLED );
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        channel.write( environ );
        channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
        channel.close();

        return apps;
    }

    public boolean install( AppDescriptor app )
            throws JoingServerException
    {
        boolean bSuccess = false;

        Channel channel = new Channel( APP_INSTALL );
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
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
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
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
        channel.write( platform.getBridge().getSessionBridge().getSessionId() );
        channel.write( sFileExtension );
        appDescriptor = (AppDescriptor) channel.read();
        channel.close();

        return appDescriptor;
    }

    public Application getApplication( int nAppId )
            throws JoingServerException
    {
        Application application = null;

        application = cache.get( nAppId );

        if( application == null )
        {
            Channel channel = new Channel( APP_GET_APPLICATION );
            channel.write( platform.getBridge().getSessionBridge().getSessionId() );
            channel.write( nAppId );
            application = (Application) channel.read();
            channel.close();
            if( application != null )
            {
                cache.put( nAppId, application );
            }
        }

        return application;
    }

    public Application getApplicationByName( String executableName )
            throws JoingServerException
    {

        Application application = null;

        // aqui tenemos el problema. Por lo pronto llevaremos
        // doble cache.
        application = cache.get( executableName );

        if( application == null )
        {
            Channel channel = new Channel( APP_SERVLET );
            ApplicationRequest req = new ApplicationRequest();
            String sessionId =
                    platform.getBridge().getSessionBridge().getSessionId();
            String account =
                    platform.getBridge().getUserBridge().getUser().getAccount();
            req.setAccount( account );
            req.setName( executableName );
            req.setSessionId( sessionId );
            req.setCode( ApplicationRequest.APP_BY_NAME );

            channel.write( req );
            ApplicationReply reply = (ApplicationReply) channel.read();
            channel.close();

            if( reply.isOk() == false )
            {
                application = null;
            }
            else
            {
                application = (Application) reply.getReply();
                cache.put( executableName, application );
            }
        }

        return application;
    }

    public List<Application> getAvailableDesktops()
            throws JoingServerException
    {

        Channel channel = new Channel( APP_SERVLET );
        ApplicationRequest req = new ApplicationRequest();
        req.setCode( ApplicationRequest.AVAILABLE_DESKTOPS );

        List<Application> apps = null;

        channel.write( req );
        ApplicationReply reply = (ApplicationReply) channel.read();
        channel.close();

        if( reply.isOk() )
        {
            apps = (List<Application>) reply.getReply();
        }
        else
        {
            apps = new ArrayList<Application>();
        }

        for( Application app : apps )
        {
            cache.put( app.getId(), app );
        }

        return apps;
    }
}