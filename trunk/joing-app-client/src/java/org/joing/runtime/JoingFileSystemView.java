/*
 * JoingFileSystemView.java
 *
 * Created on 05-ago-2007, 12:06:14
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 * Common class that internally handles one <code>FileSystemView</code> instance
 * for local OS (in case that Join'g is not running as unsigned applet) and one
 * <code>FileSystemView</code> for the remote VFS (and instance of 
 * <code>VFSView</code>).
 * 
 * @author Francisco Morero Peyrona
 */
public class JoingFileSystemView extends FileSystemView
{
    private FileSystemView local;
    private FileSystemView remote;
    
    private static final JoingFileSystemView instance = new JoingFileSystemView();
    
    //------------------------------------------------------------------------//
    
    public static FileSystemView getFileSystemView()
    {
        return instance;
    }
    
    @Override
    public File[] getRoots()
    {
        URI uri = null;
        
        try
        {
            uri = new URI( "webdav://peyrona:admin@192.168.1.9:8080/apps/editor.jar" );
        }
        catch( Exception exc )
        {
            JOptionPane.showMessageDialog( null, exc.getLocalizedMessage() );
        }
        
        ArrayList<File> roots = new ArrayList<File>();
                        roots.add( new File( uri ) );
                        //roots.add( new File( "Join'g" ) );
                        
        
        //for( File f : local.getRoots() )
        //    roots.add( f );
        
        File[] ret = new File[ roots.size() ];
        return roots.toArray( ret );
    }
    
    public File createNewFolder( File containingDir ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    //------------------------------------------------------------------------//
    
    private JoingFileSystemView()
    {
        local  = FileSystemView.getFileSystemView();
        remote = VFSView.getFileSystemView();
    }
}