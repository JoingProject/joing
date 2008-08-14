/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.swingtools.filesystem.fsviewer.fstree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.joing.swingtools.JoingSwingTree;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;
import org.joing.swingtools.filesystem.fsviewer.FileSystemActionable;
import org.joing.swingtools.filesystem.fsviewer.FileSystemActionableCommonActions;
import org.joing.swingtools.filesystem.fsviewer.FileSystemJobs;

/**
 * To preservate independency of classes this one knows only to represent files
 * and FileSystemViewerControler knows only about about files and file systems.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemTree extends JoingSwingTree implements FileSystemActionable
{
    private FileSystemJobs fsworks    = null;
    private boolean        bShowFiles = true;   // TODO: ponerlo a false
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemTree()
    {
        setEditable( true );
        setRootVisible( false );    // Better not to make it visible
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( new TreeNodeFile() ) );  // root is the only node that does not have a File as user-object
        addTreeExpansionListener( new MyTreeExpansionListener() );
        addTreeSelectionListener( new TreeSelectionListener() 
        {
            public void valueChanged( TreeSelectionEvent tse )
            {
                TreePath path = tse.getPath();
                
                if( path != null )
                {
                    TreeNodeFile node = (TreeNodeFile) path.getLastPathComponent();
                    JoingFileSystemTree.this.fsworks.updateActionsEnableStatus( node.getFile() );
                }
            }
        } );
        addMouseListener( new MouseListener()  // TODO: No funciona
        {
            public void mousePressed( MouseEvent me ) 
            {
                TreePath path = getPathForLocation( me.getX(), me.getY() );
                
                // As no button is cheked, it works for all btns
                if( (path != null) && (! isPathSelected( path )) )
                    setSelectionPath( path );
            }
            public void mouseReleased( MouseEvent me )  {  }
            public void mouseClicked(  MouseEvent me )  {  }
            public void mouseEntered(  MouseEvent me )  {  }
            public void mouseExited(   MouseEvent me )  {  }
        } );
        
        // Add one node per each File Systems (local and remote) to root node
        SwingWorker sw = new SwingWorker<Void,TreeNodeFile>()
        {            
            protected Void doInBackground() throws Exception
            {
                File[] afRoots = JoingFileSystemView.getFileSystemView().getRoots();
                
                for( int n = 0; n < afRoots.length; n++ )
                    publish( new TreeNodeFile( afRoots[n] ) );
                        
                return null;
            }
            
            protected void process( List<TreeNodeFile> lstNodes )
            {
                DefaultTreeModel model = (DefaultTreeModel) JoingFileSystemTree.this.getModel();
                TreeNodeFile     root  = (TreeNodeFile) model.getRoot();
                
                for( TreeNodeFile node : lstNodes )
                    model.insertNodeInto( node, root, root.getChildCount() );
            }
            
            protected void done() 
            {
                TreeNodeFile root = (TreeNodeFile) JoingFileSystemTree.this.getModel().getRoot();
                JoingFileSystemTree.this.setSelected( (TreeNodeFile) root.getChildAt( 0 ) );
            }
        };
        sw.execute();
    }
    
    public void setFileSystemWorks( FileSystemJobs fsworks )
    {
        this.fsworks = fsworks;
        setComponentPopupMenu( this.fsworks.getPopupMenu() );
    }
    
     /**
     * To show only folders or folders and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bShowFiles
     */
    public void setShowingFiles( boolean bShowFiles )
    {
        this.bShowFiles = bShowFiles;
    }
    
    /**
     * Is showing only folders or also files?
     * 
     * @return <code>true</code> if is showing folders and files.
     */
    public boolean isShowingFiles()
    {
        return bShowFiles;
    }
    
    //------------------------------------------------------------------------//
    // Interface FileSystemActionable Implementation
    
    public File getSelectedFile()
    {
        TreeNodeFile node = getSelectedNode();
        
        return (node == null ? null : node.getFile());
    }
    
    /**
     * 
     * @param file File representing the node in the tree to be selected.
     */
    public void setSelectedFile( File file )
    {
        if( file != null && file.exists() )
        {
            JOptionPane.showMessageDialog( this, "Option not yet implemented" );
            // TODO: hacerlo
            //       Si el fic existe, hay que ir traceando (y cargando si fuera 
            //       necesario) uno a uno todos los nodos hasta completar la ruta.
            // OJO: el fichero puede estar en local o en uno de los remotos
        }
    }
    
    /**
     * Create a new folder as child of highlighted node.
     * @return A reference to new created folder if operation was successfull or
     *         null otherwise.
     */
    public void createFolder()
    {
        File fNewFolder = fsworks.createFolder( getSelectedNode().getFile() );
        
        if( fNewFolder != null )
            add( new TreeNodeFile( fNewFolder ) );
    }
    
    /**
     * Create a new file as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public void createFile()
    {
        File fNewFile = fsworks.createFile( getSelectedNode().getFile() );
        
        if( fNewFile != null )
            add( new TreeNodeFile( fNewFile ) );
    }
    
    /**
     * Delete selected node(s) and file(s).
     * 
     * @return <code>true</code> if operation was successfull.
     */
    public void deleteAllSelected()
    {
        boolean bAll = true;   // Where all selected files deleted?
        
        TreePath[] aPath = getSelectionPaths();

        for( int n = 0; n < aPath.length; n++ )
        {
            TreeNodeFile node = (TreeNodeFile) aPath[n].getLastPathComponent();

            if( fsworks.delete( node.getFile() ) )
                delete( node );
            else
                bAll = false;
        }
        
        if( ! bAll )
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Error deleteing one or more files." );
    }
    
    /**
     * Starts user edition on highlighted node.
     */
    public void rename()
    {
        // TODO: hacerlo
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Option not yet implemented" );
        ///startEditingAtPath( getSelectionPath() );
    }
    
    public void properties()
    {
        FileSystemActionableCommonActions.properties( getSelectedNode().getFile() );
    }
    
    /**
     * Send selected node(s) and file(s) to clipboard and mark them to be deleted.
     */
    public void cut()
    {
        TreePath[] aPath = getSelectionPaths();
        // TODO: hacerlo
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Option not yet implemented" );
    }
    
    /**
     * Send selected node(s) and file(s) to clipboard and mark them to be copied.
     */
    public void copy()
    {
        TreePath[] node = getSelectionPaths();
        // TODO: hacerlo
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Option not yet implemented" );
    }
    
    /**
     * Copy node(s) and file(s) from clipboard to selected node.
     * 
     * @return <code>true</code> if operation was successfull.
     */
    public void paste()
    {
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Option not yet implemented" );
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * If more than one node is selected this returns the first one and makes
     * the rest selected nodes unselected.
     * 
     * @return The first selected node.
     */
    private TreeNodeFile getSelectedNode()
    {
        TreePath path = getSelectionPath();
        
        if( getSelectionCount() > 1 )    // There is more than one node selected
        {
            clearSelection();
            setSelectionPath( path );
        }
        
        return (path == null ? null : (TreeNodeFile) path.getLastPathComponent());
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: MyTreeExpansionListener
    //------------------------------------------------------------------------//
    private final class MyTreeExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded( TreeExpansionEvent tee )
        {
            TreeNodeFile node = (TreeNodeFile) tee.getPath().getLastPathComponent();

            if( node.getAllowsChildren() && 
                (node.getChildCount() == 0 || ((TreeNodeFile) node.getChildAt( 0 )).isFakedNode()) )
            {
                DefaultTreeModel model      = (DefaultTreeModel) getModel();
                File[]           afChildren = fsworks.getFilesIn( (File) node.getUserObject(), isShowingFiles() );
                
                // Deletes faked node (the one used to force JTree to show handles)
                if( node.getChildCount() == 1 )  // Must be the faked node
                    model.removeNodeFromParent( (TreeNodeFile) node.getChildAt( 0 ) );
                
                for( int n = 0; n < afChildren.length; n++ )
                {
                    File f = afChildren[n];
                    model.insertNodeInto( new TreeNodeFile( f ), node, n );
                }
            }
        }
        
        public void treeCollapsed( TreeExpansionEvent tee )
        {
            TreeNodeFile node = (TreeNodeFile) tee.getPath().getLastPathComponent();
            
            // Local File System is very fast: removing childs is done to save memory
            if( ! (node.getUserObject() instanceof VFSFile) )
            {
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                
                while( node.getChildCount() > 0 )
                    model.removeNodeFromParent( (TreeNodeFile) node.getFirstChild() );
                
                // Inserts a faked node (used to force JTree to show handles)
                model.insertNodeInto( new TreeNodeFile( true ), node, 0 );
            }
        }
    }
}