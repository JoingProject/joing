/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.beans.PropertyVetoException;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskWindow;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class PDEWindow extends JInternalFrame implements DeskWindow
{
    public PDEWindow( String sTitle, boolean bResizable, boolean bClosable, 
                      boolean bMaximizable, boolean bMinimizable )
    {
        super( sTitle, bResizable, bClosable, bMaximizable, bMinimizable );
    }
    
    //------------------------------------------------------------------------//
    // DeskWindow Interface
    
    public void setIcon( Image image )
    {
        if( image != null )
        {
            if( image.getHeight( this ) != 20 || image.getWidth( this ) != 20 )
                image = image.getScaledInstance( 20, 20, Image.SCALE_SMOOTH );

            setFrameIcon( new ImageIcon( image ) );
        }
    }
    
    public Image getIcon()
    {
        ImageIcon icon = (ImageIcon) getFrameIcon();
        
        return (icon == null ? null : icon.getImage());
    }
    
    public void center()
    {
        Container parent = getParent();   // Perhaps there is another parent sat (not DesktopPane).
        
        if( parent == null )
            parent = getDesktopPane();
        
        if( parent != null )
            center( parent );
    }
    
    public void setLocationRelativeTo( DeskComponent parent )
    {
        if( parent == null )
            center();
        else
            center( (Component) parent );
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
    
    private void center( Component parent )
    {
        int nX = (parent.getWidth()  - getWidth())  / 2;
        int nY = (parent.getHeight() - getHeight()) / 2;
        
        setLocation( parent.getX() + Math.max( nX, 0 ),
                     parent.getY() + Math.max( nY, 0 ) );
    }
}