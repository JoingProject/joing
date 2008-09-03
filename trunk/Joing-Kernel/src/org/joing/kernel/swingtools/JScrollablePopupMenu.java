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

package org.joing.kernel.swingtools;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Scrollable;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JScrollablePopupMenu extends JPopupMenu implements Scrollable
{
    /** Creates a new instance of JScrollablePopupMenu */
    public JScrollablePopupMenu()
    {
    }

/* TODO: Como esta clase no llega a funcionar, aquí hay otros sitios:
      http://www.beginner-java-tutorial.com/scrollable-jpopupmenu.html
      http://forum.java.sun.com/thread.jspa?threadID=454021&messageID=2068591
 * 
 Mirar aquí para los rollover:
      http://www.ibm.com/developerworks/web/library/us-j2d/
 */
    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }
    
    public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction )
    {
        return new JMenuItem("ABC").getPreferredSize().height;
    }
    
    public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction )
    {
        return new JMenuItem("ABC").getPreferredSize().height * 5;
    }
    
    public boolean getScrollableTracksViewportWidth()
    {
        return true;
    }
    
    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }
}