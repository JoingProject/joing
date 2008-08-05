/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.swingtools;

import java.util.Enumeration;
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
        DefaultMutableTreeNode sibling = node.getPreviousSibling();

        if( sibling == null )
            sibling = node.getNextSibling();
        
        return sibling;
    }
    
    /**
     * Reloads the whole tree.
     */
    public void reloadAll()
    {
        DefaultTreeModel       model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode node  = (DefaultMutableTreeNode) model.getRoot();
        
        model.reload( node );
        setSelected( node );
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
     * Makes passed node selected (highlighted) and visible.
     * 
     * @param Node to became selected. Can't be null.
     */
    public void setSelected( DefaultMutableTreeNode node )
    {
        TreePath path = new TreePath( node.getPath() );
        
        if( ! isPathSelected( path ) )
            setSelectionPath( path );
        
        scrollPathToVisible( path );   // Don't use if( ! isVisible( path ) ) because this isVisible is for other things
    }
    
    /**
     * Searches for a node in the tree.
     *  
     * @param nodeStart A tree node (can't be null).
     * @param oSearch User-object (see DefaultMutableTreeNode) to be searched.
     * @return The node if object was found, otherwise, null.
     */
    public DefaultMutableTreeNode search( DefaultMutableTreeNode node, Object oSearch )
    {
        Enumeration e = ((DefaultMutableTreeNode) getModel().getRoot()).depthFirstEnumeration();
        
        while( e.hasMoreElements() )
        {
            node = (DefaultMutableTreeNode) e.nextElement();
            
            if( node.getUserObject() != null && node.getUserObject().equals( oSearch ) )
                return node;
        }
        
        return null;
    }
    
    //------------------------------------------------------------------------//
    // As these methods only affect to the visual part of the tree,
    // they should be used always in conjunction with other that will 
    // affect the "physical" (UserObject) part of the tree.
    
    /**
     * Append passed node to selected node.
     * <p>
     * If there is no node selected, tha action is not taken.
     * 
     * @param child Node to be appended.
     */
    protected void add( DefaultMutableTreeNode child )
    {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        
        if( parent != null )
            add( child, parent, parent.getChildCount() );
    }
    
    protected void add( DefaultMutableTreeNode child, DefaultMutableTreeNode parent, int nIndex )
    {        
        ((DefaultTreeModel) getModel()).insertNodeInto( child, parent, nIndex );
        setSelected( child );
    }
    
    /**
     * Delete all selected nodes.
     * <p>
     * If there is no node selected, tha action is not taken.
     */
    protected void delete()
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
    
    protected void delete( DefaultMutableTreeNode node )
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