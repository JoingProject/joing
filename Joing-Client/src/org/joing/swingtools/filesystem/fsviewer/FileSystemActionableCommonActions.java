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

package org.joing.swingtools.filesystem.fsviewer;

import java.io.File;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.runtime.vfs.VFSFile;
import org.joing.swingtools.filesystem.JVFSPropertiesPanel;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class FileSystemActionableCommonActions
{
    public static void properties( File file )
    {
        DeskComponent panel = null;
        DeskFrame     frame = org.joing.jvmm.RuntimeFactory.getPlatform().
                                    getDesktopManager().getRuntime().createFrame();
                      frame.setTitle( "Virtual File Properties" );
        
        if( file instanceof VFSFile )
        {
            panel = new JVFSPropertiesPanel( ((VFSFile) file).getFileDescriptor() );
        }
        else
        {
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().
                showMessageDialog( null, "Option not yet implemented" );
        }
        
        if( panel != null )
        {
            frame.add( panel );
            org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                    getDesktop().getActiveWorkArea().add( frame );
        }
    }
}