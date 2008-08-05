/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.swingtools.fsviewer.fstree;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
class TreeNodeFile extends DefaultMutableTreeNode
{
    private boolean bFakedNode = false;
    
    //------------------------------------------------------------------------//
    
    TreeNodeFile()
    {
        super( null, true );   // true because this constructor is used by roots
    }
    
    TreeNodeFile( File f )
    {
        super( f, f.isDirectory() );
    }
    
    TreeNodeFile( boolean bFakedNode )
    {
        super( null, false );
        this.bFakedNode = bFakedNode;
    }
    
    //------------------------------------------------------------------------//
    
    File getFile()
    {
        return (File) super.userObject;
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
    
    boolean isFakedNode()
    {
        return bFakedNode;
    }
    
    public String toString()
    {
        File   f = (File) getUserObject();
        String s = null;

        if( f == null )
        {
            if( bFakedNode )
                s = "Reading files ...";
            else
                s = "Join'g File System";   // Can't happen because root is never visible
        }
        else
        {
            s = f.getName();

            if( JoingFileSystemView.getFileSystemView().isRoot( f ) )
                s += (f instanceof VFSFile ? " (Remote)" : " (Local)");
        }

        return s;
    }
}