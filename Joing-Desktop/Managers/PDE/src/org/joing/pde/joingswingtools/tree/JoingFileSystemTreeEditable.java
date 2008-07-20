/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import java.io.File;
import java.io.IOException;
import javax.swing.tree.TreePath;
import org.joing.runtime.vfs.JoingFileSystemView;

/**
 * This class adds edition functionality (cut, copy, paste, rename, delete, ...)
 * to <code>JoingFileSystemTree</code> class.
 * <p>
 * Also adds Drag & Drop functionality.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemTreeEditable extends JoingFileSystemTree
{
    private static final String sNewFile = "NewFile";
    
    /**
     * Create a new directory as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createDir() throws IOException
    {
        return JoingFileSystemView.getFileSystemView().createNewFolder( (File) getSelectedNode().getUserObject() );
    }
    
    /**
     * Create a new file as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createFile()
    {
        File fParent = (File) getSelectedNode().getUserObject();
        
        return JoingFileSystemView.getFileSystemView().createFileObject( fParent, sNewFile );
    }
    
    /**
     * Delete highlighted node.
     * <p>
     * It does not ask for confirmation because file is sent to trashcan, an it
     * can be always recovered from trashcan.
     * 
     * @return <code>true</code> if operation was successfull.
     */
    public boolean delete()
    {
        boolean    bSuccess = false;
        TreePath[] aPath    = getSelectionPaths();
        // TODO: hacerlo
        return bSuccess;
    }
    
    /**
     * Rename highlighted node.
     * 
     * @param sNewName
     * @return <code>true</code> if operation was successfull.
     */
    public boolean rename()
    {
        boolean      bSuccess = false;
        TreeNodeFile aPath    = getSelectedNode();
        // TODO: hacerlo
        return bSuccess;
    }
    
    public boolean properties()
    {
        boolean      bSuccess = false;
        TreeNodeFile aPath    = getSelectedNode();
        // TODO: hacerlo
        return bSuccess;
    }
    
    /**
     * Mark files to be deleted.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public void cut()
    {
        TreePath[] aPath = getSelectionPaths();
        
        // TODO: hacerlo
    }
    
    /**
     * Create a new entry (dir oor empty file) as child of highlighted node.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public void copy()
    {
        TreePath[] node = getSelectionPaths();
        // TODO: hacerlo
    }
    
    /**
     * Create a new entry (dir oor empty file) as child of highlighted node.
     * @param fNew
     * @return <code>true</code> if operation was successfull.
     */
    public boolean paste()
    {
        boolean  bSuccess = false;
        // TODO: hacerlo
        return bSuccess;
    }
    
    //------------------------------------------------------------------------//
    
    // If more than one node is selected this returns the first one and makes
    // the rest selected nodes unselected.
    private TreeNodeFile getSelectedNode()
    {
        TreePath path = null;
        
        if( getSelectionCount() > 0 )
        {
            path = getSelectionPath();
            clearSelection();
            setSelectionPath( path );
        }
        
        return (path == null ? null : (TreeNodeFile) path.getLastPathComponent());
    }
}