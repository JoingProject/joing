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

package org.joing.kernel.swingtools.filesystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import org.joing.kernel.runtime.vfs.VFSFile;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class JoingFileChooserPreviewImage extends JPanel implements PropertyChangeListener
{   // Next: Add support for BMP images
    private final static String sRENDERIZABLES = ".jpg.jpeg.gif.png.tif.tiff";
    private final static int    nIMAGE_SIZE    = 160;

    private JLabel       lblPreview;
    private JFileMaxSize fmsLocal;
    private JFileMaxSize fmsRemote;
    
    private FileFilter filter = getFilter();

    //------------------------------------------------------------------------//
    
    public JoingFileChooserPreviewImage()
    {
        lblPreview = new JLabel();
        lblPreview.setPreferredSize( new Dimension( nIMAGE_SIZE , nIMAGE_SIZE ) );
        fmsLocal   = new JFileMaxSize( "Max local (in Kb) " , "Maximum size for local files in Kb" , 512 );
        fmsRemote  = new JFileMaxSize( "Max remote (in Kb) ", "Maximum size for remote files in Kb",  64 );

        setBorder( new EmptyBorder( 0, 5, 0, 5 ) );
        setLayout( new BorderLayout( 0, 5 ) );
        add( fmsRemote , BorderLayout.NORTH  );
        add( lblPreview, BorderLayout.CENTER );
        add( fmsLocal  , BorderLayout.SOUTH  );
        setPreferredSize( new Dimension( nIMAGE_SIZE + 60, nIMAGE_SIZE ) );
    }
    
    public void propertyChange( PropertyChangeEvent pce )
    {
        String propertyName = pce.getPropertyName();

        // Make sure we are responding to the right event.
        if( propertyName.equals( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY ) )
        {
            File fSelection = (File) pce.getNewValue();
            
            if( fSelection != null && fSelection.exists() && fSelection.isFile() &&
                filter.accept( fSelection ) && isValidSize( fSelection ) )
            {
                // TODO: hay que leer el fichero apropiadamente (esto sólo sirve para los locales, no para lso remotos)
                String sPath = fSelection.getAbsolutePath();
                
                if( sPath != null )
                {
                    ImageIcon icon = new ImageIcon( sPath );
                    lblPreview.setIcon( scaleImage( icon ) );
                }
            }
            else
            {
                lblPreview.setIcon( null );
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
            nRatio = (double) (nIMAGE_SIZE - 5) / nWidth;
            nWidth = nIMAGE_SIZE - 5;
            nHeight = (int) (nHeight * nRatio);
        }
        else
        {
            if( getHeight() > 150 )
            {
                nRatio = (double) (nIMAGE_SIZE - 5) / nHeight;
                nHeight = nIMAGE_SIZE - 5;
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
    
    boolean isValidSize( File file )
    {
        return (file instanceof VFSFile && file.length() <= (fmsRemote.getFileMaxSize() * 1024)
               ||
               (file.length() <= (fmsLocal.getFileMaxSize()*1024)));
    }
    
    //------------------------------------------------------------------------//
    
    private final class JFileMaxSize extends JPanel
    {
        private JLabel   label   = new JLabel();
        private JSpinner spinner = new JSpinner();
                
        private JFileMaxSize( String sText, String sTooltip, int nSize )
        {
            setLayout( new GridBagLayout() );
            
            label.setText( sText );
            GridBagConstraints gbc1 = new GridBagConstraints();
                               gbc1.gridx  = 0;
                               gbc1.gridy  = 0;
                               gbc1.fill   = GridBagConstraints.NONE;
                               gbc1.anchor = GridBagConstraints.LAST_LINE_START;
            add( label, gbc1 );
            
            spinner.setModel( new SpinnerNumberModel( nSize, 0, 1024*1024*2, 10 ) ); // Max == 2 Gb (in Kb)
            spinner.setPreferredSize( new Dimension( 60, 20 ) );
            spinner.setToolTipText( sTooltip );
            GridBagConstraints gbc2 = new GridBagConstraints();
                               gbc2.gridx  = 1;
                               gbc2.gridy  = 0;
                               gbc2.fill   = GridBagConstraints.HORIZONTAL;
                               gbc2.anchor = GridBagConstraints.LAST_LINE_END;
            add( spinner, gbc2 );
        }
        
        private int getFileMaxSize()
        {
            return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
        }
    }
}