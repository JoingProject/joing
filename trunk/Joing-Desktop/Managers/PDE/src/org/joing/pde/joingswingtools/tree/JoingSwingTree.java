/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * An improved version of JTree assuming that all nodes are intances or inherit 
 * from DefaultMutableTreeNode, also that TreeModel is instance of 
 * DefaultTreeModel.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingSwingTree extends JTree
{
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
        TreePath               path = getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        
        ((DefaultTreeModel) getModel()).reload( node );
        scrollPathToVisible( path );
    }
    
    /**
     * Makes the node in tree that corresponds to passed file selected 
     * (highlighted) and visible.
     * 
     * @param f2Select File to to be selected. Can't be null.
     * @return
     */
    public boolean setSelected( Object objSelect )
    {
        DefaultMutableTreeNode node = search( null, objSelect );
        
        if( node != null )
        {
            TreePath path = new TreePath( node.getPath() );
            
            setSelectionPath( path );
            scrollPathToVisible( path );
        }
        
        return (node != null);
    }
    
    /**
     * Searches for a node that represents (corresponds) passed object.
     *  
     * @param nodeStart A node extracted from the tree (if null, root is taken)
     * @param f File to be searched.
     * @return The node if object was found, otherwise, null.
     */
    public DefaultMutableTreeNode search( DefaultMutableTreeNode nodeStart, Object oSearch )
    {
        // TODO: No va
        DefaultMutableTreeNode nodeCheck;
        
        if( nodeStart == null )
            nodeStart = (DefaultMutableTreeNode) getModel().getRoot();
        
        for( int n = 0; n < nodeStart.getChildCount(); n++ )
        {
            nodeCheck = (DefaultMutableTreeNode) nodeStart.getChildAt( n );
            
            if( nodeCheck.getUserObject() != null && nodeCheck.getUserObject().equals( oSearch ) )
                return nodeCheck;
            else
                if( nodeCheck.getChildCount() > 0 )
                    search( nodeCheck, oSearch );
        }
        
        return null;
    }
}