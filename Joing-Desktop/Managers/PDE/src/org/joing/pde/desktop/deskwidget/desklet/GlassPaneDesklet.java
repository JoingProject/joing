/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde.desktop.deskwidget.desklet;

import org.joing.pde.desktop.deskwidget.*;
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