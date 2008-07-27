/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.joingswingtools.tree;

import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemTree extends JoingSwingTree
{
    private boolean bOnlyDirs = false;
    
    // Actions (created lazily to save memory)
    private AbstractAction actHome       = null;
    private AbstractAction actReload     = null;
    private AbstractAction actNewFolder  = null;
    private AbstractAction actNewFile    = null;
    private AbstractAction actCut        = null;
    private AbstractAction actCopy       = null;
    private AbstractAction actPaste      = null;
    private AbstractAction actRename     = null;
    private AbstractAction actDelete     = null;
    private AbstractAction actProperties = null;
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemTree()
    {
        setEditable( true );
        setRootVisible( true );
        setShowsRootHandles( true );
        setModel( new DefaultTreeModel( new TreeNodeFile() ) );  // root is the only node that does not have a File as user-object
        addTreeExpansionListener( new MyTreeExpansionListener() );
        addTreeSelectionListener( new TreeSelectionListener() 
        {
            public void valueChanged( TreeSelectionEvent tse )
            {
                JoingFileSystemTree.this.updateActionsEnableStatus();
            }
        } );
        
// TODO: QUIZAS SE PUEDE USAR ESTO PARA EL RENAME
//        getModel().addTreeModelListener( new TreeModelListener()
//        {
//            public void treeNodesChanged( TreeModelEvent tme )
//            {
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tme.getTreePath().getLastPathComponent();
//
//                
//                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showMessageDialog(
//                                "Error renaming", "Can't rename selected entity." );
//                
//            }
//            
//            public void treeNodesInserted( TreeModelEvent tme )     { }
//            public void treeNodesRemoved( TreeModelEvent tme )      { }
//            public void treeStructureChanged( TreeModelEvent tme )  { }
//        } );
        
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
                JoingFileSystemTree.this.updateActionsEnableStatus();
            }
        };
        sw.execute();
    }
    
    /**
     * To show only dirs or dirs and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bOnlyDirs
     */
    public void setOnlyDirs( boolean bOnlyDirs )
    {
        this.bOnlyDirs = bOnlyDirs;
    }
    
    /**
     * Is showing only dirs or also files?
     * 
     * @return <code>true</code> if is showing only dirs.
     */
    public boolean isOnlyDirs()
    {
        return bOnlyDirs;
    }
    
    public TreeNodeFile setSelected( File node )
    {
        TreeNodeFile nodeSelected = getSelectedNode();
        
        // TODO: hacerlo
        JOptionPane.showMessageDialog( this, "Option not yet implemented" );
        return nodeSelected;
    }
    
    //------------------------------------------------------------------------//
    // Editing actions
    
    /**
     * Convenience method to retrieve all actions.
     * <p>
     * Those null array elements are used to group actions, indicating that a 
     * separator should be placed at this positon.
     * For example:
     * <pre>
     * private void initToolBarAndPopupMenu()
     * {
     *   AbstractAction[] actions = tree.getActions();
     *   JPopupMenu       popup   = new JPopupMenu();
     *   
     *   for( int n = 0; n < actions.length; n++ )
     *   {
     *       if( actions[n] == null )
     *       {
     *           toolbar.add( new JToolBar.Separator() );
     *           popup.add( new JPopupMenu.Separator() );
     *       }
     *       else
     *       {
     *           toolbar.add( actions[n] );
     *           popup.add( actions[n] );
     *       }
     *   }
     *   
     *   tree.setComponentPopupMenu( popup );
     * }
     * </pre>
     * 
     * @return An array with all actions.
     */
    public AbstractAction[] getActions()
    {
        return (new AbstractAction[] { getHomeAction(), getReloadAction(), null, 
                                       getNewFolderAction(), getNewFileAction(), null, 
                                       getCutAction(), getCopyAction(), getPasteAction(), null, 
                                       getRenameAction(), getPropertiesAction(), null, 
                                       getDeleteAction() });
    }
    
    /**
     * Return an action to make user home directory as selected tree node.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getHomeAction()
    {
        ImageIcon iconHome = new ImageIcon( getClass().getResource( "images/home.png" ) );
        
        if( actHome == null )
        {
            actHome = new MyAction( "Home", iconHome.getImage(), 'H' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    JoingFileSystemTree    tree = JoingFileSystemTree.this;
                    DefaultMutableTreeNode node = search( (DefaultMutableTreeNode) tree.getModel().getRoot(),
                                                          new File( System.getProperty( "user.home" ) ) );

                    if( node != null )
                        tree.setSelected( node );
                }
            };
        }

        return actHome;
    }
    
    /**
     * Return an action to reload selected tree node or whole tree.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getReloadAction()
    {
        ImageIcon iconLoad   = new ImageIcon( getClass().getResource( "images/reload.png" ));
        
        if( actReload == null )
        {
            actReload = new MyAction( "Reload", iconLoad.getImage(), 'L', "Reload current node [Ctrl-L] (with Shift, whole tree)" )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    if( (ae.getModifiers() & ActionEvent.SHIFT_MASK) != 0 )
                        JoingFileSystemTree.this.reloadAll();
                    else
                        JoingFileSystemTree.this.reloadSelected();
                }
            };
        }

        return actReload;
    }
    
    /**
     * Return an action to create a new directory from selected tree node.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getNewFolderAction()
    {
        if( actNewFolder == null )
        {
            actNewFolder = new MyAction( "New folder", StandardImage.FOLDER, 'F' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    try
                    {
                        JoingFileSystemTree.this.createDir();
                    }
                    catch( IOException exc )
                    {
                        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
                    }
                }
            };
        }

        return actNewFolder;
    }
    
    /**
     * Return an action to create a new file from selected tree node.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getNewFileAction()
    {
        if( actNewFile == null )
        {
            actNewFile = new MyAction( "New file", StandardImage.NEW, 'I' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    JoingFileSystemTree.this.createFile();
                }
            };
        }
        
        return actNewFile;
    }
    
    /**
     * Return an action to cut selected nodes and the underlaying file(s).
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getCutAction()
    {
        if( actCut == null )
        {
            actCut = new MyAction( "Cut", StandardImage.CUT, 'X' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    JoingFileSystemTree.this.cut();
                }
            };
        }
        
        return actCut;
    }
    
    /**
     * Return an action to copy selected nodes and the underlaying file(s).
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getCopyAction()
    {
        if( actCopy == null )
        {
            actCopy = new MyAction( "Copy", StandardImage.COPY, 'C' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    JoingFileSystemTree.this.copy();
                }
            };
        }
        
        return actCopy;
    }
    
    /**
     * Return an action to paste previously cut or copied nodes and the 
     * underlaying file(s).
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getPasteAction()
    {
        if( actPaste == null )
        {
            actPaste = new MyAction( "Paste", StandardImage.PASTE, 'V' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    if( ! JoingFileSystemTree.this.paste() )
                        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                                showMessageDialog( "Error pasteing", "Can't paste one or more selected entities." );
                }
            };
        }
        
        return actPaste;
    }
    
    /**
     * Return an action to rename selected tree node and the underlaying file.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getRenameAction()
    {
        ImageIcon iconRename = new ImageIcon( getClass().getResource( "images/rename.png" ));
        
        if( actRename == null )
        {
            actRename = new MyAction( "Rename", iconRename.getImage(), 'R' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    JoingFileSystemTree.this.rename();   // After renaming, TreeModelListener::treeNodesChanged() is invoked
                }
            };
        }
        
        return actRename;
    }
        
    /**
     * Return an action to delete selected tree node and the underlaying file.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getDeleteAction()
    {
        if( actDelete == null )
        {
            actDelete = new MyAction( "Delete", StandardImage.TRASHCAN, 'D' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    if( ! JoingFileSystemTree.this.deleteAllSelected() )
                        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                                showMessageDialog( "Error deleting", "Can't delete one or more selected files or directories." );
                }
            };
        }
        
        return actDelete;
    }
        
    /**
     * Return an action to edit selected tree node properties and the 
     * underlaying file.
     * <p>
     * Note: The action is lazily created to have in memory only those used.
     * @return Requested action
     */
    public AbstractAction getPropertiesAction()
    {
        if( actProperties == null )
        {
            actProperties = new MyAction( "Properties", StandardImage.PROPERTIES, 'P' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    if( ! JoingFileSystemTree.this.properties() )
                        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                                showMessageDialog( "Error editing properties", "Can't edit properties for selected entity." );
                }
            };
        }
        
        return actProperties;
    }
    
    //------------------------------------------------------------------------//
    // Editing methods
    
    /**
     * Create a new directory as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createDir() throws IOException
    {
        File fDir = JoingFileSystemView.getFileSystemView().createNewFolder( (File) getSelectedNode().getUserObject() );
        
        add( new TreeNodeFile( fDir ) );
        
        return fDir;
    }
    
    /**
     * Create a new file as child of highlighted node.
     * @return <code>true</code> if operation was successfull.
     */
    public File createFile()
    {
        File fParent = (File) getSelectedNode().getUserObject();
        File fNew    = JoingFileSystemView.getFileSystemView().createFileObject( fParent, 
                                                                                 getNextNewFileName( fParent ) );
        add( new TreeNodeFile( fNew ) );
        
        return fNew;
    }
    
    /**
     * Delete selected node(s) and file(s).
     * 
     * @return <code>true</code> if operation was successfull.
     */
    public boolean deleteAllSelected()
    {
        boolean bAll = true;   // Where all selected files deleted?
        
        if( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
            showYesNoDialog( "Confirm deletion", "Deleted entities can't be recovered.\nPlease confirm to delete." ) )
        {
            TreePath[] aPath = getSelectionPaths();

            for( int n = 0; n < aPath.length; n++ )
            {
                TreeNodeFile node = (TreeNodeFile) aPath[n].getLastPathComponent();

                if( deepDelete( node.getFile() ) )
                    delete( node );
                else
                    bAll = false;
            }
        }
        
        return bAll;
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
    
    public boolean properties()
    {
        boolean      bSuccess = false;
        TreeNodeFile aPath    = getSelectedNode();
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Option not yet implemented" );
        return true; ////////bSuccess;
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
    public boolean paste()
    {
        boolean bSuccess = false;
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Option not yet implemented" );
        return true; ////////bSuccess;
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
    
    // Retrieve all files in passed dir or null if none
    private File[] getFilesIn( File fParent )
    {
        // List files, hidden are not included
        File[] afChildren = fParent.listFiles( new FileFilter() 
        {
            public boolean accept( File f )
            {
                return (! f.isHidden()) && (! bOnlyDirs || f.isDirectory());
            }
        } );
        
        if( afChildren == null )    // Yes, afChildren can be null
        {
            afChildren = new File[0];
        }
        else                        // Sort directories before files
        {
            Arrays.sort( afChildren, new Comparator<File>()
            {
                public int compare( File f1, File f2 )
                {
                    if( f1.isDirectory() && ! f2.isDirectory() )       return -1;
                    else if ( ! f1.isDirectory() && f2.isDirectory() ) return 1;
                    else                                               return f1.getName().compareTo( f2.getName() );
                }
            } );
        }
        
        return afChildren;
    }
    
    // If passed file is a directory, recursively deletes it an all its files.
    // If passed file is just a file, deletes it.
    private boolean deepDelete( java.io.File f )
    {
        boolean bSuccess = false;
        
        if( f.exists() )
        {
            bSuccess = true;
            java.io.File[] files = f.listFiles();
            
            if( files != null )
            {
                for( int n = 0; n < files.length; n++ )
                {
                    if( files[n].isDirectory() )
                        bSuccess = bSuccess && deepDelete( files[n] );
                    else
                        bSuccess = bSuccess && files[n].delete();
                }
            }
            
            bSuccess = bSuccess && f.delete();
        }
        
        return bSuccess;
    }
    
    private String getNextNewFileName( File fParent )
    {
        // TODO: Hacerlo bien: buscar el último: NewFileN y añadirle 1
        String sBase = "NewFile";
        String sTime = String.valueOf( System.currentTimeMillis() );
        
        return sBase + sTime.substring( 10 ); 
    }
    
    private void updateActionsEnableStatus()
    {
        int          nSelect     = getSelectionCount();
        boolean      bIsRoot     = true;
        boolean      bAlterable  = true;  // Read not read-only
        boolean      bDeleteable = true;
        TreeNodeFile node1st     = null;
        
        if( nSelect > 0 )
        {
            node1st = (TreeNodeFile) getSelectionPath().getLastPathComponent();
            bIsRoot = JoingFileSystemView.getFileSystemView().isRoot( node1st.getFile() );
            
            if( node1st.getFile() instanceof VFSFile )
            {
                bAlterable  = ((VFSFile) node1st.getFile()).isAlterable();
                bDeleteable = ((VFSFile) node1st.getFile()).isDeleteable();
            }
        }
        
      //if( actHome != null )    Always enabled
      //    actHome.setEnabled( );
            
        if( actReload != null )
            actReload.setEnabled( nSelect > 0 );
        
        if( actNewFolder != null )
            actNewFolder.setEnabled( (nSelect > 0) && node1st.getAllowsChildren() );
        
        if( actNewFile != null )
            actNewFile.setEnabled( (nSelect > 0) && node1st.getAllowsChildren() );
        
        if( actCut != null )
            actCut.setEnabled( ((nSelect > 1) || (! bIsRoot)) && bDeleteable );
        
        if( actCopy != null )
            actCopy.setEnabled( (nSelect > 1) || (! bIsRoot) );
        
        if( actPaste != null )
            actPaste.setEnabled( false );   // TODO: hay que implemetar el clipboard para activar el paste
        
        if( actRename != null )
            actRename.setEnabled( ((nSelect > 0) && (! bIsRoot)) && bAlterable );
        
        if( actDelete != null )
            actDelete.setEnabled( ((nSelect > 0) && (! bIsRoot)) && bDeleteable );
        
        if( actProperties != null )
            actProperties.setEnabled( (nSelect > 0) );
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
                File[]           afChildren = getFilesIn( (File) node.getUserObject() );
                
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
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private abstract class MyAction extends AbstractAction
    {
        private MyAction( String sText, StandardImage img, char character )
        {
            this( sText, 
                  org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( img ),
                  character );
        }

        private MyAction( String sText, Image image, char character )
        {
            this( sText, image, character, null );
        }

        private MyAction( String sText, Image image, char character, String sToolTip )
        {
            super( sText );
            
            KeyStroke stroke   = KeyStroke.getKeyStroke( character, Event.CTRL_MASK );
            
            if( sToolTip == null )
                sToolTip = sText +" [Ctrl-"+ Character.toString( character ) +"]";
            
            putValue( SMALL_ICON       , new ImageIcon( image.getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) ) );
            putValue( LARGE_ICON_KEY   , new ImageIcon( image.getScaledInstance( 24, 24, Image.SCALE_SMOOTH ) ) );
            putValue( ACCELERATOR_KEY  , stroke   );
            putValue( TOOL_TIP_TEXT_KEY, sToolTip );
            putValue( SHORT_DESCRIPTION, sToolTip );
            putValue( LONG_DESCRIPTION , sToolTip );
        }
    }
}