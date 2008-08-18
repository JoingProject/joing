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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskDialog;

/**
 * 
 * 
 * Note: this class uses two undocumented methods in java.awt.Container class to 
 * start/stop modal state.
 * 
 * @author Francisco Morero Peyrona
 */
public class PDEDialog extends JDialog implements DeskDialog
{
    public PDEDialog()
    {
        setLayout( new BorderLayout() );
        setModalityType( ModalityType.APPLICATION_MODAL );
        setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
    }
    
    public boolean isModal()
    {
        return true;
    }
    
    public void add( DeskComponent dc )
    {
        add( (Component) dc, BorderLayout.CENTER );
    }
    
    public Image getIcon()
    {
        List<Image> images = getIconImages();
        
        if( images == null || images.size() == 0 )
            return null;
        else
            return images.get( 0 );
    }
    
    public void setIcon( Image image )
    {
        super.setIconImage( image );
    }
    
    public void setLocationRelativeTo( DeskComponent parent )
    {
        super.setLocationRelativeTo( (Component) parent );
    }
    
    public void center()
    {
        super.setLocationRelativeTo( null );
    }
    
    public void remove( DeskComponent dc )
    {
        getContentPane().remove( (Component) dc );
    }

    public void close()
    {
        setVisible( false );
    }

    public boolean isSelected()
    {
        return super.isVisible();
    }

    public void setSelected( boolean bStatus )
    {
        // Nothing to do: a modal JDialog is selected when it is made visible.
    }
}