package org.joing.pde.swing;

import org.joing.pde.misce.gpt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * "A well-behaved GlassPane"
 * http://weblogs.java.net/blog/alexfromsun/
 * 
 * @author Alexander Potochkin
 * @adapted_by Francsico Morero Peyrona
 */
public class GlassPaneBase extends JPanel implements AWTEventListener
{
    private final Container owner;

    public GlassPaneBase( Container owner )
    {
        super( null );
        this.owner = owner;
        setOpaque( false );
        
        Toolkit.getDefaultToolkit().addAWTEventListener( this,
                                                         AWTEvent.KEY_EVENT_MASK | 
                                                         AWTEvent.MOUSE_MOTION_EVENT_MASK | 
                                                         AWTEvent.MOUSE_EVENT_MASK);
    }

    protected void onMouseEvent( MouseEvent event )
    {        
    }
    
    protected void onKeyEvent( KeyEvent event )
    {        
    }
    
    public void eventDispatched( AWTEvent event )
    {
        if( event.getSource() instanceof Component &&
            SwingUtilities.isDescendingFrom( (Component) event.getSource(), owner ) )
        {
            if( event instanceof MouseEvent )
                onMouseEvent( (MouseEvent) event );
            else
                onKeyEvent( (KeyEvent) event );
        }
    }
}
