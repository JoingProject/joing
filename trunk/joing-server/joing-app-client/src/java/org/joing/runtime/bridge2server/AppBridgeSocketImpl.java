/*
 * AppBridgeDirectImpl.java
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

import ejb.app.AppDescriptor;
import ejb.app.Application;
import ejb.app.AppsByGroup;
import java.util.List;

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

    public List<AppsByGroup> getAvailableForUser()
    {
        return null;
    }

    public List<AppsByGroup> getNotInstalledForUser()
    {
        return null;
    }

    public List<AppsByGroup> getInstalledForUser()
    {
        return null;
    }

    public boolean install( AppDescriptor app    )
    {
        return false;
    }

    public boolean uninstall( AppDescriptor app    )
    {
        return false;
    }

    public AppDescriptor getPreferredForType(String sFileExtension)
    {
        return null;
    }

    public Application getApplication( int nAppId )
    {
        return null;
    }
}