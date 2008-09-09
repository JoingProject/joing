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

package org.joing.kernel.runtime.vfs;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import org.joing.kernel.api.desktop.StandardImage;

/**
 * Assigns a default icon for passed file.<br>
 * The return icon is based on several criterias: isRoot(), isDirectory(), ...
 * And file type: text, binary, packed, ...
 * <p>
 * The temptation would we to store in this class all images (a cache) to make
 * loading icons faster and consuming as low memory as possible; but among other 
 * things, this class returns icons in different sizes, therofore, each class
 * that uses VFSIconMapper has to decibe about make its own cache or not.
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSIconMapper
{   // NEXT: Add more file extensions 
    
    // Note1: When this array changes, changes nust be done in VFSIconMapper.Icon enum
    // Note2: I feel today like playing with arrays (they are fast and small)
    private static final String[][] mapImgExt =
    { { ".bin.exe.bat.sh",                "binary"        },
      { ".png.jpg.jpeg.bmp.tiff.gif.svg", "image"         },
      { ".java.jar.jnlp.ear.war",         "java"          },
      { ".xls.xlt.ods.",                  "office_calc"   },
      { ".ppt.odp",                       "office_impact" },
      { ".doc.odt",                       "office_writer" },
      { ".zip.rar.deb.rmp.cab",           "packed"        },
      { ".pdf",                           "pdf"           },
      { ".ogg.mp3.au",                    "sound"         },
      { ".txt.text",                      "text"          },
      { ".wmv.avi",                       "video"         },
      { ".html.htm.css.url",              "web"           } };
    
    private static final int FS_LOCAL_ROOT  = -1;
    private static final int FS_REMOTE_ROOT = -2;
    private static final int FOLDER         = -3;
    private static final int STANDARD_FILE  = -4;
    
    private Dimension dimSize;
    
    //------------------------------------------------------------------------//
    
    public VFSIconMapper()
    {
        dimSize = null;
    }
    
    public VFSIconMapper( int nWidth, int nHeight )
    {
        dimSize = new Dimension( Math.max( nWidth , Math.min( nWidth , 9999 ) ), 
                                 Math.max( nHeight, Math.min( nHeight, 9999 ) ) );
    }
    
    public Integer getIconId( File file )
    {
        if( file.isDirectory() )
        {
            return FOLDER;
        }
        else if( JoingFileSystemView.getFileSystemView().isRoot( file ) )
        {
            return ((file instanceof VFSFile) ? FS_REMOTE_ROOT : FS_LOCAL_ROOT);
        }
        else
        {
            int nId = getIdForFileExtension( file );
            
            return (nId == -1 ? STANDARD_FILE : nId);
        }
    }
    
    /**
     * Returns associated icon to passed file.<br>
     * This is a shortcut for <code>getIcon( getIconId( file ) )</code>
     * <p>
     * Note: if frecuet calls to this method are expected from certain piece,
     * then it is recomended to create a images caché, what can be easily done
     * using ::getIconId( file ) in combinatio with ::getIcon( int ).
     * 
     * @param file File to get its icon.
     * @return Associated icon to passed file.
     * @see #getIcon(int)
     * @see #getIcon(int)
     */
    public ImageIcon getIcon( File file )
    {
        return getIcon( getIconId( file ) );
    }
    
    public ImageIcon getIcon( int nIconId )
    {
        ImageIcon icon = null;
        
        if( nIconId >= 0 && nIconId <= mapImgExt.length )
        {
            String sFileName = "images/"+ mapImgExt[nIconId][1] +".png";
            
            icon = new ImageIcon( getClass().getResource( sFileName ) );
        }
        else
        {
            switch( nIconId )
            {
                case FS_LOCAL_ROOT :
                    icon = new ImageIcon( getClass().getResource( "images/fs_local.png"  ) );
                    break;
                case FS_REMOTE_ROOT:
                    icon = new ImageIcon( getClass().getResource( "images/fs_remote.png" ) );
                    break;
                case FOLDER: 
                    icon = new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                                              getRuntime().getImage( StandardImage.FOLDER ) );
                    break;
                case STANDARD_FILE:
                    icon = new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                                              getRuntime().getImage( StandardImage.FILE ) );
                    break;
            }
        }
        
        if( icon == null )
        {
            icon = new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                                      getRuntime().getImage( StandardImage.NO_IMAGE ) );
        }
        
        if( dimSize != null && 
            (icon.getIconWidth()  != dimSize.width ||
             icon.getIconHeight() != dimSize.height) )
        {
            icon = new ImageIcon( icon.getImage().getScaledInstance( dimSize.width, dimSize.height, 
                                                                     Image.SCALE_SMOOTH ) );
        }
        
        return icon;
    }
    
    //------------------------------------------------------------------------//
    
    private int getIdForFileExtension( File file )
    {
        // NEXT: Would be better to resolve by MimeType than by file extension?
        // Look here: http://www.feedforall.com/mime-types.htm
        int nIndex = file.getName().lastIndexOf( '.' );
        
        if( (nIndex > 0) && (nIndex < file.getName().length() - 1) )   // (! endsWidth( "." ))
        {
            String sExt = file.getName().substring( nIndex ).toLowerCase();
            
            for( int n = 0; n < mapImgExt.length; n++ )
            {
                if( mapImgExt[n][0].indexOf( sExt ) > -1 )
                    return n;
            }
        }
        
        return -1;
    }
}