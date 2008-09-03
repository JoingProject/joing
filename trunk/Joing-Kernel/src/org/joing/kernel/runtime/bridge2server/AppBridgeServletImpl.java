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

import java.util.ArrayList;
import org.joing.kernel.api.bridge.AppBridge;
import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;
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

    public List<AppGroup> getAvailableForUser( AppGroupKey groupKey )
            throws JoingServerAppException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_AVAILABLES );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
                channel.close();

        return apps;
    }

    public List<AppGroup> getNotInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_NOT_INSTALLED );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
                channel.close();

        return apps;
    }

    public List<AppGroup> getInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException
    {
        List<AppGroup> apps = null;

        Channel channel = new Channel( APP_GET_INSTALLED );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( groupKey );
        apps = (List<AppGroup>) channel.read();
                channel.close();

        return apps;
    }

    public boolean install( AppDescriptor app )
            throws JoingServerAppException
    {
        boolean bSuccess = false;

        Channel channel = new Channel( APP_INSTALL );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( app.getId() );
        bSuccess = (Boolean) channel.read();
                channel.close();

        return bSuccess;
    }

    public boolean uninstall( AppDescriptor app )
            throws JoingServerAppException
    {
        boolean bSuccess = false;

        Channel channel = new Channel( APP_UNINSTALL );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( app.getId() );
        bSuccess = (Boolean) channel.read();
                channel.close();

        return bSuccess;
    }

    public AppDescriptor getPreferredForType( String sFileExtension )
            throws JoingServerAppException
    {
        AppDescriptor appDescriptor = null;

        Channel channel = new Channel( APP_GET_PREFERRED );
                channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                channel.write( sFileExtension );
        appDescriptor = (AppDescriptor) channel.read();
                channel.close();

        return appDescriptor;
    }

    public Application getApplication( int nAppId )
            throws JoingServerAppException
    {
        Application application = cache.get( nAppId );

        if( application == null )
        {
            Channel channel = new Channel( APP_GET_APPLICATION );
                    channel.write( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().getSessionBridge().getSessionId() );
                    channel.write( nAppId );
            application = (Application) channel.read();
                    channel.close();
                    
            if( application != null )
                cache.put( nAppId, application );
        }

        return application;
    }

    public List<AppDescriptor> getAvailableDesktops()
            throws JoingServerAppException
    {
        Channel            channel = new Channel( APP_SERVLET );
        ApplicationRequest req     = new ApplicationRequest();
                           req.setCode( ApplicationRequest.AVAILABLE_DESKTOPS );

        List<AppDescriptor> apps = new ArrayList<AppDescriptor>();

        channel.write( req );
        ApplicationReply reply = (ApplicationReply) channel.read();
        channel.close();

        if( reply.isOk() )
        {
            apps = (List<AppDescriptor>) reply.getReply();
        }

        return apps;
    }
}