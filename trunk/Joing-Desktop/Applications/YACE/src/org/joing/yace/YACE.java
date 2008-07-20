/* 
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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
package org.joing.yace;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 * YACE: Yet Another Community Explorer.
 * 
 * @author Francisco Morero Peyrona
 */
public class YACE extends javax.swing.JPanel implements DeskComponent
{
    private JoingCommunityEditable tree;
    
    /** Creates new form YACE */
    public YACE()
    {
        tree = new JoingCommunityEditable();
        
        initComponents();
        initToolBarsAndPopupMenu();
        
        spTree.getViewport().add( tree );
        
        showInFrame();
    }
    
    //------------------------------------------------------------------------//
    
    public static void main( String[] args )
    {
        new YACE();
    }
    
    private void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/yace.png" ) );
        
        if( dm == null )
        {
            javax.swing.JFrame frame = new javax.swing.JFrame();
                               frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                               frame.add( this );
                               frame.pack();
                               frame.setVisible( true );
        }
        else
        {
            // Show this panel in a frame created by DesktopManager Runtime.
            DeskFrame frame = dm.getRuntime().createFrame();
                      frame.setTitle( "Community Explorer (Mockup)" );
                      frame.setIcon( icon.getImage() );
                      frame.add( (DeskComponent) this );
                      
            dm.getDesktop().getActiveWorkArea().add( frame );
        }
    }
    
    private void initToolBarsAndPopupMenu()
    {
        Action[]   aActComm = getCommActions();
        Action[]   aActUser = getUserActions();
        JPopupMenu popup    = new JPopupMenu();
        
        for( int n = 0; n < aActComm.length; n++ )
        {
            tbComm.add( aActComm[n] );
            popup.add(  aActComm[n] );
        }
        
        popup.add( new JPopupMenu.Separator() );
        
        for( int n = 0; n < aActUser.length; n++ )
        {
            if( aActUser[n] == null )
            {
                tbUsers.add( new JToolBar.Separator() );
                popup.add( new JPopupMenu.Separator() );
            }
            else
            {
                tbUsers.add( aActUser[n] );
                popup.add(   aActUser[n] );
            }
        }
        
        tree.setComponentPopupMenu( popup );
    }
    
    private Action[] getCommActions()
    {
        AbstractAction actChat = new MyAction( "Chat", "act_chat" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
         AbstractAction actTalk = new MyAction( "Talk", "act_talk" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actVideo = new MyAction( "Video", "act_video" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction act3DWorld = new MyAction( "3D world", "act_3D" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actSendFile = new MyAction( "Send file", "act_send_file" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actShareApp = new MyAction( "Share app", "act_share_app" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        return (new Action[] { actChat, actTalk, actVideo, act3DWorld, actSendFile, actShareApp });
    }
            
    private Action[] getUserActions()
    {
        AbstractAction actGroupNew = new MyAction( "New group", "group_add" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actGroupDelete = new MyAction( "Delete group", "group_delete" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actGroupEdit = new MyAction( "Edit group", "group_edit" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actUserNew = new MyAction( "New user", "user_add" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actUserDelete = new MyAction( "Delete user", "user_delete" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actUserEdit = new MyAction( "Edit user", "user_edit" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        AbstractAction actUserInfo = new MyAction( "User info", "user_info" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                javax.swing.JOptionPane.showMessageDialog( null, "Option not yet implemented" );
            }
        };
        
        return (new Action[] { actGroupNew, actGroupEdit, actGroupDelete, null,
                               actUserNew, actUserEdit, actUserInfo, actUserDelete });
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private abstract class MyAction extends AbstractAction
    {        
        private MyAction( String sText, String sIconName )
        {
            super( sText );
            
            Image image = new ImageIcon( getClass().getResource( "images/"+ sIconName +".png" ) ).getImage();
                    
            putValue( SMALL_ICON, new ImageIcon( image.getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) ) );
            putValue( LARGE_ICON_KEY, new ImageIcon( image.getScaledInstance( 24, 24, Image.SCALE_SMOOTH ) ) );
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTree = new javax.swing.JScrollPane();
        tbComm = new javax.swing.JToolBar();
        tbUsers = new javax.swing.JToolBar();

        tbComm.setRollover(true);

        tbUsers.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbComm, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
            .addComponent(tbUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
            .addComponent(spTree, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbComm, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spTree, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane spTree;
    private javax.swing.JToolBar tbComm;
    private javax.swing.JToolBar tbUsers;
    // End of variables declaration//GEN-END:variables
}