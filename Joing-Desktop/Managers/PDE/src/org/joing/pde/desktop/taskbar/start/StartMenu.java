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
package org.joing.pde.desktop.taskbar.start;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.dto.user.User;
import org.joing.pde.media.PDEColorSchema;
import org.joing.pde.PDEManager;
import org.joing.pde.apps.EditUser;
import org.joing.swingtools.JoingSwingUtilities;
import org.joing.swingtools.JScrollablePopupMenu;

/**
 *
 * @author Francisco Morero Peyrona
 */
class StartMenu extends JScrollablePopupMenu
{
    private final static int    ICON_SIZE = 22;
    private final static String KEY_APP_DESCRIPTOR = "JOING_APP_DESCRIPTOR";
    
    //------------------------------------------------------------------------//
    
    StartMenu()
    {
        setBorderPainted( true );
        setBorder( new CompoundBorder( new LineBorder( Color.black, 1 ),
                                       new EmptyBorder( 3,3,3,3 ) ) );
        addUser();
        addSeparator();
        addApplications();
        addSeparator();
        addLock();
        addExit();
    }
    
    //------------------------------------------------------------------------//
    
    private void addUser()
    {
        JMenuItem item = new JMenuItem();
                  item.setBackground( PDEColorSchema.getInstance().getUserNameBackground() );
                  item.setForeground( PDEColorSchema.getInstance().getUserNameForeground() );
                  item.setBorder( new EmptyBorder( 4,4,4,4 ) );
                  item.setFont( item.getFont().deriveFont( Font.BOLD, item.getFont().getSize() + 4 ) );

        User user = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getUserBridge().getUser();
        
        if( user != null )
        {
            StandardImage image = (user.isMale() ? StandardImage.USER_MALE : StandardImage.USER_FEMALE);
            
            item.setIcon( new ImageIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( image, ICON_SIZE+5, ICON_SIZE+5 ) ) );
            item.setText( user.getFirstName() +" "+ user.getSecondName() );

            item.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    (new EditUser()).showFrame();
                }
            } );
        }
        else
        {
            item.setText( "Error reading user" );
        }

        add( item );
    }
    
    private void addLock()
    {
        JMenuItem itmLock = new JMenuItem( "Lock session" );
                  itmLock.setIcon( JoingSwingUtilities.getIcon( this, "images/lock", ICON_SIZE, ICON_SIZE ) );
                  itmLock.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          ((PDEManager) org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager()).lock();
                      }
                  } );
        add( itmLock );
    }
    
    private void addExit()
    {
        JMenuItem itmExit = new JMenuItem( "End session" );
                  itmExit.setIcon( JoingSwingUtilities.getIcon( this, "images/exit", ICON_SIZE, ICON_SIZE ) );
                  itmExit.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          ((PDEManager) org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager()).exit();
                      }
                  } );
        add( itmExit );
    }
    
    private void addApplications()
    {
        List<AppGroup> lstGroups = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getAppBridge().getInstalledForUser( AppGroupKey.ALL );
                                              
        if( lstGroups != null )
        {
            // Having the same instance for all app items saves memory
            AppMenuItemListener apil = new AppMenuItemListener();
            
            for( AppGroup group : lstGroups )
            {
                if( group.getGroupKey() == AppGroupKey.DESKTOP )    // Don't show "Desktops" apps
                    break;
                
                JMenu menu = new JMenu( group.getName() );
                      menu.setIcon( createItemIcon( group.getIconPixel() ) );
                      
                add( menu );

                List<AppDescriptor> appList = group.getApplications();

                for( AppDescriptor appDesc : appList )
                {
                    JMenuItem itemApp = new JMenuItem( appDesc.getName() );
                              itemApp.setIcon( createItemIcon( appDesc.getIconPixel() ) );
                              itemApp.setToolTipText( appDesc.getDescription() );
                              itemApp.putClientProperty( KEY_APP_DESCRIPTOR, appDesc );
                              itemApp.addActionListener( apil );
                    menu.add( itemApp );
                }
            }
        }
    }
    
    private ImageIcon createItemIcon( byte[] anImage )
    {
        ImageIcon icon = null;
        
        if( anImage != null )
        {
            icon = new ImageIcon( anImage );
            
            if( icon.getIconWidth() != ICON_SIZE || icon.getIconHeight() != ICON_SIZE )
                icon.setImage( icon.getImage().getScaledInstance( ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH ) );
        }
        
        return icon;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: App menu item action listener.
    // To have only one instance for all app items (to save memory)
    //------------------------------------------------------------------------//
    private final class AppMenuItemListener implements ActionListener
    {
        public void actionPerformed( ActionEvent ae )
        {
            JMenuItem     item    = (JMenuItem) ae.getSource();
            AppDescriptor appDesc = (AppDescriptor) item.getClientProperty( KEY_APP_DESCRIPTOR );
            
            JoingSwingUtilities.launch( DeskLauncher.Type.APPLICATION, String.valueOf( appDesc.getId() ), null );   
        }
    }
}