/*
 * AppGroup.java
 *
 * Created on 6 de junio de 2007, 12:55
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

package org.joing.common.dto.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
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