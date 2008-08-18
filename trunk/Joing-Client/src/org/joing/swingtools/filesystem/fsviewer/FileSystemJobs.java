/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.  * Join'g Team Members are listed at project's home page. By the time of   * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.swingtools.filesystem.fsviewer;

import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.runtime.vfs.JoingFileSystemView;
import org.joing.runtime.vfs.VFSFile;

/**
 * To preservate independency of classes this one knows only about files and
 * file systems and JoingFileSystemTree knows only about to represent files.
 * <p>
 * This class is used by classes implementing FileSystemActionable interface.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileSystemJobs
{
    private boolean    bShowHidden = false;
    private JPopupMenu popup       = null;    // One shared instance per class instance to save memory
    
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
    
    public FileSystemJobs()
    {
    }
    
    /**
     * To show hidden files and folders.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>false</code>
     * @param bShowHidden
     */
    public void setShowingHidden( boolean bShowHidden )
    {
        this.bShowHidden = bShowHidden;
    }
    
    /**
     * Is showing hidden folders and files?
     * 
     * @return <code>true</code> if is showing hidden folders and files.
     */
    public boolean isShowingHidden()
    {
        return bShowHidden;
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.setSelectedFile( new File( System.getProperty( "user.home" ) ) );
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    if( (ae.getModifiers() & ActionEvent.SHIFT_MASK) != 0 )
                        invoker.reloadAll();
                    else
                        invoker.reloadSelected();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.createFolder();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.createFile();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.cut();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.copy();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                                         invoker.paste();
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                    
                    invoker.rename();   // After renaming, TreeModelListener::treeNodesChanged() is invoked
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
                    if( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                           showYesNoDialog( "Confirm deletion", "Deleted entities can't be recovered.\nPlease confirm to delete." ) )
                    {
                        FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                                             invoker.deleteAllSelected();
                    }
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
                    FileSystemActionable invoker = (FileSystemActionable) FileSystemJobs.this.popup.getInvoker();
                                         invoker.properties();
                }
            };
        }
        
        return actProperties;
    }
    
    //------------------------------------------------------------------------//
    // Editing methods
    
    public File createFolder( File fParent )
    {
        return _create( fParent, true );
    }
    
    public File createFile( File fParent )
    {
        return _create( fParent, false );
    }
    
    public boolean delete( File file )
    {
        return ((file instanceof VFSFile) ? file.delete() : deepDeleteLocalFS( file ));
    }
    
    // Retrieve all files in passed dir or null if none
    public File[] getFilesIn( File fParent, final boolean bShowFiles )
    {
        // List files, hidden are not included
        File[] afChildren = fParent.listFiles( new FileFilter() 
        {
            public boolean accept( File f )
            {
                return (bShowFiles || f.isDirectory()) && (bShowHidden || ! f.isHidden());
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
    
    //------------------------------------------------------------------------//
    
    private File _create( File fParent, boolean bFolder )
    {
        File fRet = null;
        
        try
        {
            if( bFolder )
                fRet = JoingFileSystemView.getFileSystemView().createNewFolder( fParent );
            else
                fRet = JoingFileSystemView.getFileSystemView().createNewFile( fParent );
        }
        catch( IOException exc )
        {
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
        }
        
        return fRet;
    }
    
    public void updateActionsEnableStatus( File fSelected )
    {
        boolean  bIsRoot     = true;
        boolean  bAlterable  = true;
        boolean  bDeleteable = true;
        
        if( fSelected != null  )
        {
            bIsRoot = JoingFileSystemView.getFileSystemView().isRoot( fSelected );
            
            if( fSelected instanceof VFSFile )
            {
                bAlterable  = ((VFSFile) fSelected).isAlterable();
                bDeleteable = ((VFSFile) fSelected).isDeleteable();
            }
        }
        
      //if( actHome != null )    Always enabled
      //    actHome.setEnabled( );
            
        if( actReload != null )
            actReload.setEnabled( fSelected != null );
        
        if( actNewFolder != null )
            actNewFolder.setEnabled( (fSelected != null) && fSelected.isDirectory() );
        
        if( actNewFile != null )
            actNewFile.setEnabled( (fSelected != null) && fSelected.isDirectory() );
        
        if( actCut != null )
            actCut.setEnabled( ((fSelected != null) || (! bIsRoot)) && bDeleteable );
        
        if( actCopy != null )
            actCopy.setEnabled( (fSelected != null) || (! bIsRoot) );
        
        if( actPaste != null )
            actPaste.setEnabled( false );   // TODO: hay que implemetar el clipboard para activar el paste
        
        if( actRename != null )
            actRename.setEnabled( ((fSelected != null) && (! bIsRoot)) && bAlterable );
        
        if( actDelete != null )
            actDelete.setEnabled( ((fSelected != null) && (! bIsRoot)) && bDeleteable );
        
        if( actProperties != null )
            actProperties.setEnabled( (fSelected != null) );
    }
    
    public JPopupMenu getPopupMenu()
    {
        if( popup == null )
        {
            popup = new JPopupMenu();
            
            AbstractAction[] actions = getActions();   

            for( int n = 0; n < actions.length; n++ )
            {
                if( actions[n] == null )
                    popup.add( new JPopupMenu.Separator() );
                else
                    popup.add( actions[n] );
            }
        }
        
        return popup;
    }
    
    // If passed file is a directory, recursively deletes it an all its files.
    // If passed file is just a file, deletes it.
    private boolean deepDeleteLocalFS( java.io.File f )
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
                        bSuccess = bSuccess && deepDeleteLocalFS( files[n] );
                    else
                        bSuccess = bSuccess && files[n].delete();
                }
            }
            
            bSuccess = bSuccess && f.delete();
        }
        
        return bSuccess;
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
            
            KeyStroke stroke = KeyStroke.getKeyStroke( character, Event.CTRL_MASK );
            
            if( sToolTip == null )
                sToolTip = sText +" [Ctrl-"+ Character.toString( character ) +"]";
            
            putValue( SMALL_ICON       , new ImageIcon( image.getScaledInstance( 18, 18, Image.SCALE_SMOOTH ) ) );
            putValue( LARGE_ICON_KEY   , new ImageIcon( image.getScaledInstance( 24, 24, Image.SCALE_SMOOTH ) ) );
            putValue( ACCELERATOR_KEY  , stroke   );
            ///putValue( TOOL_TIP_TEXT_KEY, sToolTip );
            putValue( SHORT_DESCRIPTION, sToolTip );
            putValue( LONG_DESCRIPTION , sToolTip );
        }
    }
}