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

package org.joing.common.dto.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A group of applications with similar functionality (accesories, internet, 
 * system, office, etc).
 *
 * @author Francisco Morero Peyrona
 */
public class AppGroup implements Serializable
{
    private AppGroupKey         groupKey;
    private String              name;
    private String              description;
    private byte[]              iconPixel;
    private byte[]              iconVector;
    private List<AppDescriptor> apps;
    
    //------------------------------------------------------------------------//
    
    public AppGroup()
    {
        this( AppGroupKey.UNKNOWN );
    }
    
    public AppGroup( AppGroupKey key )
    {
        this.groupKey    = key;
        this.name        = "";
        this.description = "";
        this.iconPixel   = null;
        this.iconVector  = null;
        this.apps        = new ArrayList<AppDescriptor>();
    }
    
    public AppGroupKey getGroupKey()
    {
        return groupKey;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public byte[] getIconPixel()
    {
        return iconPixel;
    }
    
    public byte[] getIconVector()
    {
        return iconVector;
    }
    
    /**
     *
     * Guaranted to be a list with zero or more elements.
     */
    public List<AppDescriptor> getApplications()
    {
        return apps;
    }
    
    //------------------------------------------------------------------------//
    // NEXT: Los siguientes métodos debieran ser package (o al menos protected)
    
    public void setId( AppGroupKey key )
    {
        this.groupKey = key;
    }
    
    public void setName( String name )
    {
        this.name = name;
    }
    
    public void setDescription( String description )
    {
        this.description = description;
    }
    
    public void setIconPixel( byte[] icon )
    {
        if( icon != null )
        {
            byte[] copy = new byte[ icon.length ];
            System.arraycopy( icon, 0, copy, 0, icon.length );   // defensive copy
            iconPixel = copy;
        }
        else
        {
            iconPixel = null;
        }
    }
    
    public void setIconVector( byte[] icon )
    {
        if( icon != null )
        {
            byte[] copy = new byte[ icon.length ];
            System.arraycopy( icon, 0, copy, 0, icon.length );   // defensive copy
            iconVector = copy;
        }
        else
        {
            iconVector = null;
        }
    }
    
    public void addApplication( AppDescriptor app )
    {
        if( apps != null )
            apps.add( app );
    }
    
    public void addApplications( List<AppDescriptor> apps )
    {
        if( apps != null )
        {
            for( AppDescriptor ad : apps )
                this.apps.add( ad );
        }
    }
    
    public void setApplications( List<AppDescriptor> apps )
    {
        if( apps == null )
            this.apps.clear();   // this.apps will never be null
        else
            this.apps = apps;
    }
}