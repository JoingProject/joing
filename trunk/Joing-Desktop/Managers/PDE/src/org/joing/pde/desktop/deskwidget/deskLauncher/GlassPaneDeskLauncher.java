/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde.desktop.deskwidget.deskLauncher;

import java.awt.event.MouseEvent;
import org.joing.pde.desktop.deskwidget.GlassPaneWidget;

/**
 *
 * @author Francisco Morero Peyrona
 */
class GlassPaneDeskLauncher extends GlassPaneWidget
{
    private PDEDeskLauncher launcher;
    
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
    
    protected void mousePressed( MouseEvent me )
    {
        super.mousePressed( me );
        
        if( me.getButton() == MouseEvent.BUTTON1 )
            launcher.setSelected( true, me.isControlDown() );
    }
    
    protected void mouseClicked( MouseEvent me )
    {
        if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 )
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
}