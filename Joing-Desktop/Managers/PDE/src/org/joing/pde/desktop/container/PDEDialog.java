/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
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