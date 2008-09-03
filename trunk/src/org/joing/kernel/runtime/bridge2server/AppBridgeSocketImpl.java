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

import org.joing.kernel.api.bridge.AppBridge;
import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class AppBridgeSocketImpl
       extends BridgeSocketBaseImpl
       implements AppBridge        
{    
    /** 
     * Creates a new instance of AppBridgeDirectImpl 
     *
     * Package scope: only Runtime class can create instances of this class.
     */
    AppBridgeSocketImpl()
    {
        super();
    }

    public List<AppGroup> getAvailableForUser( AppGroupKey groupKey ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<AppGroup> getNotInstalledForUser( AppGroupKey groupKey ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public List<AppGroup> getInstalledForUser( AppGroupKey groupKey ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean install( AppDescriptor app ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public boolean uninstall( AppDescriptor app ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public AppDescriptor getPreferredForType( String sFileExtension ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Application getApplication( int nAppId ) throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public List<AppDescriptor> getAvailableDesktops() throws JoingServerAppException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}