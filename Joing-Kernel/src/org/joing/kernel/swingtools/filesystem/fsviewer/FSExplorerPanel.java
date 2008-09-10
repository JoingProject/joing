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

package org.joing.kernel.swingtools.filesystem.fsviewer;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.kernel.swingtools.JoingSwingUtilities;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActions;
import org.joing.kernel.swingtools.filesystem.fsviewer.fslist.JoingFileSystemList;
import org.joing.kernel.swingtools.filesystem.fsviewer.fstree.JoingFileSystemTree;

/**
 * YAFE: Yet Another File Explorer.
 * 
 * @author  Francisco Morero Peyrona
 */
public class FSExplorerPanel extends javax.swing.JPanel implements DeskComponent
{
    private ThisToolBar         toolbar;
    private JoingFileSystemTree tree;
    private JoingFileSystemList list;
    
    protected FileSystemActions fsActions = new FileSystemActions();
    
    //------------------------------------------------------------------------//
    
    public FSExplorerPanel()
    {
        initGUI();
    }
    
    public FSExplorerPanel( final File fSelected )
    {
        if( fSelected == null )
            throw new NullPointerException( "Passed file can't be null" );
        
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                initGUI();
                FSExplorerPanel.this.tree.setSelected( fSelected );
            }
        } );
    }
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/fs_explorer.png" ) );
        
        // Show this panel in a frame created by DesktopManager Runtime.
        DeskFrame frame = dm.getRuntime().createFrame();
                  frame.setTitle( "YAFE :: File Explorer" );
                  frame.setIcon( icon.getImage() );
                  frame.add( (DeskComponent) FSExplorerPanel.this );

        dm.getDesktop().getActiveWorkArea().add( frame );
    }
    
    //------------------------------------------------------------------------//
    
    private void initGUI()
    {
        AbstractAction[] actions = fsActions.getActions();
        JPopupMenu       popup   = new JPopupMenu();
        
        initComponents();
        
        toolbar = new ThisToolBar();
        
        // Init toolbar for main panel and popup for tree and list panels
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
        
        pnl4ToolBar.setLayout( new BorderLayout() );
        pnl4ToolBar.add( toolbar, BorderLayout.CENTER );
        
        // Init tree
        tree  = new JoingFileSystemTree();
        tree.setComponentPopupMenu( popup );
        tree.addTreeSelectionListener( new TreeSelectionListener() 
        {
            public void valueChanged( TreeSelectionEvent tse )
            {
                File f = tree.getSelected();
                
                if( f != null )
                {
                    FSExplorerPanel.this.fsActions.updateActionsEnableStatus( f );
                    FSExplorerPanel.this.txtBreadcrumb.setText( f.getAbsolutePath() );
                    FSExplorerPanel.this.list.setSelected( f );
                }
            }
        } );
        
        // Init list panel
        list = new JoingFileSystemList();
        list.setComponentPopupMenu( popup );
        list.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( ListSelectionEvent lse )
            {
                File f = FSExplorerPanel.this.list.getSelected();
                
                if( f != null )
                {
                    FSExplorerPanel.this.fsActions.updateActionsEnableStatus( f );
                    FSExplorerPanel.this.txtBreadcrumb.setText( f.getAbsolutePath() );
                    // Selection changes are not done from here but from next Listener
                }
            }
        } );
        list.addListener( new FileSystemActionableListener()
        {
            public void selectionChanged( File fSelected )      {  }
            public void folderCreated( File fCreated )          {  }
            public void fileCreated( File fCreated )            {  }
            public void cutted( List<File> cutted )             {  }
            public void copied( List<File> copied )             {  }
            public void pasted( List<File> pasted )             {  }
            public void deleted( List<File> deleted )           {  }
            public void movedToTrashcan( List<File> trashcan )  {  }
            public void renamed( File fRenamed )                {  }
            public void propertiesChanged( File fChanged )      {  }
            public void open( File fToOpen )
            {
                if( fToOpen.isDirectory() )
                    FSExplorerPanel.this.tree.setSelected( fToOpen );
            }
        } );
        
        // Init Goto button
        btnGoto.setIcon( JoingSwingUtilities.getIcon( this, "images/goto.png", 18,18 ) );
        btnGoto.setMargin( new Insets( 2,2,2,2 ) );
                
        // Init split panel
        split.setLeftComponent( new JScrollPane( tree ) );
        split.setRightComponent( new JScrollPane( list ) );
        split.setDividerLocation( 180 );
        split.setResizeWeight( .30d );
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: TOOLBAR
    //------------------------------------------------------------------------//
    private final class ThisToolBar extends JToolBar implements FileSystemActionable 
    {
        private FileSystemActionableDelegated fsaca = new FileSystemActionableDelegated();
        
        private ThisToolBar()
        {
            setRollover( true );
        }
        
        public void setSelected( File f )  { tree.setSelected( f );        }   // Must go against tree
        public void reloadAll()            { getTarget().reloadAll();      }
        public void reloadSelected()       { getTarget().reloadSelected(); }
        public void newFolder()            { getTarget().newFolder();      }
        public void newFile()              { getTarget().newFile();        }
        public void cut()                  { getTarget().cut();            }
        public void copy()                 { getTarget().copy();           }
        public void paste()                { getTarget().paste();          }
        public void delete()               { getTarget().delete();         }
        public void toTrascan()            { getTarget().toTrascan();      }
        public void rename()               { getTarget().rename();         }
        public void properties()           { getTarget().properties();     }
        
        // Following methods of interface are not used by ThisToolBar
        public void setShowingFiles( boolean bShowFiles )                { }
        public boolean isShowingFiles()                                  { return false; }
        public void setShowingHidden( boolean bShowHidden )              { }
        public boolean isShowingHidden()                                 { return false; }
        public File getSelected()                                        { return null;  }
        public void addListener( FileSystemActionableListener fsal )     { }
        public void removeListener( FileSystemActionableListener fsal )  { }
        
        // Return the target to which resend the action: tree or list
        private FileSystemActionable getTarget()
        {
            return list;  // TODO: hacerlo: buscar cual fue el último ctrl que se usó nates de haecr clic en la toolbar
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

        split = new javax.swing.JSplitPane();
        txtBreadcrumb = new javax.swing.JTextField();
        btnGoto = new javax.swing.JButton();
        pnl4ToolBar = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));

        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);

        txtBreadcrumb.setFont(txtBreadcrumb.getFont().deriveFont(txtBreadcrumb.getFont().getSize()+1f));
        txtBreadcrumb.setPreferredSize(new java.awt.Dimension(10, 24));
        txtBreadcrumb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBreadcrumActionPerformed(evt);
            }
        });

        btnGoto.setPreferredSize(new java.awt.Dimension(32, 32));
        btnGoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGotoActionPerformed(evt);
            }
        });

        pnl4ToolBar.setBorder(null);
        pnl4ToolBar.setFocusable(false);
        pnl4ToolBar.setMinimumSize(new java.awt.Dimension(100, 36));
        pnl4ToolBar.setPreferredSize(new java.awt.Dimension(100, 42));

        javax.swing.GroupLayout pnl4ToolBarLayout = new javax.swing.GroupLayout(pnl4ToolBar);
        pnl4ToolBar.setLayout(pnl4ToolBarLayout);
        pnl4ToolBarLayout.setHorizontalGroup(
            pnl4ToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
        );
        pnl4ToolBarLayout.setVerticalGroup(
            pnl4ToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl4ToolBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(txtBreadcrumb, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGoto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl4ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGoto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBreadcrumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnGoto, txtBreadcrumb});

    }// </editor-fold>//GEN-END:initComponents

private void btnGotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGotoActionPerformed
    String sPath = txtBreadcrumb.getText();
    
    if( sPath.length() > 0 )
        tree.setSelected( new File( sPath ) );
}//GEN-LAST:event_btnGotoActionPerformed

private void txtBreadcrumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBreadcrumActionPerformed
    btnGotoActionPerformed( null );
}//GEN-LAST:event_txtBreadcrumActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGoto;
    private javax.swing.JPanel pnl4ToolBar;
    private javax.swing.JSplitPane split;
    private javax.swing.JTextField txtBreadcrumb;
    // End of variables declaration//GEN-END:variables
}