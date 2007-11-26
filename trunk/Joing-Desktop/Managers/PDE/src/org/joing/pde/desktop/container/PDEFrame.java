/*
 *
 * Creado el 31-jul-2005 a las 18:10:41
 */

package org.joing.pde.desktop.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.joing.common.desktopAPI.DeskComponent;
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
public class PDEFrame extends JInternalFrame implements DeskFrame
{
    private boolean bAlwaysOnTop = false;
    
    //------------------------------------------------------------------------//
    
    public PDEFrame()
    {           // resizable, closable, maximizable, minimizable
        super( "", true, true, true, true );
    }
    
    //------------------------------------------------------------------------//
    // Container interface
    
    public void add( DeskComponent dc )
    {
        getContentPane().add( (Component) dc );
    }
    
    public void remove( DeskComponent dc )
    {
        getContentPane().remove( (Component) dc );
    }
    
    //------------------------------------------------------------------------//
    // Closeable interface
    
    public void close()
    {
        try
        {
            setClosed( true );
        }
        catch( PropertyVetoException exc )
        {
        }
        
        dispose();
    }
    
    //------------------------------------------------------------------------//
    // DeskWindow interface
    
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
    
    public void setIcon( Image image )
    {
        setFrameIcon( new ImageIcon( image ) );
    }
    
    public Image getIcon()
    {
        return ((ImageIcon) getFrameIcon()).getImage();
    }
    
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
    
    public void center()
    {
        if( getDesktopPane() != null )
        {
            Container cp = getDesktopPane();
            int       nX = (cp.getSize().width  - getWidth())  / 2;
            int       nY = (cp.getSize().height - getHeight()) / 2;
            
            setLocation( Math.max( nX, 0 ), Math.max( nY, 0 ) );
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
    
    public void setAlwaysOnTop( boolean b )
    { // TODO: hacerlo
        if( b != bAlwaysOnTop )
        {
            this.bAlwaysOnTop = b;
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //------------------------------------------------------------------------//
    // Special PDE methods
    
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
    
    private void init()
    {
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        
        JComponent pane = ((BasicInternalFrameUI) getUI()).getNorthPane();
        
        pane.addMouseListener( new MouseAdapter()
        {// TODO: hacer que click con el btn izq sobre el icono de la barra de título abra el popup
            
            // Note: In this context, isPopupTrigger() does not work
            public void mousePressed( MouseEvent me )
            {
                if( me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3 )
                    showPopupMenu( me.getPoint() );
            }
        } );
    }
    
    private void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from ivocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
        FramePopupMenu popup = new FramePopupMenu( this );            
                       popup.show( this, p.x, p.y );
    }
}