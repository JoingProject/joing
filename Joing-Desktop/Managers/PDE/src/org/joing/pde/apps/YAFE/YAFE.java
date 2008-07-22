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

import javax.swing.AbstractAction;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;
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
        split.setResizeWeight( .4d );
        
        showInFrame();
    }
    
    //------------------------------------------------------------------------//
    
    private void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "yafe.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "File Explorer" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    private void initToolBarAndPopupMenu()
    {
        AbstractAction[] actions = tree.getActions();
        JPopupMenu       popup   = new JPopupMenu();
        
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

        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);

        pnlBreadcrumb.setBackground(new java.awt.Color(253, 253, 222));

        javax.swing.GroupLayout pnlBreadcrumbLayout = new javax.swing.GroupLayout(pnlBreadcrumb);
        pnlBreadcrumb.setLayout(pnlBreadcrumbLayout);
        pnlBreadcrumbLayout.setHorizontalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        pnlBreadcrumbLayout.setVerticalGroup(
            pnlBreadcrumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
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
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblBreadcrumb;
    private javax.swing.JPanel pnlBreadcrumb;
    private javax.swing.JSplitPane split;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables
}