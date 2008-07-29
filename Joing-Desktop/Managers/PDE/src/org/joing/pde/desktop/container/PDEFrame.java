/*
 *
 * Creado el 31-jul-2005 a las 18:10:41
 */

package org.joing.pde.desktop.container;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 * An improved JInternalFrame.
 * 
 * Among other things:
 * <ul>
 * <li> Easier to use
 * <li> Translucent background property
 * <li> AlwaysOnTop property
 * <li> Easy to add to a WorkArea (by default)
 * <li> By default setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE )
 * </ul>
 * @author Francisco Morero Peyrona
 */
public class PDEFrame extends PDEWindow implements DeskFrame
{   
    private boolean bAlwaysOnTop = false;
    
    //------------------------------------------------------------------------//
    
    public PDEFrame()
    {           // resizable, closable, maximizable, minimizable
        super( "", true,      true,     true,        true );
        
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        
        // To show a popup menu when mouse right clicked on title bar or
        // left clicked on title-bar icon.
        JComponent pane = ((BasicInternalFrameUI) getUI()).getNorthPane();
                   pane.addMouseListener( new MyMouseListener() );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * Redefined from JInternalFrame because I need a different behaviour 
     * when a JInterlFrame is iconnized.
     * <p>
     * This method can't be provate but should not be used outside of this class.
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
    
    //------------------------------------------------------------------------//
    // DeskFrame Interface
    
    public void setStatus( Status status )
    {
        switch( status )
        {
            case MAXIMIZED: maximize(); break;
            case MINIMIZED: minimize(); break;
            case RESTORED : restore();  break;
        }
    }
    
    public Status getStatus()
    {
        if( isMaximum() ) return Status.MAXIMIZED;
        if( isIcon() )    return Status.MINIMIZED;
        
        return Status.RESTORED;
            
    }
    
    public boolean isAlwaysOnTop()
    {
        return this.bAlwaysOnTop;
    }
    
    /**
     * Places the frame over all other frames (and below dialogs, obviously).
     * <p>
     * This property must be sat before adding the frame to the desktop, otherwise
     * it has no effect.
     * 
     * @param b New value.
     */
    public void setAlwaysOnTop( boolean b )
    {
        this.bAlwaysOnTop = b;   // In PDE this property is handled by the WorkArea
        // TODO: Hay que lanzar un ChangedProperyEvent y hacer que el WorkArea lo escuche para que la quite ponga y la quite del top
    }
    
    //------------------------------------------------------------------------//
    // Special PDE methods
    
    public void setTranslucency( int nPercent )
    {
        nPercent = (nPercent < 0 ? 0 : nPercent > 100 ? 100 : nPercent);
        nPercent = (255 * nPercent) / 100;    // Moves from range [0 a 100] to range [0 a 255]
        
        setOpaque( nPercent == 0 );
        
        if( nPercent > 0 )
        {
            Color clr = getContentPane().getBackground();
            getContentPane().setBackground( new Color( clr.getRed(), clr.getGreen(), clr.getBlue(), 255 - nPercent ) );
        }
        
        repaint();  // Needed
    }
    
    //------------------------------------------------------------------------//
    // PRIVATES
    
    private void minimize()
    {
        setIcon( true );
    }
    
    private void maximize()
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
    private void restore()
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
    
    private void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from ivocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
        FramePopupMenu popup = new FramePopupMenu( this );            
                       popup.show( this, p.x, p.y );
    }
    
    //------------------------------------------------------------------------//
    //
    //------------------------------------------------------------------------//
    private final class MyMouseListener implements MouseListener
    {
        public void mouseClicked( MouseEvent e )
        {
        }

        // Note: In this context, isPopupTrigger() does not work
        public void mousePressed( MouseEvent me )
        {
            /*
            // TODO: hacer que click con el btn izq sobre el icono de la barra de 
            //       título o el dcho en cualquier parte de la barra, abra el popup
            
            if( me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3 )
                showPopupMenu( me.getPoint() );
             */
        }
        
        public void mouseReleased( MouseEvent e )
        {
        }

        public void mouseEntered( MouseEvent e )
        {
        }

        public void mouseExited( MouseEvent e )
        {
        }
    }
}