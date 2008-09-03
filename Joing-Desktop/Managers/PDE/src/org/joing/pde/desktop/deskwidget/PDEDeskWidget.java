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
import javax.swing.JPopupMenu;
import org.joing.kernel.api.desktop.deskwidget.DeskWidget;
import org.joing.pde.desktop.container.PDECanvas;

/**
 * A Deskwidget is every component shown in the dektop that can be dragged 
 * (can change its position by the user interaction).
 * <p>
 * DeskLaunchers and Desklets are subclased from this class.
 *
 * @author Francisco Morero Peyrona
 */
public abstract class PDEDeskWidget extends PDECanvas implements DeskWidget
{
    /**
     * Creates a new instance of PDEDeskWidget
     */
    public PDEDeskWidget()
    {
        // As this class is abstract, it does not needs a glass pane.
        // But there is a better reason not to set a glass pane: if the next line 
        // is used, an extrange bug happens and dragging is a mess. -->
        // root.setGlassPane( new GlassPaneWidget( this ) );
    }
    
    //------------------------------------------------------------------------//
    // Following methods from DeskWidget interface already exists in JPanel:
    // getName(), setName(...), getLocation(), setLocation(...), 
    
    protected void showPopup( Point ptWhere )
    {
        JPopupMenu popup = getComponentPopupMenu();
        
        if( popup != null && ! popup.isVisible() )
            popup.show( this, ptWhere.x, ptWhere.y );
    }
}