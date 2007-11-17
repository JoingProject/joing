/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.event.MouseEvent;
import org.joing.pde.desktop.deskwidget.GlassPaneWidget;

/**
 *
 * @author fmorero
 */
class GlassPaneDeskLauncher extends GlassPaneWidget
{
    protected PDEDeskLauncher launcher;
    
    GlassPaneDeskLauncher( PDEDeskLauncher launcher )
    {
        super( launcher );
        this.launcher = launcher;
    }
    
    //------------------------------------------------------------------------//
    
    protected  void onMouseEvent( MouseEvent me )
    {
        switch( me.getID() )
        {
            case MouseEvent.MOUSE_PRESSED: mousePressed( me ); break;
            case MouseEvent.MOUSE_CLICKED: mouseClicked( me ); break;
            case MouseEvent.MOUSE_ENTERED: mouseEntered( me ); break;
            case MouseEvent.MOUSE_EXITED : mouseExited(  me ); break;
            
            default: 
                super.onMouseEvent( me ); break;
        }
    }

    protected void mouseClicked( MouseEvent me )
    {
        if( me.getClickCount() == 2 )
            launcher.launch();
    }
    
    protected void mouseEntered( MouseEvent me )
    {
        launcher.setHighlighted( true );
    }
    
    protected void mouseExited( MouseEvent me )
    {
        launcher.setHighlighted( false );
    }

    protected void mousePressed( MouseEvent me )
    {
        super.mousePressed( me );
        
        boolean bCtrlPressed = (me.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
        launcher.setSelected( true, bCtrlPressed );
    }
}