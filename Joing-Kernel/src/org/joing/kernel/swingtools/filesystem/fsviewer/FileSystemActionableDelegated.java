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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.pane.DeskFrame;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.runtime.vfs.VFSFile;
import org.joing.kernel.swingtools.JoingApplicationChooser;
import org.joing.kernel.swingtools.JoingSwingUtilities;

/**
 * Actions that work the same for JoingFileSystemTree, JoingFileSystemList and 
 * Toolbar (classes that implement FileSystemActinable interface).
 * <p>
 * Even if this class executes several actions, the methods here do not fire
 * event notifications to listeners. This is so because in this way classes that 
 * invoke these methods can decide if they wnat to fire or not the event and 
 * when.
 * 
 * @author Francisco Morero Peyrona
 */
public class FileSystemActionableDelegated
{
    private EventListenerList listenerList = new EventListenerList();
    
    //------------------------------------------------------------------------//
    
    /*public void reloadAll()
    {
        // Selects root for hihglighted FS (local o remote)
            
        reloadSelected();
    }
        
    public void reloadSelected()
    {
        File folder = tree.getSelected();

        if( ! folder.isDirectory() )
        {

        }

        if( folder != null )
            fsaca.getFilesIn( folder, tree.isShowingFiles(), tree.isShowingHidden() );
    }*/
    
    public File createFolder( File fParent )
    {
        return _create( fParent, true );
    }
    
    public File createFile( File fParent )
    {
        return _create( fParent, false );
    }
    
    public List<File> delete( List<File> files )
    {
        List<File> listErrors = new ArrayList<File>();
        
        if( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                           showYesNoDialog( "Confirm deletion", "Deleted entities can't be recovered.\nPlease confirm to delete." ) )
        {     
            for( File f : files )
            {   // TODO: Rellenar el array de errores
                if( f instanceof VFSFile )
                    f.delete();
                else
                    deepDeleteLocalFS( f );
            }
        }
        
        return listErrors;
    }
    
    public boolean rename( File file, String sNewName )
    {
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Rename: Option not yet implemented" );
        
        return false;
    }
    
    public void properties( List<File> files )
    {
        for( File f : files )
        {
            DeskFrame frame = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().
                                      getDesktopManager().getRuntime().createFrame();
                      frame.setTitle( (f instanceof VFSFile ? "Remote" : "Local") +" File Properties" );
                      frame.add( new JFilePropertiesPanel( f ) );
                          
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                getDesktop().getActiveWorkArea().add( frame );
        }
    }
    
    public void cut( List<File> lstFile )
    {
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Cut: Option not yet implemented" );
    }
    
    public void copy( List<File> lstFile )
    {
        // TODO: hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Copy: Option not yet implemented" );
    }
    
    public List<File> toTrashcan( List<File> lstFile )
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "To trashcan: Option not yet implemented" );
        
        return null;
    }
    
    // Retrieve all files in passed dir or null if none
    public File[] getFilesIn( File fParent, final boolean bShowFiles, final boolean bShowHidden )
    {
        File[] afChildren = new File[0];
        
        if( fParent != null )
        {
            afChildren = fParent.listFiles( new FileFilter() 
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
        }
        
        return afChildren;
    }
    
    /**
     * 
     * @param f File to be opened: can't be null.
     */
    public void openFile( File file )
    {
        if( file != null )
        {
            AppDescriptor appDesc = null;
            int           nIndex  = file.getName().lastIndexOf( '.' );

            if( nIndex > 0 )
            {
                String sFileExtension = file.getName().substring( nIndex );

                appDesc = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge().
                                        getAppBridge().getPreferredForType( sFileExtension );
            }

            if( appDesc == null )
            {
                org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                    showMessageDialog( "Open file", "There is no application associated with this type of file." );

                AppDescriptor app = JoingApplicationChooser.showDialog();
            }

            if( appDesc != null )
            {
                JoingSwingUtilities.launch( DeskLauncher.Type.APPLICATION, 
                                            String.valueOf( appDesc.getId() ), 
                                            "\""+ file.getAbsolutePath() +"\"" );  // "" are only needed when file name contains spaces
            }
        }
    }
    
    //------------------------------------------------------------------------//
    
    private File _create( File fParent, boolean bFolder )
    {
        File fRet = null;
        
        if( fParent != null )
        {
            try
            {
                if( bFolder )
                    fRet = JoingFileSystemView.getFileSystemView().createNewFolder( fParent );
                else
                    fRet = JoingFileSystemView.getFileSystemView().createNewFile( fParent );
            }
            catch( IOException exc )
            {
                org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().showException( exc, null );
            }
        }
        
        return fRet;
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
    // EVENT MANAGEMENT
    // Note: See class documentation
    
    public void addListener( FileSystemActionableListener fsal )
    {
        listenerList.add( FileSystemActionableListener.class, fsal );
    }
    
    public void removeListener( FileSystemActionableListener fsal )
    {
        listenerList.remove( FileSystemActionableListener.class, fsal );
    }
    
    public void fireSelectionChanged( File fSelected )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].selectionChanged( fSelected );
    }
    
    public void fireFolderCreated( File fCreated )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].folderCreated( fCreated );
    }
    
    public void fireFileCreated( File fCreated )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].fileCreated( fCreated );
    }
    
    public void fireCutted( List<File> cutted )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].cutted( cutted );
    }
    
    public void fireCopied( List<File> copied )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].copied( copied );
    }
    
    public void firePasted( List<File> pasted )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].pasted( pasted );
    }
    
    public void fireDeleted( List<File> deleted )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].deleted( deleted );
    }
    
    public void fireMovedToTrashcan( List<File> trashcan )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].movedToTrashcan( trashcan );
    }
    
    public void fireRenamed( File fRenamed )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].renamed( fRenamed );
    }
    
    public void firePropertiesChanged( File fChanged )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].propertiesChanged( fChanged );
    }
    
    public void fireOpen( File fToOpen )
    {   // Guaranteed to return a non-null array
        FileSystemActionableListener[] listeners = listenerList.getListeners( FileSystemActionableListener.class );
        
        for( int n = 0; n < listeners.length; n++ )
            listeners[n].open( fToOpen );
    }
}