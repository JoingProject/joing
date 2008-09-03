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
package org.joing.pde.desktop.taskbar;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.taskbar.TaskBarPanel;
import org.joing.pde.swing.JToolBarHandle;

/**
 * The base class for most part of the widtgets that are shown in the TaskBar.
 * 
 * By default it has a BoxLayout on X_AXIS.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDETaskBarPanel extends PDETaskBarComponent implements TaskBarPanel
{
    private static final String DONT_USE_ME = "Do not use me";
    
    private JToolBarHandle handle;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of PDETaskBarPanel */
    public PDETaskBarPanel()
    {
        handle = new JToolBarHandle( this );
        
        setBorder( new EmptyBorder( 0, handle.getPreferredSize().width + 2, 0, 2 ) );
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
    }

    public void setBorder( Border border )
    {
        // I can not allow to modify border.
        // But I can not throw an exception because this method is invoked by Swing.
    }
    
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        handle.paintComponent( g );
    }
        
    //------------------------------------------------------------------------//
    // DeskContainer interface
    
    // Note: A TaskBarComponent is also a DeskComponent nad a DeskContainer also
    public void add( DeskComponent comp )
    {
        super.add( (Component) comp );
    }
    
    public void remove( DeskComponent comp )
    {
        super.remove( (Component) comp );
    }
    
    //------------------------------------------------------------------------//
    // TaskBarComponent interface
    
    public void onAbout()
    {
        // TODO: Hacerlo
    }
    
    public void onRemove()
    {
        // TODO: Hacerlo
    }
    
    public void onMove()
    {
        // TODO: Hacerlo
    }
    
    public void onPreferences()
    {
        // TODO: Hacerlo
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        
    }
    
    // Just to avoid accidental use of them  ---------------------------------------------------------
    public Component add( Component c )             { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( Component c, int n )      { throw new IllegalAccessError(DONT_USE_ME); }
    public Component add( String s, Component c )   { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o )        { throw new IllegalAccessError(DONT_USE_ME); }
    public void add( Component c, Object o, int n ) { throw new IllegalAccessError(DONT_USE_ME); }
    public void remove( Component c )               { throw new IllegalAccessError(DONT_USE_ME); }
    //------------------------------------------------------------------------------------------------
}