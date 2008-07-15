/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.yace;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author fmorero
 */
public class JoingCommunityTree extends JTree
{
    private boolean bOnlyConnected;
    
    //------------------------------------------------------------------------//
    
    public JoingCommunityTree()
    {
        bOnlyConnected = false;
        
        setRootVisible( true );
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( createRootNodes() ) );
        setCellRenderer( new UserTreeCellRenderer() );
    }
    
    /**
     * To show only dirs or dirs and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bOnlyDirs
     */
    public void setOnlyConnected( boolean bOnlyDirs )
    {
        this.bOnlyConnected = bOnlyDirs;
    }
    
    /**
     * Is showing only dirs or also files?
     * 
     * @return <code>true</code> if is showing only dirs.
     */
    public boolean isOnlyConnected()
    {
        return bOnlyConnected;
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
    
    private DefaultMutableTreeNode createRootNodes()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Community: joing.org" );
                               root.add( new DefaultMutableTreeNode( "John Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Mary Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Frank Doe" ) );
                               root.add( new DefaultMutableTreeNode( "Albert Einstein" ) );
                               root.add( new DefaultMutableTreeNode( "Fridich Nietszche" ) );
                               root.add( new DefaultMutableTreeNode( "Agustina de Aragón" ) );
        return root;
    }
    
    private File[] getFilesIn( File fParent )
    {
        // List files, hidden are not included
        File[] afChildren = fParent.listFiles( new FileFilter() 
        {
            public boolean accept( File f )
            {
                return (! f.isHidden()) && (! bOnlyConnected || f.isDirectory());
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
}