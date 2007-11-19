/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    private Point ptMousePosition;
    private PDEDeskWidget widget;     // 4 speed
    
    //------------------------------------------------------------------------//
    
    public GlassPaneWidget( PDEDeskWidget owner )
    {
        super( owner );
        widget = owner;        
        ptMousePosition = new Point();
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
        if( me.getButton() == MouseEvent.BUTTON1 )
            ptMousePosition = me.getPoint();

        // I prefer to handle events personally in order to show the popup. 
        // See: http://www.jguru.com/forums/view.jsp?EID=1239349
        // Another advantage of this approach is that it saves memory because the
        // JPopupMenu is in memery only meanwhile it is shown (it is created and
        // destroied every time).
        if( me.isPopupTrigger() )
            widget.showPopup( me.getPoint() );
    }

    protected void mouseDragged( MouseEvent me )
    {
        int x = me.getPoint().x + widget.getX() - ptMousePosition.x;
        int y = me.getPoint().y + widget.getY() - ptMousePosition.y;

        widget.setLocation( x,y );
    }
}