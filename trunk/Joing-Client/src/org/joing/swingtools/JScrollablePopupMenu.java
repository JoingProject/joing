/*
 * JScrollablePopupMenu.java
 *
 * Created on 25 de septiembre de 2007, 15:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.swingtools;

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