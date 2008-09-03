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
package org.joing.pde.desktop.taskbar.waSwitcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.joing.kernel.api.desktop.desktop.Desktop;
import org.joing.kernel.api.desktop.desktop.DesktopListener;
import org.joing.kernel.api.desktop.taskbar.TaskBar;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.taskbar.PDETaskBarComponent;
import org.joing.pde.desktop.workarea.PDEWorkArea;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class WorkAreaSwitcher extends PDETaskBarComponent
{
    private GridLayout grid;
    
    //------------------------------------------------------------------------//
    
    public WorkAreaSwitcher()
    {
        grid = new GridLayout( 1, 0, 0, 0 );
        
        setLayout( grid );
        setBorder( new LineBorder( Color.black, 1 ) );
        setInheritsPopupMenu( true );  // It is also inherited by sub-components
        
        Desktop desktop = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop();
        
        // Traverse existing workareas: one or more workareas would exist when this component is shown.
        List<WorkArea> lstWorAreas = desktop.getWorkAreas();
        
        for( WorkArea wa : lstWorAreas )
            onWorkAreaAdded( wa );
        //--------------------------------------------
        
        // Now the listener for future add/remove
        desktop.addDesktopListener( new DesktopListener()
        {
            public void workAreaAdded( WorkArea wa )   { onWorkAreaAdded( wa );   }
            public void workAreaRemoved( WorkArea wa ) { onWorkAreaRemoved( wa ); }
            public void workAreaSelected( WorkArea waPrevious, WorkArea waCurrent ) { onWorkAreaSelected( waPrevious, waCurrent ); }
            public void taskBarAdded( TaskBar tb )     {}
            public void taskBarRemoved( TaskBar tb )   {}
        } );
        
        calculateSizes( lstWorAreas.size() );
    }
    
    //------------------------------------------------------------------------//
    // TaskBarComponent interface
    
    public void onAbout()
    {
        // TODO: hacerlo
    }

    public void onRemove()
    {
        // TODO: hacerlo
    }

    public void onMove()
    {
        // TODO: hacerlo
    }

    public void onPreferences()
    {
        // TODO: hacerlo
    }
    
    //------------------------------------------------------------------------//
    
    protected JPanel getAboutPanel()
    {
        return null;   // TODO: hacerlo
    }

    protected JPanel getPreferencesPanel()
    {
        return new Preferences();
    }

    protected void onPreferencesChanged( JPanel pnl )
    {
        Preferences pnlPrefs = (Preferences) pnl;
        
        // TODO: procesar los cambios
    }
    
    //------------------------------------------------------------------------//
    
    private void onWorkAreaAdded( WorkArea wa )
    {
        calculateSizes( grid.getColumns() + 1 );
        
        Map map = new Map( (PDEWorkArea) wa );
        
        grid.setColumns( grid.getColumns() + 1 );
        add( map );
        grid.layoutContainer( this );
    }
    
    private void onWorkAreaRemoved( WorkArea wa )
    {
        calculateSizes( grid.getColumns() - 1 );
        
        remove( getMapFor( wa ) );       // Removes the button from the panel
        grid.setColumns( grid.getColumns() - 1 );
        grid.layoutContainer( this );
    }
    
    private void onWorkAreaSelected( WorkArea waPrevious, WorkArea waCurrent )
    {   
        if( waPrevious != null )
            getMapFor( waPrevious ).setSelected( false );
        
        if( waCurrent != null )
            getMapFor( waCurrent ).setSelected( true );
    }
    
    private void calculateSizes( int nComponents )
    {
        Insets    insets = getBorder().getBorderInsets( this );   
        int       nWidth = (nComponents * 22) + ((nComponents - 1) * grid.getHgap()) + (insets.left * 2);
        Dimension dim    = new Dimension( nWidth, 22 + (insets.top * 2) );
        
        setMinimumSize(   dim );
        setMaximumSize(   dim );
        setPreferredSize( dim );
    }
    
    private Map getMapFor( WorkArea wa )
    {
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( (aComp[n] instanceof Map) )
            {
                Map map = (Map) aComp[n];
                
                if( map.getTarget() == wa )
                    return map;
            }
        }
        
        return null;    // Should never arrive to here
    }
}