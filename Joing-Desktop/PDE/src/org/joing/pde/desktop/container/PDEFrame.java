/*
 *
 * Creado el 31-jul-2005 a las 18:10:41
 */

package org.joing.pde.desktop.container;

import java.awt.Color;
import java.awt.Container;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import java.awt.Component;
import org.joing.api.desktop.DeskFrame;
import org.joing.api.desktop.Selectable;

public class PDEFrame 
       extends JInternalFrame 
        implements Selectable, DeskFrame
{
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
        
    public void restore()
    {
       PDEFrame.restore( this );
    }
    
    /** Used by FramesList */
    public static void restore( JInternalFrame ifrm )
    {// @TODO casca cuando se pasa de Maximized a Minimized y a Restored
       try
       {
          if( ifrm.isIcon() )
          {                        // To restore it must:
             ifrm.setMaximum( true );   // 1st maximize
             ifrm.setMaximum( false );  // 2nd restore
          }
          else if( ifrm.isMaximum() )
          {
              ifrm.setMaximum( false );
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
               Me he ilimitado a remearlo, con la esperanza de que solo sirva para
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