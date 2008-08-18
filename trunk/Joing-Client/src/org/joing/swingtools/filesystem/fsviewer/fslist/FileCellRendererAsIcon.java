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

package org.joing.swingtools.filesystem.fsviewer.fslist;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.joing.runtime.vfs.VFSIconMapper;
import org.joing.swingtools.JRoundLabel;
import org.joing.swingtools.JoingSwingUtilities;

/**
 *
 * @author Francisco Morero Peyrona
 */
class FileCellRendererAsIcon extends JRoundLabel implements ListCellRenderer
{    
    public FileCellRendererAsIcon()
    {
        setFont( getFont().deriveFont( Font.PLAIN ) );
        setBorder( new EmptyBorder( 3,3,3,3 ) );
        setHorizontalTextPosition( JLabel.CENTER );
        setVerticalTextPosition( JLabel.BOTTOM );
    }
    
    public Component getListCellRendererComponent( JList list, Object value, int index, 
                                                   boolean isSelected, boolean hasFocus )
    {
        ImageIcon icon;
        String    sName;
        
        if( value instanceof String )
        {
            icon  = JoingSwingUtilities.getIcon( this, "images/loading.png", 40, 40 );
            sName = value.toString();
        }
        else
        {
            File file  = (File) value;
            icon = (new VFSIconMapper( 40,40 )).getIcon( file );
            sName = file.getName();
        }
        
        setOpaque( isSelected );
        setText( ((sName.length() > 9) ? sName.substring( 0, 8 ) : sName) );
        setIcon( icon );
        
        if( isSelected )
        {
            setForeground( UIManager.getColor( "List.selectionForeground" ) );
            setBackground( UIManager.getColor( "List.selectionBackground" ) );
        }
        else
        {
            setForeground( UIManager.getColor( "List.textForeground" ) );
        }
        
        return this;
    }
}