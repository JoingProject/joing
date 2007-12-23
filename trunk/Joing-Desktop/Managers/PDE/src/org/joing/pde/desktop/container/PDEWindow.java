/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.desktop.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskWindow;
import org.joing.pde.PDEUtilities;

/**
 *
 * @author fmorero
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
    
    public void setIcon( byte[] image )
    {
        Image img = Toolkit.getDefaultToolkit().createImage( image );
        setFrameIcon( new ImageIcon( img ) );
    }
    
    public byte[] getIcon()
    {
        return PDEUtilities.icon2ByteArray( (ImageIcon) getFrameIcon() );
    }
    
    public void center()
    {
        if( getDesktopPane() != null )
        {
            Container cp = getDesktopPane();
            int       nX = (cp.getWidth()  - getWidth())  / 2;
            int       nY = (cp.getHeight() - getHeight()) / 2;
            
            setLocation( Math.max( nX, 0 ), Math.max( nY, 0 ) );
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
}