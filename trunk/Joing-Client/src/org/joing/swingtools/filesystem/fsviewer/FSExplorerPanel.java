/* 
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.  * Join'g Team Members are listed at project's home page. By the time of   * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
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
package org.joing.swingtools.filesystem.fsviewer;

import java.awt.Insets;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.swingtools.JoingSwingUtilities;
import org.joing.swingtools.filesystem.fsviewer.FileSystemJobs;
import org.joing.swingtools.filesystem.fsviewer.fslist.JoingFileSystemList;
import org.joing.swingtools.filesystem.fsviewer.fstree.JoingFileSystemTree;

/**
 * YAFE: Yet Another File Explorer.
 * 
 * @author  Francisco Morero Peyrona
 */
public class FSExplorerPanel extends javax.swing.JPanel implements DeskComponent
{
    private JoingFileSystemTree tree;
    private JoingFileSystemList list;
    
    //------------------------------------------------------------------------//
    
    public FSExplorerPanel()
    {
        this( null );
    }
    
    public FSExplorerPanel( final File fOrigin )
    {
        initGUI();
        
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                list.setSelectedFile( fOrigin );    // FIXME: Pasarlo al tree y que sincronize el list
            }
        } );
    }
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/fs_explorer.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "File Explorer" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) FSExplorerPanel.this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    //------------------------------------------------------------------------//
    
    private void initToolBar( FileSystemJobs controler )
    {
        AbstractAction[] actions = controler.getActions();
        
        for( int n = 0; n < actions.length; n++ )
        {
            if( actions[n] == null )
                toolbar.add( new JToolBar.Separator() );
            else
                toolbar.add( actions[n] );
        }
    }
    
    private void initGUI()
    {
        FileSystemJobs controler = new FileSystemJobs();
        
        tree  = new JoingFileSystemTree();
        tree.setFileSystemWorks( controler );
        tree.addTreeSelectionListener( new TreeSelectionListener() 
        {
            public void valueChanged( TreeSelectionEvent tse )
            {
                File f = tree.getSelectedFile();
                
                if( f != null )
                {
                    FSExplorerPanel.this.txtBreadcrumb.setText( f.getAbsolutePath() );
                    FSExplorerPanel.this.list.setSelectedFile( f );
                }
            }
        }  );
        
        list = new JoingFileSystemList();
        list.setFileSystemWorks( controler );
        list.addListSelectionListener( new ListSelectionListener() 
        {
            public void valueChanged( ListSelectionEvent lse )
            {
                File f = list.getSelectedFile();
                
                if( f != null )
                    FSExplorerPanel.this.txtBreadcrumb.setText( f.getAbsolutePath() );
            }
        }  );
        
        initComponents();
        initToolBar( controler );
        
        btnGoto.setIcon( JoingSwingUtilities.getIcon( this, "images/goto.png", 18,18 ) );
        btnGoto.setMargin( new Insets( 2,2,2,2 ) );
        
        split.setLeftComponent( new JScrollPane( tree ) );
        split.setRightComponent( new JScrollPane( list ) );
        split.setDividerLocation( 180 );
        split.setResizeWeight( .30d );
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
        toolbar = new javax.swing.JToolBar();
        txtBreadcrumb = new javax.swing.JTextField();
        btnGoto = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));

        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);

        toolbar.setRollover(true);

        txtBreadcrumb.setFont(txtBreadcrumb.getFont().deriveFont(txtBreadcrumb.getFont().getSize()+1f));
        txtBreadcrumb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBreadcrumActionPerformed(evt);
            }
        });

        btnGoto.setMaximumSize(new java.awt.Dimension(28, 28));
        btnGoto.setMinimumSize(new java.awt.Dimension(22, 22));
        btnGoto.setPreferredSize(new java.awt.Dimension(24, 24));
        btnGoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(txtBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBreadcrumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnGotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGotoActionPerformed
    String sPath = txtBreadcrumb.getText();
    
    if( sPath.length() > 0 )
        tree.setSelectedFile( new File( sPath ) );
}//GEN-LAST:event_btnGotoActionPerformed

private void txtBreadcrumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBreadcrumActionPerformed
    btnGotoActionPerformed( null );
}//GEN-LAST:event_txtBreadcrumActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGoto;
    private javax.swing.JSplitPane split;
    private javax.swing.JToolBar toolbar;
    private javax.swing.JTextField txtBreadcrumb;
    // End of variables declaration//GEN-END:variables
}