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

package org.joing.runtime.vfs;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.joing.common.desktopAPI.StandardImage;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class VFSIconMapper
{
    // Images are static to save memory and increase speed
    private static Image imgFS_LOCAL      = null;
    private static Image imgFS_REMOTE     = null;
    private static Image imgLOCKED        = null;
    
    private static Image imgBINARY        = null;
    private static Image imgIMAGE         = null;
    private static Image imgJAVA          = null;
    private static Image imgOFFICE_CALC   = null;
    private static Image imgOFFICE_IMPACT = null;
    private static Image imgOFFICE_WRITER = null;
    private static Image imgPACKED        = null;
    private static Image imgPDF           = null;
    private static Image imgSOUND         = null;
    private static Image imgTEXT          = null;
    private static Image imgVIDEO         = null;
    private static Image imgWEB           = null;
    //----------------------------------------------------
    
    // Only one instance of this class
    private static final Map<String,Image> map = new HashMap<String, Image>();
    
    static
    {
        getSwingWorkerImagesLoad().execute();
    }
    
    private Dimension dimSize;
    
    //------------------------------------------------------------------------//
    
    public VFSIconMapper()
    {
        dimSize = null;
    }
    
    public VFSIconMapper( int nWidth, int nHeight )
    {
        dimSize = new Dimension( Math.max( nWidth, 5 ), Math.max( nHeight, 5 ) );
    }
    
    public ImageIcon getIcon( File file )
    {
        if( file.isDirectory() )
        {            
            return _getIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                               getRuntime().getImage( StandardImage.FOLDER ) );
        }
        else if( JoingFileSystemView.getFileSystemView().isRoot( file ) )
        {
            return _getIcon( ((file instanceof VFSFile) ? imgFS_REMOTE : imgFS_LOCAL) );
        }
        else
        {
            // NEXT: Sería mejor hacerlo por su MimeType que por la extensión
            // Mirar aquí : http://www.feedforall.com/mime-types.htm
            int nIndex = file.getName().lastIndexOf( '.' ) + 1;
            
            if( (nIndex > 0) && (nIndex < file.getName().length() - 1) )
            {
                String sExt = file.getName().substring( nIndex ).toLowerCase();
                
                if( map.containsKey( sExt) )
                    return _getIcon( map.get( sExt ) );
            }
        }
        
        return _getIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                                 getRuntime().getImage( StandardImage.FILE ) );
    }
    
    //------------------------------------------------------------------------//
    
    private ImageIcon _getIcon( Image image )
    {
        ImageIcon icon = null;
        
        if( image == null )
            image = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.NO_IMAGE );
        
        icon = new ImageIcon( image );   // At this point, image is guaranted to be not null
        
        if( dimSize != null && 
            (icon.getIconWidth()  != dimSize.width ||
             icon.getIconHeight() != dimSize.height) )
            icon = new ImageIcon( image.getScaledInstance( dimSize.width, dimSize.height, Image.SCALE_SMOOTH ) );
        
        return icon;
    }
    
    // A SwingWorker to load images in a background task
    private static SwingWorker getSwingWorkerImagesLoad()
    {
        return new SwingWorker()
        {
            protected Void doInBackground() throws Exception
            {
                imgFS_LOCAL      = new ImageIcon( getClass().getResource( "images/fs_local.png"      ) ).getImage();
                imgFS_REMOTE     = new ImageIcon( getClass().getResource( "images/fs_remote.png"     ) ).getImage();
                imgLOCKED        = new ImageIcon( getClass().getResource( "images/locked.png"        ) ).getImage();
                
                imgBINARY        = new ImageIcon( getClass().getResource( "images/binary.png"        ) ).getImage();
                imgIMAGE         = new ImageIcon( getClass().getResource( "images/image.png"         ) ).getImage();
                imgJAVA          = new ImageIcon( getClass().getResource( "images/java.png"          ) ).getImage();
                imgOFFICE_CALC   = new ImageIcon( getClass().getResource( "images/office_calc.png"   ) ).getImage();
                imgOFFICE_IMPACT = new ImageIcon( getClass().getResource( "images/office_impact.png" ) ).getImage();
                imgOFFICE_WRITER = new ImageIcon( getClass().getResource( "images/office_writer.png" ) ).getImage();
                imgPACKED        = new ImageIcon( getClass().getResource( "images/packed.png"        ) ).getImage();
                imgPDF           = new ImageIcon( getClass().getResource( "images/pdf.png"           ) ).getImage();
                imgSOUND         = new ImageIcon( getClass().getResource( "images/sound.png"         ) ).getImage();
                imgTEXT          = new ImageIcon( getClass().getResource( "images/text.png"          ) ).getImage();
                imgVIDEO         = new ImageIcon( getClass().getResource( "images/video.png"         ) ).getImage();
                imgWEB           = new ImageIcon( getClass().getResource( "images/web.png"           ) ).getImage();
                
                return null;
            }
            
            protected void done()
            {    
                // TODO: poner más extensiones y cambiar el icono de fs_local y fs_remote
                map.put( "bin" , imgBINARY );
                map.put( "exe" , imgBINARY );
                map.put( "bat" , imgBINARY );
                map.put( "sh"  , imgBINARY );
                map.put( "png" , imgIMAGE );
                map.put( "jpg" , imgIMAGE );
                map.put( "jpeg", imgIMAGE );
                map.put( "bmp" , imgIMAGE );
                map.put( "tiff", imgIMAGE );
                map.put( "gif" , imgIMAGE );
                map.put( "svg" , imgIMAGE );
                map.put( "java", imgJAVA );
                map.put( "jar" , imgJAVA );
                map.put( "jnlp", imgJAVA );
                map.put( "ear" , imgJAVA );
                map.put( "war" , imgJAVA );
                map.put( "xls" , imgOFFICE_CALC );
                map.put( "xlt" , imgOFFICE_CALC );
                map.put( "ods" , imgOFFICE_CALC );
                map.put( "ppt" , imgOFFICE_IMPACT );
                map.put( "odp" , imgOFFICE_IMPACT );
                map.put( "doc" , imgOFFICE_WRITER );
                map.put( "odt" , imgOFFICE_WRITER );
                map.put( "zip" , imgPACKED );
                map.put( "rar" , imgPACKED );
                map.put( "deb" , imgPACKED );
                map.put( "rmp" , imgPACKED );
                map.put( "cab" , imgPACKED );
                map.put( "pdf" , imgPDF );
                map.put( "ogg" , imgSOUND );
                map.put( "mp3" , imgSOUND );
                map.put( "au"  , imgSOUND );
                map.put( "txt" , imgTEXT );
                map.put( "text", imgTEXT );
                map.put( "wmv" , imgVIDEO );
                map.put( "avi" , imgVIDEO );
                map.put( "html", imgWEB );
                map.put( "htm" , imgWEB );
                map.put( "css" , imgWEB );
                map.put( "url" , imgWEB );
            }
        };
    }
}