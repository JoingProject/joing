/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class TreeNodeFile extends DefaultMutableTreeNode
{
    private boolean bFakedNode = false;
    
    //------------------------------------------------------------------------//
    
    public TreeNodeFile()
    {
        super( null, true );   // true because this constructor is used by roots
    }
    
    public TreeNodeFile( File f )
    {
        super( f, f.isDirectory() );
    }
    
    public TreeNodeFile( boolean bFakedNode )
    {
        super( null, false );
        this.bFakedNode = bFakedNode;
    }
    
    //------------------------------------------------------------------------//
    
    public File getFile()
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
    
    public boolean isFakedNode()
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
                s = "Join'g File System";
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