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
package org.joing.pde.apps.YAFE;

import javax.swing.event.TreeSelectionEvent;
import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.pde.joingswingtools.tree.JoingFileSystemTree;
import org.joing.pde.joingswingtools.tree.TreeNodeFile;

/**
 * YAFE: Yet Another File Explorer.
 * 
 * @author  Francisco Morero Peyrona
 */
public class YAFE extends javax.swing.JPanel implements DeskComponent
{
    private JoingFileSystemTree tree;
    private Table table;
    
    //------------------------------------------------------------------------//
    
    public YAFE()
    {
        tree  = new JoingFileSystemTree();
        tree.addTreeSelectionListener( new TreeSelectionListener() 
        {
            public void valueChanged( TreeSelectionEvent tse )
            {
                TreeNodeFile node = (TreeNodeFile) tse.getPath().getLastPathComponent();
                YAFE.this.lblBreadcrumb.setText( node.getFile().toString() );
            }
        } );
        
        table = new Table();
        
        initComponents();
        initToolBarAndPopupMenu();
        
        split.setLeftComponent( new JScrollPane( tree ) );
        split.setRightComponent( new JScrollPane( table ) );
        split.setResizeWeight( .3d );
        
        showInFrame();
    }
    
    //------------------------------------------------------------------------//
    
    private void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/yafe.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "File Explorer" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    private void initToolBarAndPopupMenu()
    {
        Action[]   actions = getActions();
        JPopupMenu popup   = new JPopupMenu();
        
        
        for( int n = 0; n < actions.length; n++ )
        {
            if( actions[n] == null )
            {
                toolbar.add( new JToolBar.Separator() );
                popup.add( new JPopupMenu.Separator() );
            }
            else
            {
                toolbar.add( actions[n] );
                popup.add( actions[n] );
            }
        }
        
        tree.setComponentPopupMenu( popup );
    }
    
    private Action[] getActions()
    {
        ImageIcon iconHome   = new ImageIcon( getClass().getResource( "images/home.png" ) );
        ImageIcon iconLoad   = new ImageIcon( getClass().getResource( "images/reload.png" ));
        ImageIcon iconRename = new ImageIcon( getClass().getResource( "images/rename.png" ));
        
        AbstractAction actHome = new MyAction( "Home", iconHome.getImage(), 'H' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                DefaultMutableTreeNode node = tree.search( null, new File( System.getProperty( "user.home" ) ) );
                
                if( node != null )
                    tree.setSelected( node );
            }
        };
        
        AbstractAction actReload = new MyAction( "Reload", iconLoad.getImage(), 'L', "Reload current node [Ctrl-L] (with Shift, whole tree)" )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( (ae.getModifiers() & ActionEvent.SHIFT_MASK) != 0 )
                    tree.reloadAll();
                else
                    tree.reloadSelected();
            }
        };
        
        AbstractAction actNewDir = new MyAction( "New folder", StandardImage.FOLDER, 'F' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                try
                {
                    tree.createDir();
                }
                catch( IOException exc )
                {
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
                }
            }
        };
        
        AbstractAction actNewFile = new MyAction( "New file", StandardImage.NEW, 'I' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.createFile();
            }
        };
        
        AbstractAction actCut = new MyAction( "Cut", StandardImage.CUT, 'X' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.cut();
            }
        };

        AbstractAction actCopy = new MyAction( "Copy", StandardImage.COPY, 'C' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                tree.copy();
            }
        };
        
        AbstractAction actPaste = new MyAction( "Paste", StandardImage.PASTE, 'V' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.paste() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error pasteing", "Can't paste one or more selected entities." );
            }
        };
        
        AbstractAction actRename = new MyAction( "Rename", iconRename.getImage(), 'R' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.rename() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error renaming", "Can't rename selected entity." );
            }
        };
        
        AbstractAction actDelete = new MyAction( "Delete", StandardImage.TRASHCAN, 'D' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.deleteAllSelected() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error deleting", "Can't delete one or more selected files or directories." );
            }
        };
        
        AbstractAction actProperties = new MyAction( "Properties", StandardImage.PROPERTIES, 'P' )
        {
            public void actionPerformed( ActionEvent ae )
            {
                if( ! tree.rename() )
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
                            "Error editing properties", "Can't edit properties for selected entity." );
            }
        };
        
        return (new Action[] { actHome, actReload, null, actNewDir, actNewFile, null, 
                              actCut, actCopy, actPaste, null, actRename, actProperties, null, actDelete });
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private abstract class MyAction extends AbstractAction
    {
        private MyAction( String sText, StandardImage img, char character )
        {
            this( sText, 
                  org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( img ),
                  character );
        }

        private MyAction( String sText, Image image, char character )
        {
            this( sText, image, character, null );
        }

        private MyAction( String sText, Image image, char character, String sToolTip )
        {
            super( sText );
            
            KeyStroke stroke   = KeyStroke.getKeyStroke( character, Event.CTRL_MASK );
            
            if( sToolTip == null )
                sToolTip = sText +" [Ctrl-"+ Character.toString( character ) +"]";
            
            putValue( SMALL_ICON       , new ImageIcon( image.getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) ) );
            putValue( LARGE_ICON_KEY   , new ImageIcon( image.getScaledInstance( 24, 24, Image.SCALE_SMOOTH ) ) );
            putValue( ACCELERATOR_KEY  , stroke   );
            putValue( TOOL_TIP_TEXT_KEY, sToolTip );
            putValue( SHORT_DESCRIPTION, sToolTip );
            putValue( LONG_DESCRIPTION , sToolTip );
        }
    }
    
    //------------------------------------------------------------------------//
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        split = new javax.swing.JSplitPane();
        pnlBreadcrumb = new javax.swing.JPanel();
        lblBreadcrumb = new javax.swing.JLabel();
        toolbar = new javax.swing.JToolBar();

        pnlBreadcrumb.setBackground(new java.awt.Color(253, 253, 222));

        javax.swing.GroupLayout pnlBreadcrumbLayout = new javax.swing.GroupLayout(pnlBreadcrumb);
        pnlBreadcrumb.setLayout(pnlBreadcrumbLayout);
        pnlBreadcrumbLayout.setHorizontalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        pnlBreadcrumbLayout.setVerticalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        toolbar.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
            .addComponent(pnlBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBreadcrumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblBreadcrumb;
    private javax.swing.JPanel pnlBreadcrumb;
    private javax.swing.JSplitPane split;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables
}