/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
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

package org.joing.kernel.swingtools.filesystem.fsviewer;

import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 * Set of <code>javax.swing.AbstractAction</code> to be used mainly to construct 
 * toolbars and popup menus.
 * <p>
 * To preservate independency of classes this one knows only about files. On the
 * other side, file systems and JoingFileSystemTree and JoingFileSystemList know
 * only about how to represent files.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileSystemActions
{    
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
    
    public FileSystemActions()
    {
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
     *           toolbar.addListener( new JToolBar.Separator() );
     *           popup.addListener( new JPopupMenu.Separator() );
     *       }
     *       else
     *       {
     *           toolbar.addListener( actions[n] );
     *           popup.addListener( actions[n] );
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
        if( actHome == null )
        {
            ImageIcon iconHome = new ImageIcon( getClass().getResource( "images/home.png" ) );
            
            actHome = new MyAction( "Home", iconHome.getImage(), 'H' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    File fHome = new File( System.getProperty( "user.home" ) );
                    ((MyAction) this).getInvoker( ae ).setSelected( fHome );
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
        if( actReload == null )
        {
            ImageIcon iconLoad = new ImageIcon( getClass().getResource( "images/reload.png" ));
            
            actReload = new MyAction( "Reload", iconLoad.getImage(), 'L', "Reload current node [Ctrl-L] (with Shift, whole tree)" )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    FileSystemActionable invoker = ((MyAction) this).getInvoker( ae );
                    
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
                    ((MyAction) this).getInvoker( ae ).newFolder();
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
                    ((MyAction) this).getInvoker( ae ).newFile();
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
                    ((MyAction) this).getInvoker( ae ).cut();
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
                    ((MyAction) this).getInvoker( ae ).copy();
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
                    ((MyAction) this).getInvoker( ae ).paste();
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
        if( actRename == null )
        {
            ImageIcon iconRename = new ImageIcon( getClass().getResource( "images/rename.png" ));
            
            actRename = new MyAction( "Rename", iconRename.getImage(), 'R' )
            {
                public void actionPerformed( ActionEvent ae )
                {
                    // After renaming, TreeModelListener::treeNodesChanged() is invoked
                    ((MyAction) this).getInvoker( ae ).rename();
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
                    if( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                           showYesNoDialog( "Confirm deletion", "Deleted entities can't be recovered.\nPlease confirm to delete." ) )
                    {
                        ((MyAction) this).getInvoker( ae ).delete();
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
                    ((MyAction) this).getInvoker( ae ).properties();
                }
            };
        }
        
        return actProperties;
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
    
    //------------------------------------------------------------------------//
    // INNER CLASS
    //------------------------------------------------------------------------//
    
    private abstract class MyAction extends AbstractAction
    {
        private MyAction( String sText, StandardImage img, char character )
        {
            this( sText, 
                  org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( img ),
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
            putValue( SHORT_DESCRIPTION, sToolTip );   // Used for tooltips
        }
        
        // The tree or list that showed up the JPopupMenu (both implement FileSystemActionable)
        private FileSystemActionable getInvoker( ActionEvent ae )
        {
            JMenuItem  item  = (JMenuItem)  ae.getSource();
            JPopupMenu popup = (JPopupMenu) item.getParent();
            
            return ((FileSystemActionable) popup.getInvoker());
        }
    }
}