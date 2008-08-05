/*
 * Copyright (C) 2007, 2008 Join'g Team Members.  All Rights Reserved.
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

package org.joing.swingtools.fsviewer.fslist;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.joing.swingtools.fsviewer.FileSystemActionable;
import org.joing.swingtools.fsviewer.FileSystemJobs;

/**
 * A panel showing one icon per file and folder.
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemList extends JList implements FileSystemActionable
{
    private boolean         bShowFiles = true;
    private FileSystemJobs fsworks    = null;
    private File            fCurrent   = null;
    
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
                int nIndex = locationToIndex( me.getPoint() );
                
                // As no button is cheked, it works for all btns
                if( (nIndex > -1) && (! isSelectedIndex( nIndex )) )
                    setSelectedIndex( nIndex );
            }
            public void mouseReleased( MouseEvent me )  {  }
            public void mouseClicked(  MouseEvent me )  {  }
            public void mouseEntered(  MouseEvent me )  {  }
            public void mouseExited(   MouseEvent me )  {  }
        } );
    }
    
    //------------------------------------------------------------------------//
    
    public void setFileSystemWorks( FileSystemJobs fsworks )
    {
        this.fsworks = fsworks;
        setComponentPopupMenu( this.fsworks.getPopupMenu() );
    }
    
    /**
     * To show only folders or folders and files.
     * Invoking this method affect only when loading new nodes.
     * 
     * By default is <code>true</code>
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
    // Implementations for FileSystemActionable Interface
    
    public File getSelectedFile()
    {
        File fReturn   = null;
        int  nSelected = getSelectedIndex();
        
        if( nSelected > -1 )
        {
            fReturn = (File) getModel().getElementAt( nSelected );
            
            if( getSelectedIndices().length > 1 )
                setSelectedIndex( nSelected );
        }
        
        return fReturn;
    }
    
    public boolean setSelectedFile( File file )
    {
        if( file == null || ! file.exists() )
            return false;
        
        if( file.isDirectory() && file != fCurrent )   // Must use != instead of .equals(...)
        {
            fCurrent = file.getAbsoluteFile();    // Defensive copy
            
            ((DefaultListModel) getModel()).clear();
            ((DefaultListModel) getModel()).addElement( "Loading" );
            
            SwingWorker sw = new SwingWorker<Void,File>()
            {
                protected Void doInBackground() throws Exception
                {
                    publish( fsworks.getFilesIn( fCurrent, isShowingFiles() ) );
                    return null;
                }
                
                protected void process( List<File> lstfiles )
                {
                    DefaultListModel model = (DefaultListModel) getModel();
                                     model.clear();
                                     
                    for( File f : lstfiles )
                        model.addElement( f );
                }
            };
            sw.execute();
        }
        else
        {
            // TODO: poner highlight el fichero
        }
        
        return true;
    }

    public void reloadAll()
    {
        reloadSelected();
    }

    public void reloadSelected()
    {
        setSelectedFile( fCurrent );
    }

    public void createFolder()
    {
        fsworks.createFolder( fCurrent );
    }

    public void createFile()
    {
        fsworks.createFile( fCurrent );
    }

    public void cut()
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
    }
    
    public void copy()
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
    }

    public boolean paste()
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
        return false;
    }

    public boolean deleteAllSelected()
    {
        boolean bAll       = true;   // Where all selected files deleted?
        int[]   anSelected = getSelectedIndices();
        
        for( int n = 0; n < anSelected.length; n++ )
        {
            if( fsworks.delete( (File) getModel().getElementAt( n ) ) )
                getSelectionModel().removeIndexInterval( n, n );
            else
                bAll = false;
        }
        
        return bAll;
    }

    public void rename()
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
    }

    public boolean properties()
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
        return false;
    }
}