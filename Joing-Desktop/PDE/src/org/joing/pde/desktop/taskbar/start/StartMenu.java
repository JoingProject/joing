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

import ejb.app.AppDescriptor;
import ejb.app.AppsByGroup;
import ejb.user.User;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.jvmm.Platform;
import org.joing.pde.PDEManager;
import org.joing.pde.runtime.ColorSchema;
import org.joing.pde.runtime.PDERuntime;
import org.joing.pde.swing.JScrollablePopupMenu;
import org.joing.pde.utils.EditUser;
import org.joing.runtime.bridge2server.Bridge2Server;

/**
 *
 * @author fmorero
 */
class StartMenu extends JScrollablePopupMenu
{
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
                  item.setBackground( ColorSchema.getInstance().getUserNameBackground() );
                  item.setForeground( ColorSchema.getInstance().getUserNameForeground() );
                  item.setBorder( new EmptyBorder( 4,4,4,4 ) );
                  item.setFont( item.getFont().deriveFont( Font.BOLD, item.getFont().getSize() + 4 ) );

        User user = Platform.getInstance().getBridge().getUserBridge().getUser();
        
        if( user != null )
        {
            String sIcon = "images/user_"+ (user.isMale() ? "" : "fe") +"male.png";
            
            item.setIcon( PDERuntime.getRuntime().getIcon( this, sIcon, 22, 22 ) );
            item.setText(user.getFirstName() +" "+ user.getSecondName() );

            item.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ae )
                {
                    (new EditUser()).edit();
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
        JMenuItem item1 = new JMenuItem( "Lock session" );
                  item1.setIcon( PDERuntime.getRuntime().getIcon( this, "images/lock.png", 22, 22 ) );
                  item1.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          PDERuntime.getRuntime().getDesktopManager().lock();
                      }
                  } );
        add( item1 );
    }
    
    private void addExit()
    {
        JMenuItem item = new JMenuItem( "End session" );
                  item.setIcon( PDERuntime.getRuntime().getIcon( this, "images/exit.png", 22, 22 ) );
                  item.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          PDERuntime.getRuntime().getDesktopManager().close();
                      }
                  } );
        add( item );
    }
    
    private void addApplications()
    {
        final String KEY = "JOING_APP_DESCRIPTOR";
        
        List<AppsByGroup> abgList = Platform.getInstance().getBridge().getAppBridge().getInstalledForUser();
        
        for( AppsByGroup abg : abgList )
        {
            JMenu menu = new JMenu( abg.getDescription() );
                  menu.setIcon( createItemIcon( abg.getIconPNG() ) );
               
            add( menu );
            
            List<AppDescriptor> appList = abg.getApplications();
            
            for( AppDescriptor appDesc : appList )
            {
                JMenuItem itemApp = new JMenuItem( appDesc.getName() );
                          itemApp.setIcon( createItemIcon( appDesc.getPNGIcon() ) );
                          itemApp.setToolTipText( appDesc.getDescription() );
                          itemApp.putClientProperty( KEY, appDesc );
                          itemApp.addActionListener( new ActionListener()
                          {
                              public void actionPerformed( ActionEvent ae )
                              {
                                  JMenuItem item = (JMenuItem) ae.getSource();
                                  AppDescriptor appDesc = (AppDescriptor) item.getClientProperty( KEY );
                                  
                                  // TODO: llamar a lo de Antonio
                              }
                          } );
                          
                menu.add( itemApp );
            }
        }
    }
        
    private ImageIcon createItemIcon( byte[] abImage )
    {
        ImageIcon icon = null;
        
        if( abImage != null )
        {
            icon = new ImageIcon( abImage );
            
            if( icon.getIconWidth() != 22 || icon.getIconHeight() != 22 )
                icon.setImage( icon.getImage().getScaledInstance( 22, 22, Image.SCALE_SMOOTH ) );
        }
        
        return icon;
    }
}