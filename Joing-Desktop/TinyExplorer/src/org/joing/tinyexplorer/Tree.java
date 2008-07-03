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
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author fmorero
 */

class Tree extends JTree implements TreeExpansionListener
{
    Tree()
    {
        super();
        setModel( new DefaultTreeModel( createRootNodes() ) );
        addTreeExpansionListener( this );
    }
    
    void reload()
    {
        // FIXME: Comprimir los hijos de root y luego borrar los hijos de los hijos de root
    }

    //------------------------------------------------------------------------//
    // Required by TreeExpansionListener interface.
    
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
    }
    
    //------------------------------------------------------------------------//
    
    private DefaultMutableTreeNode createRootNodes()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        List<File>             fRoots = Arrays.asList( FileSystemView.getFileSystemView().getRoots() ); // FIXME: JoingFileSystemView.getFileSystemView();
        
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
                return ! f.isHidden();
            }
        } );
        
        // Sort directories before files, 
        Arrays.sort( afChildren, new Comparator<File>()
        {   
            public int compare( File f1, File f2 )
            {
                if( f1.isDirectory() && ! f2.isDirectory() )       return -1;
                else if ( ! f1.isDirectory() && f2.isDirectory() ) return 1;
                else return f1.getName().compareTo( f2.getName() );
            }
        } );
        
        return afChildren;
    }
}