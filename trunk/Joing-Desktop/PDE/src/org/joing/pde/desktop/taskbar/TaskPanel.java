/*
 * TaskPanel.java
 *
 * Created on 15 de septiembre de 2007, 22:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop.taskbar;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class TaskPanel extends JPanel implements MouseListener
{
    /** Creates a new instance of TaskPanel */
    public TaskPanel()
    {
        setOpaque( false );
        addMouseListener( this );
        add( new JLabel( "||" ), BorderLayout.EAST );
    }

    //------------------------------------------------------------------------//
    
    // Mouse Listener is needed to show the popup. 
    // See: http://www.jguru.com/forums/view.jsp?EID=1239349
    public void mouseClicked(MouseEvent me)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }
}