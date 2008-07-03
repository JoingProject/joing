/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.tinyexplorer;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author fmorero
 */
// TODO: Copiar esta calse cuando este terminada a PDE: es la base para el control DirSelector
public class JoingFileSystemTree extends JTree
{
    private boolean bOnlyDirs;
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemTree()
    {
        bOnlyDirs = false;
        setModel( new DefaultTreeModel( createRootNodes() ) );
        addTreeExpansionListener( new MyTreeExpansionListener() );
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
    
    /**
     * Force to reload (by invalidating) the whole tree or only highlighted node.
     * @param bAllTree To invalidate the root node (and subsequently all sub nodes) 
     * or only highlighted node.
     */
    public void reload( boolean bAllTree )
    {
        // FIXME: Comprimir los hijos de root y luego borrar los hijos de los hijos de root
    }
    
    //------------------------------------------------------------------------//
    
    private DefaultMutableTreeNode createRootNodes()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        List<File>             fRoots = Arrays.asList( JoingFileSystemView.getFileSystemView().getRoots() );
        
        for( File f: fRoots )
            root.add( new DefaultMutableTreeNode( f, true ) );
        
        return root;
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
        
        // Sort directories before files, 
        Arrays.sort( afChildren, new Comparator<File>()
        {   
            public int compare( File f1, File f2 )
            {
                if( f1.isDirectory() && ! f2.isDirectory() )       return -1;
                else if ( ! f1.isDirectory() && f2.isDirectory() ) return 1;
                else                                               return f1.getName().compareTo( f2.getName() );
            }
        } );
        
        return afChildren;
    }
    
    //------------------------------------------------------------------------//
    
    private final class MyTreeExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded( TreeExpansionEvent tee )
        {
            DefaultMutableTreeNode node  = (DefaultMutableTreeNode) tee.getPath().getLastPathComponent();
            DefaultTreeModel       model = (DefaultTreeModel) getModel();

            if( node.getAllowsChildren() && node.getChildCount() == 0 )
            {
                File[] afChildren = getFilesIn( (File) node.getUserObject() );

                for( int n = 0; n < afChildren.length; n++ )
                {
                    model.insertNodeInto( new DefaultMutableTreeNode( afChildren[n], afChildren[n].isDirectory() ), 
                                          node, n );
                }
            }
        }

        public void treeCollapsed( TreeExpansionEvent tee )
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tee.getPath().getLastPathComponent();

            if( ! (node.getUserObject() instanceof VFSFile) )
                node.removeAllChildren();     // Local File System is very fast: this is done to save memory
        }
    }
}