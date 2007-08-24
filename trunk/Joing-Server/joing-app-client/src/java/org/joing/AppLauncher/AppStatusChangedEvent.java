/*
 * AppStatusChangedEvent.java
 *
 * Created on 24 de junio de 2007, 11:51
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

import org.joing.AppLauncher.AppEvent;

/**
 * RAM, CPU y Threads, los he puesto sólo como ejemplo: tú pon lo
 * que quieras y que Java lo permita, claro.
 *
 * @author Francisco Morero Peyrona
 */
public class AppStatusChangedEvent extends AppEvent
{
    private int nKb;
    private int nCPU;
    
    /** 
     * Creates a new instance of AppStatusChangedEvent
     * 
     * @param source 
     */
    public AppStatusChangedEvent( Object source )
    {
        super( source );
    }
    
    public int getMemory()
    {
        return this.nKb;
    }
    
    public int getCPU()
    {
        return this.nCPU;
    }
    
    //------------------------------------------------------------------------//
    
    void setMemory( int nKb )
    {
        this.nKb = nKb;
    }
    
    void setCPU( int nCPU )
    {
         this.nCPU = (nCPU > 100 ? 100 : (nCPU < 0 ? 0 : nCPU));
    }
}