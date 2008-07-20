/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeModel;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Copiar esta calse cuando este terminada a PDE: es la base para el control DirSelector
public class JoingFileSystemTree extends JoingSwingTree
{
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
                TreeNodeFile         root    = (TreeNodeFile) model.getRoot();
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
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyTreeExpansionListener
    //------------------------------------------------------------------------//
    private final class MyTreeExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded( TreeExpansionEvent tee )
        {
            TreeNodeFile node  = (TreeNodeFile) tee.getPath().getLastPathComponent();

            if( node.getAllowsChildren() && node.getChildCount() == 0 )
            {
                DefaultTreeModel model      = (DefaultTreeModel) getModel();
                File[]           afChildren = getFilesIn( (File) node.getUserObject() );

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
            }
        }
    }
}