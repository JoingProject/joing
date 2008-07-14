/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.apps.YAFE;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
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
        setRootVisible( false );
        setShowsRootHandles( true );
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
     * Force to reload (by invalidating) the whole tree.
     */
    public void reloadAll()
    {
        // FIXME: Colapsar los hijos de root y luego borrar los nietos de root
    }
    
    /**
     * Reloads selected (highlighted) node and all sub nodes.
     */
    public void reloadSelected()
    {
        // FIXME: Colapsar los hijos de root y luego borrar los nietos de root
    }
    
    public boolean goTo( File f )
    {
        boolean bExistsNode = false;
        
        return bExistsNode;
    }
    
    //------------------------------------------------------------------------//
    
    private FileNode createRootNodes()
    {
        FileNode root    = new FileNode();
        File[]   afRoots = JoingFileSystemView.getFileSystemView().getRoots();
        
        for( int n = 0; n < afRoots.length; n++ )
            root.add( new FileNode( afRoots[n], true ) );
        
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
    // INNER CLASS: MyTreeExpansionListener
    //------------------------------------------------------------------------//
    private final class MyTreeExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded( TreeExpansionEvent tee )
        {
            FileNode node  = (FileNode) tee.getPath().getLastPathComponent();

            if( node.getAllowsChildren() && node.getChildCount() == 0 )
            {
                DefaultTreeModel model      = (DefaultTreeModel) getModel();
                File[]           afChildren = getFilesIn( (File) node.getUserObject() );

                for( int n = 0; n < afChildren.length; n++ )
                {
                    File f = afChildren[n];
                    model.insertNodeInto( new FileNode( f, f.isDirectory() ), node, n );
                }
            }
        }

        public void treeCollapsed( TreeExpansionEvent tee )
        {
            FileNode node = (FileNode) tee.getPath().getLastPathComponent();
            
            // Local File System is very fast: removing childs is done to save memory
            if( ! (node.getUserObject() instanceof VFSFile) )
            {
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                
                while( node.getChildCount() > 0 )
                    model.removeNodeFromParent( (FileNode) node.getFirstChild() );
            }
        }
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: FileNode
    //------------------------------------------------------------------------//
    // TODO: No hace lo que yo pretendía
    //       Cuando se cierra un nodo del FS local, desaparece el 'handle', y pensé
    //       que re-escribiendo getAllowsChildren() y isLeaf() se arreglaría, pero no es así.
    protected final class FileNode extends DefaultMutableTreeNode
    {
        public FileNode()
        {
            super();
        }
        
        public FileNode( File f, boolean bAllowsChildren )
        {
            super( f, bAllowsChildren );
        }
        
        public boolean getAllowsChildren()
        {
            File f = (File) getUserObject();
            
            return (f == null || f.isDirectory());    // f == null => root
        }
        
        public boolean isLeaf()
        {
            return ! getAllowsChildren();
        }
        
        public String toString()
        {
            File   f = (File) getUserObject();
            String s = null;
            
            if( f == null )
            {
                s = "Join'g File System";
            }
            else
            {
                s = f.toString();
                
                if( JoingFileSystemView.getFileSystemView().isRoot( f ) )
                    s += (f instanceof VFSFile ? " (Remote)" : " (Local)");
            }
            
            return s;
        }
    }
}