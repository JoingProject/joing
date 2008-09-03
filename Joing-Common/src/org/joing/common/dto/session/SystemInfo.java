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

package org.joing.common.dto.session;

import java.io.Serializable;
import java.net.URL;

/**
 * Information about Join'g Server which user (client side) is connected to.
 * 
 * @author Francisco Morero Peyrona
 */
public class SystemInfo implements Serializable
{
    private String sName;
    private String sVersion;
    private String sEmailServer;

    //------------------------------------------------------------------------//
    
    public String getEmailServer()
    {
        return sEmailServer;
    }

    public String getName()
    {
        return sName;
    }

    public String getVersion()
    {
        return sVersion;
    }
    
    //------------------------------------------------------------------------//
    
    // NEXT: Constructor should be accessibe only from Server side
    public SystemInfo( String name, String ver, URL email )
    {
        sName        = (name  == null ? "???" : name.trim());
        sVersion     = (ver   == null ? "0.0" : ver.trim());
        sEmailServer = (email == null ? null  : email.toString());
    }
}