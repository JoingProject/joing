/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde.swing;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * GlassPane tutorial
 * "A well-behaved GlassPane"
 * http://weblogs.java.net/blog/alexfromsun/
 * <p/>
 * This is the final version of the GlassPane
 * it is transparent for MouseEvents,
 * and respects underneath component's cursors by default,
 * it is also friedly for other users,
 * if someone adds a mouseListener to this GlassPane
 * or set a new cursor it will respect them
 *
 * @author Alexander Potochkin
 */
public class FinalGlassPane extends JPanel implements AWTEventListener
{
    private final Component owner;
    //private Point point = new Point();

    public FinalGlassPane(Component owner)
    {
        super(null);
        this.owner = owner;
        setOpaque(false);
    }
    
    public void eventDispatched(AWTEvent event)
    {
        /*if (event instanceof MouseEvent)
        {
            MouseEvent me = (MouseEvent) event;
            if (!SwingUtilities.isDescendingFrom(me.getComponent(), owner))
            {
                return;
            }
            if (me.getID() == MouseEvent.MOUSE_EXITED && me.getComponent() == owner)
            {
                point = null;
            }
            else
            {
                MouseEvent converted = SwingUtilities.convertMouseEvent(me.getComponent(), me, owner.getGlassPane());
                point = converted.getPoint();
            }
            repaint();
        }*/
    }

    /**
     * If someone adds a mouseListener to the GlassPane or set a new cursor
     * we expect that he knows what he is doing
     * and return the super.contains(x, y)
     * otherwise we return false to respect the cursors
     * for the underneath components
     */
    public boolean contains(int x, int y)
    {
        if (getMouseListeners().length == 0 && 
            getMouseMotionListeners().length == 0 && 
            getMouseWheelListeners().length == 0 && 
            getCursor() == Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        {
            return false;
        }
        return super.contains(x, y);
    }
}