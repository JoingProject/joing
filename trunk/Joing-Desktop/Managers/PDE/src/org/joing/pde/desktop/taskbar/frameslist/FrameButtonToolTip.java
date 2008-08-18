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
package org.joing.pde.desktop.taskbar.frameslist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JToolTip;

class FrameButtonToolTip extends JToolTip
{
    private Component toShow;
    
    public FrameButtonToolTip( Component toShow )
    {
        this.toShow = toShow;
    }
    
    public Dimension getPreferredSize()
    {
        Dimension dim = ((Component) org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop()).getSize();
                  dim = new Dimension( dim.width / 7, dim.height / 7 );
        return dim;
    }
    
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
    
    public Dimension getMaximumSize()
    {
        return getPreferredSize();
    }
    
    public void paintComponent( Graphics g )
    {
        BufferedImage bi = new BufferedImage( toShow.getWidth(), toShow.getHeight(), BufferedImage.TYPE_INT_RGB );
        toShow.paint( bi.createGraphics() );    // createGraphics() is preferred over getGraphics(). See JavaDoc.
        g.drawImage( bi.getScaledInstance( getWidth(), getHeight(), Image.SCALE_SMOOTH ), 0, 0, this );
    }
}