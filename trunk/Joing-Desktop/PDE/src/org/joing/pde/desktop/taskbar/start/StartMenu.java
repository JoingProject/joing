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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.joing.pde.runtime.ColorSchema;
import org.joing.pde.runtime.PDERuntime;
import org.joing.pde.swing.JScrollablePopupMenu;
import org.joing.pde.utils.EditUser;

/**
 *
 * @author fmorero
 */
class StartMenu extends JScrollablePopupMenu
{
    private final static int ICON_SIZE = 22;
    
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

        User user = PDERuntime.getRuntime().getBridge().getUserBridge().getUser();
        
        if( user != null )
        {
            String sIcon = "images/user_"+ (user.isMale() ? "" : "fe") +"male.png";
            
            item.setIcon( PDERuntime.getRuntime().getIcon( this, sIcon, ICON_SIZE+5, ICON_SIZE+5 ) );
            item.setText( user.getFirstName() +" "+ user.getSecondName() );

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
        JMenuItem itmLock = new JMenuItem( "Lock session" );
                  itmLock.setIcon( PDERuntime.getRuntime().getIcon( this, "images/lock.png", ICON_SIZE, ICON_SIZE ) );
                  itmLock.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          PDERuntime.getRuntime().getDesktopManager().lock();
                      }
                  } );
        add( itmLock );
    }
    
    private void addExit()
    {
        JMenuItem itmExit = new JMenuItem( "End session" );
                  itmExit.setIcon( PDERuntime.getRuntime().getIcon( this, "images/exit.png", ICON_SIZE, ICON_SIZE ) );
                  itmExit.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          PDERuntime.getRuntime().getDesktopManager().close();
                      }
                  } );
        add( itmExit );
    }
    
    private void addApplications()
    {
        final String KEY = "JOING_APP_DESCRIPTOR";
        
        List<AppsByGroup> abgList = PDERuntime.getRuntime().getBridge().getAppBridge().getInstalledForUser();
        
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
            
            // TODO: hacerlo bien: con IDs para las categorías
            if( menu.getText().equals( "Accesorios" ) )
                addSystemMonitor( menu );
        }
    }
    
    private void addSystemMonitor( JMenu menu )
    {
        JMenuItem itmSysMon = new JMenuItem( "System monitor" );
                  itmSysMon.setIcon( PDERuntime.getRuntime().getIcon( this, "images/system_monitor.png", ICON_SIZE, ICON_SIZE ) );
                  itmSysMon.addActionListener( new ActionListener()
                  {
                      public void actionPerformed( ActionEvent ae )
                      {
                          PDERuntime.getRuntime().showSystemMonitor();
                      }
                  } );
        
        menu.add( itmSysMon );
    }
    
    private ImageIcon createItemIcon( byte[] abImage )
    {
        ImageIcon icon = null;
        
        if( abImage != null )
        {
            icon = new ImageIcon( abImage );
            
            if( icon.getIconWidth() != ICON_SIZE || icon.getIconHeight() != ICON_SIZE )
                icon.setImage( icon.getImage().getScaledInstance( ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH ) );
        }
        
        return icon;
    }
}