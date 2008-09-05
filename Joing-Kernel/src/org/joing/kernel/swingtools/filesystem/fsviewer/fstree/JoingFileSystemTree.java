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

package org.joing.kernel.swingtools.filesystem.fsviewer.fstree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.joing.kernel.swingtools.JoingSwingTree;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.runtime.vfs.VFSFile;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionable;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionableCommonActions;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionableListener;

/**
 * To preservate independency of classes this one knows only to represent files
 * and FileSystemViewerControler knows only about about files and file systems.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemTree extends JoingSwingTree implements FileSystemActionable
{
    private boolean bShowFiles  = false;
    private boolean bShowHidden = false;
    
    private FileSystemActionableCommonActions fsaca = new FileSystemActionableCommonActions();
    
    private JPopupMenu popup;     // See this::setComponentPopupMenu(...)
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemTree()
    {
        setEditable( true );
        setRootVisible( false );    // Better not to make it visible
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( new TreeNodeFile() ) );  // root is the only node that does not have a File as user-object
        addTreeExpansionListener( new MyTreeExpansionListener() );
        addMouseListener( new MouseListener()
        {
            public void mousePressed( MouseEvent me )
            {
                if( me.isPopupTrigger() )
                    showPopup( me );
            }
            
            public void mouseClicked(  MouseEvent me )
            {
                if( JoingFileSystemTree.this.isShowingFiles() )
                {
                    if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 )
                    {
                        File fSelected = getSelected();    // Checked now because can be a little slow
                        
                        if( fSelected != null && fSelected.isFile() )
                            JoingFileSystemTree.this.fsaca.openFile( fSelected );
                    }
                }
            }

            public void mouseReleased( MouseEvent me )   // Needed for Windows
            {
                if( me.isPopupTrigger() )
                    showPopup( me );
            }

            private void showPopup( MouseEvent me )
            {
                TreePath path = getPathForLocation( me.getX(), me.getY() );
                    
                if( (path != null) && (! isPathSelected( path )) )
                    setSelectionPath( path );

                if( JoingFileSystemTree.this.popup != null )
                    JoingFileSystemTree.this.popup.show( JoingFileSystemTree.this, me.getX(), me.getY() );
            }
            
            public void mouseEntered(  MouseEvent me )  {  }
            public void mouseExited(   MouseEvent me )  {  }
        } );
        
        // Add one node per each File System (local and remote) to root node
        SwingWorker sw = new SwingWorker<List<TreeNodeFile>,TreeNodeFile>()
        {            
            protected List<TreeNodeFile> doInBackground() throws Exception
            {
                File[]             afRoots  = JoingFileSystemView.getFileSystemView().getRoots();
                List<TreeNodeFile> lstRoots = new ArrayList<TreeNodeFile>( afRoots.length );
                        
                for( File fRoot : afRoots )
                {
                    lstRoots.add( new TreeNodeFile( fRoot ) );
                    publish( lstRoots.get( lstRoots.size() - 1 ) );
                }
                
                return lstRoots;
            }
            
            protected void process( List<TreeNodeFile> lstNodes )
            {
                DefaultTreeModel model = (DefaultTreeModel) JoingFileSystemTree.this.getModel();
                TreeNodeFile     root  = (TreeNodeFile) model.getRoot();
                
                for( TreeNodeFile node : lstNodes )
                    model.insertNodeInto( node, root, root.getChildCount() );
            }
            
            protected void done()
            {
                TreeNodeFile root = (TreeNodeFile) JoingFileSystemTree.this.getModel().getRoot();
                JoingFileSystemTree.this.setSelected( (TreeNodeFile) root.getChildAt( 0 ) );
            }
        };
        sw.execute();
    }
    
    /**
     * It is needed to overwrite this method and handle isPopupTrigger() in a 
     * MouseListener: if we allow Java to handle the popup event (via 
     * setComponentPopupMenu(...)), then the MouseListener will <u>not</u> 
     * respond to right-click events.
     * <p>
     * Perhaps this is Sun's desired functionality or perhaps a bug.
     * 
     * @param popup A popup
     */
    public void setComponentPopupMenu( JPopupMenu popup )
    {
        this.popup = popup;
    }
    
    //------------------------------------------------------------------------//
    // Interface FileSystemActionable Implementation
    
    public void setShowingFiles( boolean bShowFiles )
    {
        this.bShowFiles = bShowFiles;
    }
    
    public boolean isShowingFiles()
    {
        return bShowFiles;
    }

    public boolean isShowingHidden()
    {
        return bShowHidden;
    }

    public void setShowingHidden( boolean bShowHidden )
    {
        this.bShowHidden = bShowHidden;
    }
    
    public File getSelected()
    {
        TreeNodeFile node = getFirstSelectedNode();
        
        return (node == null ? null : node.getFile());
    }
    
    public void setSelected( File file )
    {
        if( file != null && file.exists() &&
            (! file.equals( getSelected() )) )   // Works for dirs and files (in case bShowFiles == true)
        {
            // Creates a List with one dir per element.
            // Example: /home/fco/music/song.mp3 --> { /home/fco/music, /home/fco, /home, / } (file is ignored)
            ArrayList<File> lstPath = new ArrayList<File>( 16 );
            File            fParent = null;
            
            if( file.isDirectory() )     // Files are not inserted in the list
                lstPath.add( file );
            
            fParent = file.getParentFile();
            
            while( fParent != null )
            {
                lstPath.add( fParent );
                fParent = fParent.getParentFile();
            }
            
            // Check all elements, loading those that are not yet in the tree
            TreeNodeFile nodeParent = (TreeNodeFile) search( (TreeNodeFile) getModel().getRoot(), 
                                                             lstPath.get( lstPath.size() - 1 ) );
            
            for( int n = lstPath.size() - 2; n >= 0; n-- )    // - 2 because I just got root (nodeParent)
            {
                TreeNodeFile node = (TreeNodeFile) search( nodeParent, lstPath.get( n ) );
                
                if( node == null )   // Load children for parent node
                {
                    File[] afChildren = fsaca.getFilesIn( (File) nodeParent.getUserObject(), 
                                                          isShowingFiles(), isShowingHidden() );
                    // FIXME meter esto en un invokelater
                    for( int x = 0; x < afChildren.length; x++ )
                        ((DefaultTreeModel) getModel()).insertNodeInto( new TreeNodeFile( afChildren[x] ), nodeParent, x );
                }
                
                // Now returned node can't be null and node becomes nodeParent
                nodeParent = (TreeNodeFile) search( nodeParent, lstPath.get( n ) );
            }
            
            // Now it is guaranteed that all needed nodes exists in the tree
            setSelected( nodeParent );
        }
    }
    
    public void newFolder()
    {
        File fNewFolder = fsaca.createFolder( getFirstSelectedNode().getFile() );
        
        if( fNewFolder != null && 
            (! fNewFolder.isHidden() || isShowingHidden()) )   // Check fo hidden
        {
            add( new TreeNodeFile( fNewFolder ) );
        }
    }
    
    public void newFile()
    {
        File fNewFile = fsaca.createFile( getFirstSelectedNode().getFile() );
        
        if( fNewFile != null )
        {
            fireValueChanged( new TreeSelectionEvent( this, null, false, null, getSelectionPath() ) );  // To force List re-scan
            
            if( (! fNewFile.isHidden()  || isShowingHidden()) &&   // Check for hidden
                (fNewFile.isDirectory() || isShowingFiles()) )     // Check for files
            {
                add( new TreeNodeFile( fNewFile ) );
            }
        }
    }
    
    public void delete()
    {// NEXT: Si son varios ficheros, habría que enviarlos todos a la vez (por si son VFSs)
        boolean bAll = true;   // Where all selected files deleted?
        
        TreePath[] aPath = getSelectionPaths();

        for( int n = 0; n < aPath.length; n++ )
        {
            TreeNodeFile node = (TreeNodeFile) aPath[n].getLastPathComponent();

            if( fsaca.delete( node.getFile() ) )
                delete( node );
            else
                bAll = false;
        }
        
        if( ! bAll )
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Error deleteing one or more files." );
    }
    
    public void rename()
    {
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Rename: Option not yet implemented" );
        ///startEditingAtPath( getSelectionPath() );
    }
    
    public void properties()
    {
        fsaca.properties( getFirstSelectedNode().getFile() );
    }
    
    public void cut()
    {
        TreePath[] aPath = getSelectionPaths();
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Cut: Option not yet implemented" );
    }
    
    public void copy()
    {
        TreePath[] node = getSelectionPaths();
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Copy: Option not yet implemented" );
    }
    
    public void paste()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Paste: Option not yet implemented" );
    }
    
    public void toTrascan()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "To trashcan: Option not yet implemented" );
    }
    
    // TODO: Notificar los eventos a los listeners
    public void addListener( FileSystemActionableListener fsal )
    {
        listenerList.add( FileSystemActionableListener.class, fsal );
    }
    
    public void removeListener( FileSystemActionableListener fsal )
    {
        listenerList.remove( FileSystemActionableListener.class, fsal );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * If more than one node is selected this returns the first one and makes
     * the rest selected nodes unselected.
     * 
     * @return The first selected node.
     */
    private TreeNodeFile getFirstSelectedNode()
    {
        TreePath path = getSelectionPath();
        
        if( getSelectionCount() > 1 )    // There is more than one node selected
        {
            clearSelection();
            setSelectionPath( path );
        }
        
        return (path == null ? null : (TreeNodeFile) path.getLastPathComponent());
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyTreeExpansionListener
    //------------------------------------------------------------------------//
    private final class MyTreeExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded( TreeExpansionEvent tee )
        {
            TreeNodeFile node = (TreeNodeFile) tee.getPath().getLastPathComponent();

            if( node.getAllowsChildren() && 
                (node.getChildCount() == 0 || ((TreeNodeFile) node.getChildAt( 0 )).isFakedNode()) )
            {
                DefaultTreeModel model      = (DefaultTreeModel) getModel();
                File[]           afChildren = fsaca.getFilesIn( (File) node.getUserObject(), isShowingFiles(), isShowingHidden() );
                
                // Deletes faked node (the one used to force JTree to show handles)
                if( node.getChildCount() == 1 )  // Must be the faked node
                    model.removeNodeFromParent( (TreeNodeFile) node.getChildAt( 0 ) );
                
                for( int n = 0; n < afChildren.length; n++ )
                {
                    File f = afChildren[n];
                    model.insertNodeInto( new TreeNodeFile( f ), node, n );
                }
            }
        }
        
        public void treeCollapsed( TreeExpansionEvent tee )
        {
            TreeNodeFile node = (TreeNodeFile) tee.getPath().getLastPathComponent();
            
            // Local File System is very fast: removing childs is done to save memory
            if( ! (node.getUserObject() instanceof VFSFile) )
            {// NEXT: Cuando exista caché se pueden eliminar todos: locales y remotos
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                
                while( node.getChildCount() > 0 )
                    model.removeNodeFromParent( (TreeNodeFile) node.getFirstChild() );
                
                // Inserts a faked node (used to force JTree to show handles)
                model.insertNodeInto( new TreeNodeFile( true ), node, 0 );
            }
        }
    }
}