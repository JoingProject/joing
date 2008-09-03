/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde.desktop.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.beans.PropertyVetoException;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.pane.DeskWindow;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.kernel.swingtools.JoingSwingUtilities;

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
        
        // As this class is for PDE internal use only, the icon will always be PDE
        setIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.DESKTOP ) );
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
            parent = (Container) JoingSwingUtilities.findWorkAreaFor( this );
        
        if( parent != null )
            center( (Component) parent );
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
    {// TODO: Comprobar que se hace el detach del WorkArea
        setVisible( false );
        
        // Detach from container WorkArea
        WorkArea wa = JoingSwingUtilities.findWorkAreaFor( this );
        
        if( wa != null )
            wa.remove( this );
     
        // setClosed(...) could call dispose()
        try
        {
            setClosed( true );
        }
        catch( PropertyVetoException exc )
        {
        }
        
        // Releases all resources
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