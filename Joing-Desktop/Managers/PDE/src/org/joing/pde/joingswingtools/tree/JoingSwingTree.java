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
     * Returns the previous sibling node: if there is no previous sibling,
     * it returns the next sibling if there are no siblings it returns null.
     * 
     * @param node Node to obtain its sibling.
     * @return Previous or next sibling, or null if no sibling.
     */
    private DefaultMutableTreeNode getSibling( DefaultMutableTreeNode node )
    {
        //get previous sibling
        DefaultMutableTreeNode sibling = node.getPreviousSibling();

        if( sibling == null )
            sibling = node.getNextSibling();
        
        return sibling;
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
        TreePath               path = getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        
        ((DefaultTreeModel) getModel()).reload( node );
        scrollPathToVisible( path );
    }
    
    /**
     * Makes the node selected (highlighted) and visible.
     * 
     * @param Node to became selected. Can't be null.
     */
    public void setSelected( DefaultMutableTreeNode node )
    {
        TreePath path = new TreePath( node.getPath() );
        
        if( ! isPathSelected( path ) )
            setSelectionPath( path );
        
        if( ! isVisible( path ) )
            scrollPathToVisible( path );
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
    
    /**
     * Append passed node to selected node.
     * <p>
     * If there is no node selected, tha action is not taken.
     * 
     * @param child Node to be appended.
     */
    public void add( DefaultMutableTreeNode child )
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        
        if( parent != null )
            add( child, parent, parent.getChildCount() );
    }
    
    public void add( DefaultMutableTreeNode child, DefaultMutableTreeNode parent, int nIndex )
    {        
        ((DefaultTreeModel) getModel()).insertNodeInto( child, parent, nIndex );
        setSelected( child );
    }
    
    /**
     * Delete all selected nodes.
     * <p>
     * If there is no node selected, tha action is not taken.
     */
    public void delete()
    {
        TreePath[] aPath = getSelectionPaths();
        
        if( aPath.length > 0 )
        {
            DefaultTreeModel model = (DefaultTreeModel) getModel();
            
            for( int n = 0; n < aPath.length - 1; n++ )
                model.removeNodeFromParent( (DefaultMutableTreeNode) aPath[n].getLastPathComponent() );
            
            // Deletes last node via delete(...) method to ensure selecting appropiate new selected node
            delete( (DefaultMutableTreeNode) aPath[ aPath.length - 1 ].getLastPathComponent() );  
        }
    }
    
    public void delete( DefaultMutableTreeNode node )
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        
        if( parent != null )
        {
            DefaultMutableTreeNode toBeSelected = getSibling( node );
            
            if( toBeSelected == null )
                toBeSelected = parent;
            
            setSelected( toBeSelected );
        }
        
        ((DefaultTreeModel) getModel()).removeNodeFromParent( node );
    }
}