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
// TODO: No hace lo que yo pretendía
//       Cuando se cierra un nodo del FS local, desaparece el 'handle', y pensé
//       que re-escribiendo getAllowsChildren() y isLeaf() se arreglaría, pero no es así.
public class TreeNodeFile extends DefaultMutableTreeNode
{
    public TreeNodeFile()
    {
        super();
    }

    public TreeNodeFile( File f, boolean bAllowsChildren )
    {
        super( f, bAllowsChildren );
    }

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
            s = f.getName();

            if( JoingFileSystemView.getFileSystemView().isRoot( f ) )
                s += (f instanceof VFSFile ? " (Remote)" : " (Local)");
        }

        return s;
    }
}