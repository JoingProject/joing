/*
 * StartMenu.java
 *
 * Created on 15 de febrero de 2007, 14:04
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
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
import org.joing.common.dto.app.AppEnvironment;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.dto.user.User;
import org.joing.pde.media.PDEColorSchema;
import org.joing.pde.PDEManager;
import org.joing.pde.PDEUtilities;
import org.joing.pde.apps.EditUser;
import org.joing.pde.swing.JScrollablePopupMenu;

/**
 *
 * @author fmorero
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
            
            item.setIcon( new ImageIcon( PDEUtilities.getDesktopManager().getRuntime().getImage( image, ICON_SIZE+5, ICON_SIZE+5 ) ) );
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
                  itmLock.setIcon( PDEUtilities.getIcon( this, "images/lock", ICON_SIZE, ICON_SIZE ) );
                  itmLock.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          ((PDEManager) PDEUtilities.getDesktopManager()).lock();
                      }
                  } );
        add( itmLock );
    }
    
    private void addExit()
    {
        JMenuItem itmExit = new JMenuItem( "End session" );
                  itmExit.setIcon( PDEUtilities.getIcon( this, "images/exit", ICON_SIZE, ICON_SIZE ) );
                  itmExit.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          ((PDEManager) PDEUtilities.getDesktopManager()).exit();
                      }
                  } );
        add( itmExit );
    }
    
    private void addApplications()
    {
        List<AppGroup> lstGroups = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getAppBridge().
                                              getInstalledForUser( AppEnvironment.JAVA_ALL, AppGroupKey.ALL );
                                              
        if( lstGroups != null )
        {
            // Having the same instance for all app items saves memory
            AppMenuItemListener apil = new AppMenuItemListener();
            
            for( AppGroup group : lstGroups )
            {
                if( group.getGroupKey() == AppGroupKey.DESKTOP )    // Don't show "Desktops" apps
                    break;
                
                JMenu menu = new JMenu( group.getName() );
                      menu.setIcon( createItemIcon( group.getIconPNG() ) );
                      
                add( menu );

                List<AppDescriptor> appList = group.getApplications();

                for( AppDescriptor appDesc : appList )
                {
                    JMenuItem itemApp = new JMenuItem( appDesc.getName() );
                              itemApp.setIcon( createItemIcon( appDesc.getPNGIcon() ) );
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
    // INNER CLASS: App menu item action listener
    // To have only one instance for all app items
    //------------------------------------------------------------------------//
    private final class AppMenuItemListener implements ActionListener
    {
        public void actionPerformed( ActionEvent ae )
        {
            JMenuItem item = (JMenuItem) ae.getSource();
            AppDescriptor appDesc = (AppDescriptor) item.getClientProperty( KEY_APP_DESCRIPTOR );

            PDEUtilities.launch( DeskLauncher.Type.APPLICATION, String.valueOf( appDesc.getId() ) );   
        }
    }
}