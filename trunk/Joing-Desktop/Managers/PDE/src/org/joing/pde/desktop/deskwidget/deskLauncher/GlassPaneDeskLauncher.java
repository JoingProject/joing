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
package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.event.MouseEvent;
import org.joing.pde.desktop.deskwidget.GlassPaneWidget;

/**
 *
 * @author Francisco Morero Peyrona
 */
class GlassPaneDeskLauncher extends GlassPaneWidget
{
    private PDEDeskLauncher launcher;
    
    GlassPaneDeskLauncher( PDEDeskLauncher launcher )
    {
        super( launcher );
        this.launcher = launcher;
    }
    
    //------------------------------------------------------------------------//
    
    protected  void onMouseEvent( MouseEvent me )
    {
        switch( me.getID() )
        {
            case MouseEvent.MOUSE_PRESSED: mousePressed( me ); break;
            case MouseEvent.MOUSE_CLICKED: mouseClicked( me ); break;
            case MouseEvent.MOUSE_ENTERED: mouseEntered( me ); break;
            case MouseEvent.MOUSE_EXITED : mouseExited(  me ); break;
            
            default: 
                super.onMouseEvent( me ); break;
        }
    }
    
    protected void mousePressed( MouseEvent me )
    {
        super.mousePressed( me );
        
        if( me.getButton() == MouseEvent.BUTTON1 )
            launcher.setSelected( true, me.isControlDown() );
    }
    
    protected void mouseClicked( MouseEvent me )
    {
        if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 )
            launcher.launch();
    }
    
    protected void mouseEntered( MouseEvent me )
    {
        launcher.setHighlighted( true );
    }
    
    protected void mouseExited( MouseEvent me )
    {
        launcher.setHighlighted( false );
    }
}