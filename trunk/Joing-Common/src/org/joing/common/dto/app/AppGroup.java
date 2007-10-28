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
    public final static int UNKNOWN     = -1;
    public final static int ALL         =  0;
    
    public final static int ACCESSORIES =  1;
    public final static int EDUCATION   =  2;
    public final static int GAMES       =  3;
    public final static int GRAPHICS    =  4;
    public final static int INTERNET    =  5;
    public final static int MULTIMEDIA  =  6;
    public final static int OFFICE      =  7;
    public final static int PROGRAMMING =  8;
    public final static int SYSTEM      =  9;
    public final static int OTHER       = 10;
    
    public final static int DESKTOP     = 99;   // Desktops: PDE, etc.
    
    //------------------------------------------------------------------------//
    
    private int                 id;
    private String              name;
    private String              description;
    private byte[]              iconPNG;
    private byte[]              iconSVG;
    private List<AppDescriptor> apps;
    
    //------------------------------------------------------------------------//
    
    public AppGroup()
    {
        this( UNKNOWN );
    }
    
    public AppGroup( int nId )
    {
        this.id          = nId;
        this.name        = "";
        this.description = "";
        this.iconPNG     = null;
        this.iconSVG     = null;
        this.apps        = new ArrayList<AppDescriptor>();
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public byte[] getIconPNG()
    {
        return iconPNG;
    }
    
    public byte[] getIconSVG()
    {
        return iconSVG;
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
    
    public void setId( int nId )
    {
        this.id = nId;
    }
    
    public void setName( String name )
    {
        this.name = name;
    }
    
    public void setDescription( String description )
    {
        this.description = description;
    }
    
    public void setIconPNG( byte[] icon )
    {
        if( icon != null )
        {
            byte[] copy = new byte[ icon.length ];
            System.arraycopy( icon, 0, copy, 0, icon.length );   // defensive copy
            iconPNG = copy;
        }
        else
        {
            iconPNG = null;
        }
    }
    
    public void setIconSVG( byte[] icon )
    {
        if( icon != null )
        {
            byte[] copy = new byte[ icon.length ];
            System.arraycopy( icon, 0, copy, 0, icon.length );   // defensive copy
            iconSVG = copy;
        }
        else
        {
            iconSVG = null;
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