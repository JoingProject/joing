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
package org.joing.pde.desktop.deskwidget.desklet;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.joing.pde.swing.GlassPaneBase;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class GlassPaneDesklet extends GlassPaneBase
{
    private Point ptMousePosition;
    private PDEDesklet desklet;     // 4 speed
    
    //------------------------------------------------------------------------//
    
    public GlassPaneDesklet( PDEDesklet owner )
    {
        super( owner );
        desklet = owner;
        ptMousePosition = new Point(); ///null;
    }
    
    //------------------------------------------------------------------------//
    
    protected  void onMouseEvent( MouseEvent me )
    {
        switch( me.getID() )
        {
            case MouseEvent.MOUSE_PRESSED: mousePressed( me ); break;
            case MouseEvent.MOUSE_DRAGGED: mouseDragged( me ); break;
            case MouseEvent.MOUSE_ENTERED: mouseEntered( me ); break;
            case MouseEvent.MOUSE_EXITED : mouseExited(  me ); break;
        }
    }
    
    protected void mousePressed( MouseEvent me )  
    {
        if( me.getButton() == MouseEvent.BUTTON1 )
            ptMousePosition = me.getPoint();
//        else
//            ptMousePosition = null;   // Used also as flag to know if drag was started by left (BUTTON1) click
    }
    
    protected void mouseDragged( MouseEvent me )
    {
//        if( ptMousePosition != null )
//        {
            int x = me.getPoint().x + desklet.getX() - ptMousePosition.x;
            int y = me.getPoint().y + desklet.getY() - ptMousePosition.y;

            desklet.setLocation( x,y );
//        }
    }
    
    protected void mouseEntered( MouseEvent me )
    {
        desklet.setToolBarVisible( true );
    }

    protected void mouseExited( MouseEvent me )
    {
        desklet.setToolBarVisible( false );
    }
}