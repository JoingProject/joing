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

package org.joing.kernel.swingtools.filesystem.fsviewer.fslist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionable;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionableCommonActions;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionableListener;

/**
 * A panel showing one icon per file and folder.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemList extends JList implements FileSystemActionable
{
    private boolean bShowHidden = false;
    private boolean bShowFiles  = true;
    
    private FileSystemActionableCommonActions fsaca = new FileSystemActionableCommonActions();
    
    private JPopupMenu  popup;  // See ::setComponentPopupMenu(...)
    
    //------------------------------------------------------------------------//
    
    public JoingFileSystemList()
    {
        super( new DefaultListModel() );
        
        setLayoutOrientation( JList.HORIZONTAL_WRAP );
        setVisibleRowCount( 0 );
        setCellRenderer( new FileCellRendererAsIcon() );
        addMouseListener( new MouseListener()
        {
            public void mousePressed( MouseEvent me )
            {
                if( me.isPopupTrigger() )
                    showPopup( me );
            }
            
            public void mouseReleased( MouseEvent me )   // Needed for Windows
            {
                if( me.isPopupTrigger() )
                    showPopup( me );
            }
            
            public void mouseClicked(  MouseEvent me )
            {
                if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 )
                {
                    File fSelected = getSelected();   // Checked now because can be a little slow
                    
                    if( fSelected != null )
                    {
                        fireOpen( fSelected );

                        if( fSelected.isDirectory() )
                            scan( fSelected );
                        else
                            JoingFileSystemList.this.fsaca.openFile( fSelected );
                    }
                }
            }
            
            private void showPopup( MouseEvent me )
            {
                int nIndex = locationToIndex( me.getPoint() );
                    
                if( (nIndex > -1) && (! isSelectedIndex( nIndex )) )
                    setSelectedIndex( nIndex );

                if( JoingFileSystemList.this.popup != null )
                    JoingFileSystemList.this.popup.show( JoingFileSystemList.this, me.getX(), me.getY() );
            }
            
            public void mouseEntered(  MouseEvent me )  {  }
            public void mouseExited(   MouseEvent me )  {  }
        } );
    }
    
    /**
     * It is needed to overwrite this method and handle isPopupTrigger() in a 
     * MouseListener: if we allow Java to handle the popup event (via 
     * setComponentPopupMenu(...)), then the MouseListener will <u>not</u> 
     * respond to right-click events.
     * <p>
     * Perhaps this is Sun's desired functionality or perhaps a bug.
     * 
     * @param popup A popup
     */
    public void setComponentPopupMenu( JPopupMenu popup )
    {
        this.popup = popup;
    }
    
    //------------------------------------------------------------------------//
    // Implementations for FileSystemActionable Interface
    
    public void setShowingFiles( boolean bShowFiles )
    {
        this.bShowFiles = bShowFiles;
    }
    
    public boolean isShowingFiles()
    {
        return bShowFiles;
    }

    public boolean isShowingHidden()
    {
        return bShowHidden;
    }

    public void setShowingHidden( boolean bShowHidden )
    {
        this.bShowHidden = bShowHidden;
    }
    
    public File getSelected()
    {
        File fReturn   = null;
        int  nSelected = getSelectedIndex();
        
        if( nSelected > -1 )
        {
            fReturn = (File) getModel().getElementAt( nSelected );
            
            if( getSelectedIndices().length > 1 )   // If there are more than one selected
                setSelectedIndex( nSelected );      // then makes the 1st one the only one selected.
        }
        
        return fReturn;
    }
    
    public void setSelected( File file )
    {
        if( file != null && file.exists() )
        {
            if( file.isDirectory() )
            {
                scan( file );
            }
            else
            {
                setSelectedFileInCurrentDir( file );
            }
        }
    }

    public void reloadAll()
    {
        // TODO: hacerlo
    }

    public void reloadSelected()
    {
        scan( getSelected() );
    }

    public void newFolder()
    {
        File f = fsaca.createFolder( getSelected() );
        
        if( f != null )
            ((DefaultListModel) getModel()).addElement( f );
    }

    public void newFile()
    {
        File f = fsaca.createFile( getSelected() );
        
        if( f != null )
            ((DefaultListModel) getModel()).addElement( f );
    }

    public void cut()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Cut: Option not yet implemented" );
    }
    
    public void copy()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Copy: Option not yet implemented" );
    }

    public void paste()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Paste: Option not yet implemented" );
    }

    public void delete()
    {// NEXT: Si son varios ficheros, habría que enviarlos todos a la vez (por si son VFSs)
        boolean bAll       = true;   // Where all selected files deleted?
        int[]   anSelected = getSelectedIndices();
        
        for( int n = 0; n < anSelected.length; n++ )
        {
            if( fsaca.delete( (File) getModel().getElementAt( n ) ) )
                getSelectionModel().removeIndexInterval( n, n );
            else
                bAll = false;
        }
        
        if( ! bAll )
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Error deleteing one or more files." );
    }

    public void rename()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Rename: Option not yet implemented" );
    }
    
    public void properties()
    {
        fsaca.properties( getSelected() );
    }
    
    public void toTrascan()
    {
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "To trashcan: Option not yet implemented" );
    }

    // TODO: Notificar los eventos a los listeners
    public void addListener( FileSystemActionableListener fsal )
    {
        listenerList.add( FileSystemActionableListener.class, fsal );
    }
    
    public void removeListener( FileSystemActionableListener fsal )
    {
        listenerList.remove( FileSystemActionableListener.class, fsal );
    }
    
    //------------------------------------------------------------------------//
    
    protected void fireOpen( File fSelected )
    {
        Object[] listeners = listenerList.getListenerList();   // Guaranteed to return a non-null array
        
        for( int n = listeners.length - 2; n >= 0; n -= 2 )
        {
            if( listeners[n] == FileSystemActionableListener.class )
                ((FileSystemActionableListener) listeners[n + 1]).open( fSelected );
        }
    }
    
    //------------------------------------------------------------------------//
    
    // Forces to scan passed file if is a dir or its parent if is a file.
    // f must exists and not be null
    private void scan( File f )
    {
        final File fToScan   = f.isFile() ? f.getParentFile() : f;
        final File fSelected = f.isFile() ? f : null;
        
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                ((DefaultListModel) getModel()).clear();
                ((DefaultListModel) getModel()).addElement( "Loading" );
            }
        } );
        
        SwingWorker sw = new SwingWorker<Void,File>()
        {
            protected Void doInBackground() throws Exception
            {
                publish( fsaca.getFilesIn( fToScan, isShowingFiles(), isShowingHidden() ) );
                return null;
            }

            protected void process( List<File> lstfiles )
            {
                DefaultListModel model = (DefaultListModel) getModel();
                                 model.clear();
                                 
                for( File f : lstfiles )
                    model.addElement( f );
                
                if( fSelected != null )
                    setSelectedFileInCurrentDir( fSelected );
            }
        };
        sw.execute();
    }
    
    // f is guaranted not to be null
    private void setSelectedFileInCurrentDir( File f )
    {
            // TODO: Hacerlo
    }
}