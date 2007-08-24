/*
 * AppStartedEvent.java
 *
 * Created on 24 de junio de 2007, 11:28
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

package org.joing.AppLauncher;

import org.joing.jvmm.*;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class AppStartedEvent extends AppEvent
{
    private String sName;
    
    /**
     * Creates a new instance of AppStartedEvent
     */
    public AppStartedEvent( Object source )
    {
        super( source );
    }
    
    public String getName()
    {
        return this.sName;
    }
    
    //------------------------------------------------------------------------//
    
    void setName( String sName )
    {
        this.sName = sName;
    }
}