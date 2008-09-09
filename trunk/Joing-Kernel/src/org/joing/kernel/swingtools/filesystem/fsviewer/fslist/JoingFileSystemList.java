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
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionable;
import org.joing.kernel.swingtools.filesystem.fsviewer.FileSystemActionableDelegated;
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
    
    private FileSystemActionableDelegated fsaca = new FileSystemActionableDelegated();
    
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
                        if( fSelected.isDirectory() )
                            scan( fSelected );
                        else
                            JoingFileSystemList.this.fsaca.openFile( fSelected );
                        
                        JoingFileSystemList.this.fsaca.fireOpen( fSelected );
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
        reloadSelected();   // Both (all & selected) are the same: current folder
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
        // TODO: Hacerlo
        fsaca.cut( null );
    }
    
    public void copy()
    {
        // TODO: Hacerlo
        fsaca.copy( null );
    }

    public void paste()
    {
        // TODO: Hacerlo
        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
        showMessageDialog( null, "Paste: Option not yet implemented" );
    }

    public void delete()
    {
        int[]      anSelected = getSelectedIndices();   // Receives an empty array in case of empty selection
        List<File> lstSelect  = new ArrayList<File>( anSelected.length );
        List<File> lstErrors  = null;
        
        for( int n = 0; n < anSelected.length; n++ )
            lstSelect.add( (File) getModel().getElementAt( n ) );
        
        lstErrors = fsaca.delete( lstSelect );  // receive those files that were not deleted
        
        // Removes from JList only those files that were successfully deleted
        for( int n = 0; n < anSelected.length; n++ )
        {
            File file = (File) getModel().getElementAt( n );
            
            if( ! lstErrors.contains( file ) )
                getSelectionModel().removeIndexInterval( n, n );
        }
        
        if( lstErrors.size() > 0 )
        {
            StringBuilder sb = new StringBuilder( 1024*4 );
            
            for( File file : lstErrors )
                sb.append( '\n' ).append( file.getAbsolutePath() );
            
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Error deleting following files:"+ sb.toString() );
        }
    }
    
    public void rename()
    {
        // TODO: Hacerlo
        fsaca.rename( null, null );
    }
    
    public void properties()
    {
        int[]      anSelected = getSelectedIndices();   // Receives an empty array in case of empty selection
        List<File> lstFiles   = new ArrayList<File>( anSelected.length );
        
        for( int n = 0; n < anSelected.length; n++ )
            lstFiles.add( (File) getModel().getElementAt( n ) );
        
        fsaca.properties( lstFiles );
    }
    
    public void toTrascan()
    {
        fsaca.toTrashcan( null );
    }
    
    // TODO: Notificar los eventos a los listeners
    public void addListener( FileSystemActionableListener fsal )
    {
        fsaca.addListener( fsal );
    }
    
    public void removeListener( FileSystemActionableListener fsal )
    {
        fsaca.addListener( fsal );
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