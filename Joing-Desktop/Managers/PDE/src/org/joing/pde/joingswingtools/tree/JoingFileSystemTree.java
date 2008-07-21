/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemTree extends JoingSwingTree
{
    private static final String sNewFile = "NewFile";
    
    private boolean bOnlyDirs;
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemTree()
    {
        bOnlyDirs = false;
        setRootVisible( false );
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( new TreeNodeFile() ) );  // root is the only node that does not have a File as user-object
        addTreeExpansionListener( new MyTreeExpansionListener() );
        
        // Add one node per each File Systems (local and remote) to root node
        SwingWorker sw = new SwingWorker()
        {
            protected Object doInBackground() throws Exception
            {
                JTree            tree    = JoingFileSystemTree.this;
                DefaultTreeModel model   = (DefaultTreeModel) tree.getModel();
                TreeNodeFile     root    = (TreeNodeFile) model.getRoot();
                File[]           afRoots = JoingFileSystemView.getFileSystemView().getRoots();
        
                for( int n = 0; n < afRoots.length; n++ )
                    model.insertNodeInto( new TreeNodeFile( afRoots[n], true ), root, root.getChildCount() );
                
                return null;
            }
        };
        sw.execute();
    }
    
    /**
     * To show only dirs or dirs and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bOnlyDirs
     */
    public void setOnlyDirs( boolean bOnlyDirs )
    {
        this.bOnlyDirs = bOnlyDirs;
    }
    
    /**
     * Is showing only dirs or also files?
     * 
     * @return <code>true</code> if is showing only dirs.
     */
    public boolean isOnlyDirs()
    {
        return bOnlyDirs;
    }
    
    //------------------------------------------------------------------------//
    // Editing actions
    
    /**
     * Create a new directory as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createDir() throws IOException
    {
        File         fDir = JoingFileSystemView.getFileSystemView().createNewFolder( (File) getSelectedNode().getUserObject() );
        TreeNodeFile node = new TreeNodeFile( fDir, true );
        
        add( node );
        
        return fDir;
    }
    
    /**
     * Create a new file as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createFile()
    {
        File fParent = (File) getSelectedNode().getUserObject();
        
        return JoingFileSystemView.getFileSystemView().createFileObject( fParent, sNewFile );
    }
    
    public boolean deleteAllSelected()
    {
        boolean    bAll  = true;   // Where all selected files deleted?
        TreePath[] aPath = getSelectionPaths();
        
        for( int n = 0; n < aPath.length; n++ )
        {
            TreeNodeFile node = (TreeNodeFile) aPath[n].getLastPathComponent();
            
            if( deepDelete( node.getFile() ) )
                delete( node );
            else
                bAll = false;
        }
        
        return bAll;
    }
    
    /**
     * Rename highlighted node.
     * 
     * @param sNewName
     * @return <code>true</code> if operation was successfull.
     */
    public boolean rename()
    {
        boolean      bSuccess = false;
        TreeNodeFile aPath    = getSelectedNode();
        // TODO: hacerlo
        return bSuccess;
    }
    
    public boolean properties()
    {
        boolean      bSuccess = false;
        TreeNodeFile aPath    = getSelectedNode();
        // TODO: hacerlo
        return bSuccess;
    }
    
    /**
     * Mark files to be deleted.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public void cut()
    {
        TreePath[] aPath = getSelectionPaths();
        
        // TODO: hacerlo
    }
    
    /**
     * Create a new entry (dir oor empty file) as child of highlighted node.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public void copy()
    {
        TreePath[] node = getSelectionPaths();
        // TODO: hacerlo
    }
    
    /**
     * Create a new entry (dir oor empty file) as child of highlighted node.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public boolean paste()
    {
        boolean  bSuccess = false;
        // TODO: hacerlo
        return bSuccess;
    }
    
    //------------------------------------------------------------------------//
    
    // If more than one node is selected this returns the first one and makes
    // the rest selected nodes unselected.
    private TreeNodeFile getSelectedNode()
    {
        TreePath path = null;
        
        if( getSelectionCount() > 0 )
        {
            path = getSelectionPath();
            clearSelection();
            setSelectionPath( path );
        }
        
        return (path == null ? null : (TreeNodeFile) path.getLastPathComponent());
    }
    
    private File[] getFilesIn( File fParent )
    {
        // List files, hidden are not included
        File[] afChildren = fParent.listFiles( new FileFilter() 
        {
            public boolean accept( File f )
            {
                return (! f.isHidden()) && (! bOnlyDirs || f.isDirectory());
            }
        } );
        
        // Sort directories before files
        if( afChildren != null )    // Needed or NPE when afChildren == null
        {
            Arrays.sort( afChildren, new Comparator<File>()
            {   
                public int compare( File f1, File f2 )
                {
                    if( f1.isDirectory() && ! f2.isDirectory() )       return -1;
                    else if ( ! f1.isDirectory() && f2.isDirectory() ) return 1;
                    else                                               return f1.getName().compareTo( f2.getName() );
                }
            } );
        }
        
        return afChildren;
    }
    
    // If passed file is a directory, recursively deletes it an all its files.
    // If passed file is just a file, deletes it.
    private boolean deepDelete( java.io.File f )
    {
        boolean bSuccess = false;
        
        if( f.exists() )
        {
            bSuccess = true;
            java.io.File[] files = f.listFiles();
            
            if( files != null )
            {
                for( int n = 0; n < files.length; n++ )
                {
                    if( files[n].isDirectory() )
                        bSuccess = bSuccess && deepDelete( files[n] );
                    else
                        bSuccess = bSuccess && files[n].delete();
                }
            }
            
            bSuccess = bSuccess && f.delete();
        }
        
        return bSuccess;
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
                File[]           afChildren = getFilesIn( (File) node.getUserObject() );
                
                // Deletes faked node (the one used to force JTree to show handles)
                if( node.getChildCount() == 1 )  // Must be the faked node
                    model.removeNodeFromParent( (TreeNodeFile) node.getChildAt( 0 ) );
                
                for( int n = 0; n < afChildren.length; n++ )
                {
                    File f = afChildren[n];
                    model.insertNodeInto( new TreeNodeFile( f, f.isDirectory() ), node, n );
                }
            }
        }

        public void treeCollapsed( TreeExpansionEvent tee )
        {
            TreeNodeFile node = (TreeNodeFile) tee.getPath().getLastPathComponent();
            
            // Local File System is very fast: removing childs is done to save memory
            if( ! (node.getUserObject() instanceof VFSFile) )
            {
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                
                while( node.getChildCount() > 0 )
                    model.removeNodeFromParent( (TreeNodeFile) node.getFirstChild() );
                
                // Inserts a faked node (used to force JTree to show handles)
                model.insertNodeInto( new TreeNodeFile( true ), node, 0 );
            }
        }
    }
}