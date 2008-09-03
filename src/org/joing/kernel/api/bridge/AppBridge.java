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

package org.joing.kernel.api.bridge;

import java.util.List;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;

/**
 * Interface that describes applications related information and actions at 
 * the server side that can be accessed from the client side.
 * 
 * @author Francisco Morero Peyrona
 */
public interface AppBridge
{
    List<AppGroup> getAvailableForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    List<AppGroup> getNotInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    List<AppGroup> getInstalledForUser( AppGroupKey groupKey )
            throws JoingServerAppException;
    
    boolean install( AppDescriptor app )
            throws JoingServerAppException;
    
    boolean uninstall( AppDescriptor app )
            throws JoingServerAppException;
    
    AppDescriptor getPreferredForType( String sFileExtension )
            throws JoingServerAppException;
    
    /**
     * Abstract method to get an application from the Joing Server. The concrete
     * implementation must handle details about the preferred way to fetch
     * data from the server.
     * @param nAppId Application Id.
     * @return Application instance.
     */
    Application getApplication( int nAppId )
            throws JoingServerAppException;
    
    List<AppDescriptor> getAvailableDesktops()
            throws JoingServerAppException;
}