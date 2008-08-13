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
package org.joing.runtime.swap;

import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingFileChooserPreviewImage extends JLabel implements PropertyChangeListener
{   // Next: Add support for BMP images
    private final static String sRENDERIZABLES = ".jpg.jpeg.gif.png.tif.tiff";
    private final static int     ACC_SIZE      = 155;

    public JoingFileChooserPreviewImage()
    {
        setPreferredSize( new Dimension( ACC_SIZE, -1 ) );
    }
    
    public void propertyChange( PropertyChangeEvent pce )
    {
        String propertyName = pce.getPropertyName();

        // Make sure we are responding to the right event.
        if( propertyName.equals( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ) )
        {
            File fSelection = (File) pce.getNewValue();
            
            if( fSelection != null && fSelection.exists() && fSelection.isFile() )
            {
                FileFilter filter = getFilter();
                String     sPath  = fSelection.getAbsolutePath();
                
                if( sPath != null && filter.accept( fSelection ) )
                {
                    ImageIcon icon = new ImageIcon( sPath );  // FIXME: Mirar qué pasa cuando el file es VFSFile
                    setIcon( scaleImage( icon ) );
                }
            }
        }
    }
    
    /**
     * Return a filter to filter image files.
     * 
     * @return A filter to filter image files.
     */
    public static FileFilter getFilter()
    {
        return new FileFilter() 
        {            
            public boolean accept( File f )
            {
                if( f.isDirectory() )
                    return true;
                
                String sPath  = f.getAbsolutePath();
                int    nIndex = sPath.lastIndexOf( '.' );
                String sExt   = null;

                if( nIndex > -1 )
                    sExt = sPath.toLowerCase().substring( nIndex );

                 return (sExt != null && sRENDERIZABLES.indexOf( sExt ) > -1);
            }
            
            public String getDescription()
            {
                return "Image files";
            }
        };
    }
    
    //------------------------------------------------------------------------//
    
    private ImageIcon scaleImage( ImageIcon icon )
    {
        Image  image   = icon.getImage();
        int    nWidth  = image.getWidth( this );
        int    nHeight = image.getHeight( this );
        double nRatio  = 1.0;
        
        /* 
         * Determine how to scale the image. Since the accessory can expand
         * vertically make sure we don't go larger than 150 when scaling
         * vertically.
         */
        if( nWidth >= nHeight )
        {
            nRatio = (double) (ACC_SIZE - 5) / nWidth;
            nWidth = ACC_SIZE - 5;
            nHeight = (int) (nHeight * nRatio);
        }
        else
        {
            if( getHeight() > 150 )
            {
                nRatio = (double) (ACC_SIZE - 5) / nHeight;
                nHeight = ACC_SIZE - 5;
                nWidth = (int) (nWidth * nRatio);
            }
            else
            {
                nRatio = (double) getHeight() / nHeight;
                nHeight = getHeight();
                nWidth = (int) (nWidth * nRatio);
            }
        }

        image = image.getScaledInstance( nWidth, nHeight, Image.SCALE_DEFAULT );
        
        return new ImageIcon( image );
    }
}