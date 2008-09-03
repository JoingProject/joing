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
package org.joing.pde.desktop.taskbar.systray;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.pde.desktop.taskbar.PDETaskBarPanel;
import org.joing.pde.swing.PDEAboutPanel;

/**
 * AKA: "Notification Area", "Taskbar Status Area", etc.<br>
 * I use the this name because is the one used by Sun.<br>
 * For more inforation about the proper name, visit:<br>
 * http://blogs.msdn.com/oldnewthing/archive/2003/09/10/54831.aspx<br>
 * and<br>
 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/systemtray/
 * <p>
 * In PDE it is mainly a PDETaskBarPanel that has a special name (SysTray) and can 
 * be accessed from the TaskBar.
 *
 * @author Francisco Morero Peyrona
 */
public class SysTray extends PDETaskBarPanel
{
    private static final int nHGAP = 2;
    
    //------------------------------------------------------------------------//
    
    /** Creates a new instance of SysTray */
    public SysTray()
    {
        setLayout( new FlowLayout( FlowLayout.LEADING, nHGAP, 0 ) );
    }
    
    public Dimension getMinimumSize()
    {
        int nWidth  = 0;
        int nHeight = 0;
        
        Component[] aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            Dimension preferred = aComp[n].getPreferredSize();
            
            nWidth += preferred.width;
            nHeight = Math.max( nHeight, preferred.height );
        }
        
        nWidth += (aComp.length - 1) * nHGAP;
        nWidth += 10;  // TODO: el 10 es por el component que está a la izq ("||"): hacer esto bien, no así, a lo bestia
        
        return new Dimension( nWidth, nHeight );
    }
    
    public Dimension getMaximunSize()
    {
        return getMinimumSize();
    }

    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
    
    //------------------------------------------------------------------------//
    // TaskBar Interface
    
    public void onPreferences()
    {
        // Nothing to do: SysTray has no preferences
    }

    public void onAbout()
    {
        PDEAboutPanel panel = new PDEAboutPanel();
                      panel.setProductName( "Notification Area" );
                      panel.setVersion( "1.0" );
                      
        // Better to use a Frame than a Dialog (modaless: this is the way Gnome does it)
        DeskFrame frame = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createFrame();
                  frame.setTitle( "About" );
                  frame.add( (DeskComponent) panel );
                  
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( frame );
    }

    public void onRemove()
    {
        // Nothing to do: SysTray can not be removed
    }

    public void onMove()
    {
        // Nothing to do: SysTray can not be moved
    }

    //------------------------------------------------------------------------//
    // Closeable Interface
    
    public void close()
    {
        // TODO: Llamar a close() en todos sus sub-comps 
    }
}