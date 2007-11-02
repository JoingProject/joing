/*
 *
 * Creado el 31-jul-2005 a las 18:10:41
 */

package org.joing.pde.desktop.container;

import java.awt.Color;
import java.awt.Container;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import org.joing.api.desktop.DeskFrame;
import org.joing.api.desktop.Selectable;

public class PDEFrame 
       extends JInternalFrame 
        implements Selectable, DeskFrame
{
    private boolean bAlwaysOnTop = false;
    
    //------------------------------------------------------------------------//
    
    public PDEFrame()
    {
        this( "" );
    }

    public PDEFrame( String title )
    {
        this( title, true );
    }

    public PDEFrame( String title, boolean resizable )
    {
        this( title, resizable, true );
    }

    public PDEFrame( String title, boolean resizable, boolean closable )
    {
        super( title, resizable, closable, true, true );
    }
    
    //------------------------------------------------------------------------//
    
    public void maximize()
    {
        try
        {
            if( ! isMaximum() )
            {
                if( isIcon() )
                    setIcon( false );
            
                setMaximum( true );
            }
        }
        catch( PropertyVetoException exc )
        {
            // Nothing to do
        }
    }
    
    /** Used by FramesList */
    public void restore()
    {
        try
        {
            if( isIcon() || isMaximum() )
            {
                if( isIcon() )
                    setIcon( false );
                
                setMaximum( false );
            }
        }
        catch( PropertyVetoException exc )
        {
            // Nothing to do
        }
    }
    
    public void setSelected( boolean bSelected )
    {
       try
        {
           super.setSelected( bSelected );
        }
        catch( PropertyVetoException exc )
        {
           // Nothing to do
        }
    }
    
    /**
     * Redefined because I need a different behaviour when a JInterlFrame is iconnized.
     */
    public void setIcon( boolean bIcon )
    {
        if( isIcon() != bIcon )
        {
            /*
             * If an internal frame is being iconified before it has a parent, 
             * (e.g., client wants it to start iconic), create the parent if 
             * possible so that we can place the icon in its proper place on 
             * the desktop. I am not sure the call to validate() is necessary, 
             * since we are not going to display this frame yet.
             */
            firePropertyChange( "ancestor", null, getParent() );

            /* Este cacho de código de JInternalFrame es el que hace q se invoque
               el proceso de iconización.
               Me he ilimitado a remearlo, con la esperanza de que sólo sirva para
               eso y q ningún otro escuchante utilize este evento.
            Boolean oldValue = isIcon ? Boolean.TRUE : Boolean.FALSE;
            Boolean newValue = b ? Boolean.TRUE : Boolean.FALSE;
            
            fireVetoableChange( IS_ICON_PROPERTY, oldValue, newValue );
            isIcon = b;
            firePropertyChange( IS_ICON_PROPERTY, oldValue, newValue );*/
            
            isIcon = bIcon;
            
            if( bIcon )
                fireInternalFrameEvent( InternalFrameEvent.INTERNAL_FRAME_ICONIFIED );
            else
                fireInternalFrameEvent( InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED );
            
            setSelected( ! bIcon );
            setVisible( ! bIcon );
        }
    }
    
    public boolean isAlwaysOnTop()
    {
        return this.bAlwaysOnTop;
    }
    
    public void setAlwaysOnTop( boolean b )
    { // TODO: hacerlo
        if( b != bAlwaysOnTop )
        {
            this.bAlwaysOnTop = b;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setTranslucency( int nPercent )
    {
        nPercent = (nPercent < 0 ? 0 : nPercent > 100 ? 100 : nPercent);
        nPercent = 255 * nPercent / 100;    // Pasa de [0 a 100] a [0 a 255]
        
        setOpaque( nPercent == 0 );
        
        if( nPercent > 0 )
        {
            Color clr = getContentPane().getBackground();
            getContentPane().setBackground( new Color( clr.getRed(), clr.getGreen(), clr.getBlue(), 255 - nPercent ) );
        }
        
        repaint();
    }
    
    public void center()
    {
        if( getParent() != null )
        {
            Container cp =  getParent();
            int       nX = (cp.getSize().width  - getWidth())  / 2;
            int       nY = (cp.getSize().height - getHeight()) / 2;
            
            setLocation( Math.max( nX, 0 ), Math.max( nY, 0 ) );
        }
    }
}