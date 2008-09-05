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
package org.joing.pde.desktop.deskwidget;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.joing.pde.swing.GlassPaneBase;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class GlassPaneWidget extends GlassPaneBase
{
    private PDEDeskWidget widget;     // 4 speed
    private Point ptMousePosition;
    
    //------------------------------------------------------------------------//
    
    public GlassPaneWidget( PDEDeskWidget owner )
    {
        super( owner );
        this.widget = owner;        
        ptMousePosition = null;
    }
    
    //------------------------------------------------------------------------//
        
    protected  void onMouseEvent( MouseEvent me )
    {
        switch( me.getID() )
        {
            case MouseEvent.MOUSE_PRESSED: mousePressed( me ); break;
            case MouseEvent.MOUSE_DRAGGED: mouseDragged( me ); break;
        }
    }
    
    protected void mousePressed( MouseEvent me )
    {
        // I prefer to handle events personally in order to show the popup. 
        // See: http://www.jguru.com/forums/view.jsp?EID=1239349
        // Another advantage of this approach is that it saves memory because the
        // JPopupMenu is in memery only meanwhile it is shown (it is created and
        // destroied every time).
        if( me.isPopupTrigger() )
        {
            widget.showPopup( me.getPoint() );
        }
        else
        {
            if( me.getButton() == MouseEvent.BUTTON1 )
                ptMousePosition = me.getPoint();
            else
                ptMousePosition = null;   // Used also as flag to know if drag was started by left (BUTTON1) click
        }
    }
    
    public void mouseReleased( MouseEvent me )   // Needed for Windows
    {
        if( me.isPopupTrigger() )
            widget.showPopup( me.getPoint() );
    }

    protected void mouseDragged( MouseEvent me )
    {
        if( ptMousePosition != null )
        {
            int x = me.getPoint().x + widget.getX() - ptMousePosition.x;
            int y = me.getPoint().y + widget.getY() - ptMousePosition.y;

            widget.setLocation( x,y );
        }
    }
}