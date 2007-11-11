/*
 * FrameButton.java
 *
 * Created on 15 de febrero de 2007, 11:35
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.taskbar.frameslist;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolTip;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.workarea.WorkArea;
import org.joing.pde.desktop.container.FramePopupMenu;
import org.joing.pde.desktop.container.PDEFrame;
import org.joing.pde.runtime.PDERuntime;

/**
 * Every button in the Frames List: each one represents a Frame, JFrame or 
 * JDesktopFrame
 * 
 * @author Francisco Morero Peyrona
 */
class FrameButton extends JToggleButton
{
    // Wihout a tooltip text "createToolTip()" method is not invoked
    private final static String sTOOLTIP = "A";  // To save memory all instances share same String
    
    private Frame    frame  = null;
    private PDEFrame iframe = null;
    
    //------------------------------------------------------------------------//
    
    FrameButton( Frame f )
    {
        this.frame = f;
        
        List<Image> icons = frame.getIconImages();
        Icon        icon  = null;

        if( icons.size() > 0 ) 
            icon = new ImageIcon( icons.get( 0 ) );

        initButton( frame.getTitle(), icon );
    }
    
    FrameButton( PDEFrame frame )
    {
        iframe = frame;

        initButton( iframe.getTitle(), iframe.getFrameIcon() );
    }
    
    public Container getFrame()
    {
        return (frame == null) ? iframe : frame;
    }
    
    public void setIcon( Icon icon )
    {
        if( icon != null )
        {
            Image image = null;   // Creo una nueva imagen para no desvirtuar la original escalándola aquí y allí
            
            if( icon instanceof ImageIcon )
            {
                image = ((ImageIcon) icon).getImage();
            }
            else
            {
                BufferedImage bi  = new BufferedImage( icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB );
                Graphics2D    g2D = bi.createGraphics();
                
                icon.paintIcon( this, g2D, 0, 0 );
                
                image = bi;
            }
            
            image = image.getScaledInstance( 16, 16, Image.SCALE_SMOOTH );
            
            super.setIcon( new ImageIcon( image ) );
        }
    }
    
    public JToolTip createToolTip()
    {
        // TODO: cambiarlo para que funcione tb con instancias de Frame
        if( getFrame() instanceof JInternalFrame )
        {
            FrameButtonToolTip tip = new FrameButtonToolTip( (PDEFrame) getFrame() );
            tip.setComponent( this );
            return tip;
        }
        else
            return null;
    }
    
    // TODO: hay q mirar dónde mostarlo (la barra puede estar: arriba, abajo, izq o dcha)
    /*public Point getToolTipLocation( MouseEvent me )
    {
        FrameButtonToolTip fbtt = new FrameButtonToolTip( null );
        Point              nPos = new Point( -1, -fbtt.getPreferredSize().height );        
        
        return nPos;
    }*/
    
    //------------------------------------------------------------------------//
    
    private void showPopupMenu( Point p )
    {
        // Has to be created every time because some items can change from ivocation to invocation.
        // And in this way, we also save memory (it exists in memory only while needed).
        FramePopupMenu popup;
        
        if( frame != null )
            popup = new FramePopupMenu( frame );
        else
            popup = new FramePopupMenu( iframe );
            
        popup.show( this, p.x, p.y );
    }
    
    private void initButton( String sTitle, Icon icon )
    {
        setText( sTitle );
        setIcon( icon );
        setToolTipText( sTOOLTIP );
        setFont( getFont().deriveFont( Font.PLAIN, 11f ) );
        setFocusPainted( false );
        setMargin( new Insets( 2,3,2,3 ) );
        setVerticalTextPosition( AbstractButton.CENTER );
        setHorizontalTextPosition( AbstractButton.TRAILING );
        
        // I use a mouse listener instead of JComponent.setComponentPopupMenu(...) 
        // to save memory: in this way the Popup object is created only when needed
        // (does not exists all the time in memory).
        addMouseListener( new MouseInputAdapter()
        {
            public void mousePressed( MouseEvent me )
            {
                if( me.isPopupTrigger() )
                    showPopupMenu( me.getPoint() );
            }
        } );
    }
}