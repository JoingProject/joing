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
package org.joing.pde.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * "A well-behaved GlassPane"
 * http://weblogs.java.net/blog/alexfromsun/
 * 
 * @author Alexander Potochkin
 * @adapted_by Francsico Morero Peyrona
 */
public class GlassPaneBase extends JPanel implements AWTEventListener
{
    private Container owner;

    public GlassPaneBase( Container owner )
    {
        super( null );        // null because LayoutManager is not needed
        this.owner = owner;
        setOpaque( false );
        
        Toolkit.getDefaultToolkit().addAWTEventListener( this,
                                                         AWTEvent.KEY_EVENT_MASK | 
                                                         AWTEvent.MOUSE_MOTION_EVENT_MASK | 
                                                         AWTEvent.MOUSE_EVENT_MASK);
    }

    protected void onMouseEvent( MouseEvent event )
    {        
    }
    
    protected void onKeyEvent( KeyEvent event )
    {        
    }
    
    public void eventDispatched( AWTEvent event )
    {
        if( event.getSource() instanceof Component &&
            SwingUtilities.isDescendingFrom( (Component) event.getSource(), owner ) )
        {
            if( event instanceof MouseEvent )
                onMouseEvent( (MouseEvent) event );
            else
                onKeyEvent( (KeyEvent) event );
        }
    }
}
