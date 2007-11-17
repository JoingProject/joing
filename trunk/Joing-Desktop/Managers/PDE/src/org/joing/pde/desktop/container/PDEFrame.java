/*
 *
 * Creado el 31-jul-2005 a las 18:10:41
 */

package org.joing.pde.desktop.container;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.joing.common.desktopAPI.Selectable;
import org.joing.common.desktopAPI.container.DeskFrame;

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
public class PDEFrame 
       extends JInternalFrame 
        implements Selectable, DeskFrame
{
    private boolean bAlwaysOnTop = false;
    private boolean bAutoArrange = true;
    
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
        init();
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
        if( getDesktopPane() != null )
        {
            Container cp = getDesktopPane();
            int       nX = (cp.getSize().width  - getWidth())  / 2;
            int       nY = (cp.getSize().height - getHeight()) / 2;
            
            setLocation( Math.max( nX, 0 ), Math.max( nY, 0 ) );
        }
    }
    
    /**
     * Return AutoArrange property status.
     * @return AutoArrange property status.
     */
    public boolean isAutoArrange()
    {
        return bAutoArrange;
    }

    /**
     * If <code>true</code>, when frame is added to the WorkArea, it will be 
     * automatically packed, centered, selected and moved to front.
     * <p>
     * By default it is <code>true</code>.
     * 
     * @param bAutoArrange New AutoArrange property status
     */
    public void setAutoArrange( boolean bAutoArrange )
    {
        this.bAutoArrange = bAutoArrange;
    }
    
    //------------------------------------------------------------------------//
    
    private void init()
    {
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        setAutoArrange( true );
        
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