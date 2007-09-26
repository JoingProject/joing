/*
 * DefaultFileLauncher.java
 * 
 * Created on 11-sep-2007, 14:23:20
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.impl.desktop.workarea;

import java.io.FileDescriptor;
import org.joing.api.desktop.workarea.desklet.deskLauncher.FileLauncher;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultFileLauncher extends DefaultLauncher 
                                 implements FileLauncher
{
    // TODO: terminar de implementarla
    
    private FileDescriptor fd;

    //------------------------------------------------------------------------//
    
    public DefaultFileLauncher()
    {
        super();
    }
    
    public FileDescriptor getFileDescriptor()
    {
        return fd;
    }

    public void setFileDescriptor( FileDescriptor fd )
    {
        this.fd = fd;
    }
}