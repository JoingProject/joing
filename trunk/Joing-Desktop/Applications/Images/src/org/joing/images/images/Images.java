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
package org.joing.images.images;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.common.dto.vfs.VFSFile4IO;
import org.joing.runtime.swap.JoingFileChooser;
import org.joing.runtime.swap.JoingFileChooserPreviewImage;
import org.joing.runtime.vfs.VFSFile;
import org.joing.swingtools.JoingPanel;

/**
 * A simple image viewer.
 * 
 * @author Francisco Morero Peyrona
 */
public class Images extends JPanel implements DeskComponent
{
    private JTabbedPane tabs;
    private ToolBar     toolbar;
    private StatusBar   status;
    
    //------------------------------------------------------------------------//
    
    public Images()
    {
        toolbar = new ToolBar( this );
        status  = new StatusBar();
        tabs    = new JTabbedPane();
        tabs.addChangeListener( new ChangeListener() 
            {
                public void stateChanged( ChangeEvent e )
                {
                    WImage image = Images.this.getSelectedImage(); 
                 
                    Images.this.toolbar.updateButtons();
                    Images.this.status.setOriginalSize( image );
                    Images.this.status.setRotation( image );
                    Images.this.status.setScale( image );
                }
            } );
        
        setPreferredSize( new Dimension( 640,480 ) );
        setLayout( new BorderLayout() );
        add( toolbar, BorderLayout.NORTH  );
        add( tabs   , BorderLayout.CENTER );
        add( status , BorderLayout.SOUTH  );
        Images.this.toolbar.updateButtons();
    }
    
    public void showInFrame()
    {
        DesktopManager dm   = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        ImageIcon      icon = new ImageIcon( getClass().getResource( "images/images.png" ) );
        
        if( dm != null )
        {
            // Show this panel in a frame created by DesktopManager Runtime.
            DeskFrame frame = dm.getRuntime().createFrame();
                      frame.setTitle( "Images" );
                      frame.setIcon( icon.getImage() );
                      frame.add( (DeskComponent) this );
                      
            dm.getDesktop().getActiveWorkArea().add( frame );
        }
        else
        {
            javax.swing.JFrame frame = new javax.swing.JFrame();
                               frame.add( this );
                               frame.pack();
                               frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                               frame.setVisible( true );
        }
    }
    
    //------------------------------------------------------------------------//
    // THIS CLASS API 
    
    public void addImage( URL url ) throws IOException 
    {
        String sName  = url.getPath();
        int    nIndex = sName.lastIndexOf( '/' );
        
        if( nIndex > -1 )
            sName = sName.substring( nIndex + 1 );
        
        addImage( sName, ImageIO.read( url ) );
    }
    
    public void addImage( String sName, InputStream is )
    {
        byte[] abImage  = new byte[0];
        byte[] abBuffer = new byte[1024*8];
        int    nReaded  = 0;
        
        try
        {
            while( nReaded != -1 )
            {
                nReaded = is.read( abBuffer );
                
                if( nReaded != -1 )
                {
                    // Create a temp array with enought space for btContent and new bytes readed
                    byte[] abTemp = new byte[ abImage.length + nReaded ];
                    // Copy btContent to new array
                    System.arraycopy( abImage, 0, abTemp, 0, abImage.length  );
                    // Copy readed bytes at tail of new array
                    System.arraycopy( abBuffer, 0, abTemp, abImage.length, nReaded );
                    // Changes btContent reference to temp array
                    abImage = abTemp;
                }
            }
            
            is.close();
        }
        catch( IOException exc )
        {
            abImage = null;
        }
        
        if( abImage != null )
        {
            ImageIcon icon = new ImageIcon( abImage );
            addImage( sName, icon.getImage() );
        }
    }
    
    public void addImage( String sName, Image image )
    {
        if( sName == null )
            sName = "Noname";
        
        Image       imgThumb = image.getScaledInstance( 18, 18, Image.SCALE_DEFAULT );
        JScrollPane sp       = new JScrollPane( new WImage( image ) );
                    sp.getViewport().setScrollMode( JViewport.BLIT_SCROLL_MODE );
                    sp.setWheelScrollingEnabled( true );
        
        tabs.addTab( sName, new ImageIcon( imgThumb ), sp );
        tabs.setSelectedIndex( tabs.getTabCount() - 1 );
        
        toolbar.updateButtons();
    }
    
    public WImage getSelectedImage()
    {
        WImage image = null;
        JScrollPane   sp    = (JScrollPane) tabs.getSelectedComponent();
        
        if( sp != null )
            image = (WImage) sp.getViewport().getView();
        
        return image;
    }
    
    public void closeSelectedTab()
    {
        int nIndex = tabs.getSelectedIndex();
        
        tabs.removeTabAt( nIndex );
        
        if( tabs.getTabCount() > 0 )
            tabs.setSelectedIndex( nIndex - 1 );
    }
    
    public void setHelp( JComponent comp )
    {
        this.status.setHelp( comp );
    }
    
    // Available actions
    
    public void Open()
    {
        JoingFileChooser jfc = new JoingFileChooser();
                         jfc.setAcceptAllFileFilterUsed( false );
                         jfc.addChoosableFileFilter( JoingFileChooserPreviewImage.getFilter() );
                                 
        if( jfc.showDialog( this ) == JoingFileChooser.APPROVE_OPTION )
        {
            File fImage = jfc.getSelectedFile();
            
            try
            {
                if( fImage instanceof VFSFile )
                {
                    VFSFile4IO vfs4IO = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().
                            getFileBridge().getFile( ((VFSFile) fImage).getFileDescriptor() );

                    addImage( fImage.getName(), vfs4IO.getByteReader() );
                }
                else
                {
                    addImage( fImage.getName(), new FileInputStream( fImage ) );
                }
            }
            catch( IOException exc )
            {
                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                        getRuntime().showException( exc, "Error opening file" );
            }
            
            Images.this.toolbar.updateButtons();
        }
    }
    
    public void Print()    
    {
        JOptionPane.showMessageDialog( this, "Option not yet implemented." );
    }
    
    public void RotateLeft()    
    {
        getSelectedImage().incrRotation( -90 );
        Images.this.status.setRotation( getSelectedImage() );
    }
    
    public void RotateRight()    
    {
        getSelectedImage().incrRotation( 90 );
        Images.this.status.setRotation( getSelectedImage() );
    }
    
    public void ZoomIn()    
    {
        getSelectedImage().incrZoom( 10 );
        Images.this.toolbar.updateButtons();
        Images.this.status.setScale( getSelectedImage() );
    }
    
    public void ZoomOut()    
    {
        getSelectedImage().incrZoom( -10 );
        Images.this.toolbar.updateButtons();
        Images.this.status.setScale( getSelectedImage() );
    }
    
    public void Zoom100()    
    {
        getSelectedImage().incrZoom( 0 );   // 0 == original size 
        Images.this.toolbar.updateButtons();
        Images.this.status.setScale( getSelectedImage() );
    }
    
    public void ZoomStretch()    
    {
        getSelectedImage().setStretched( ! getSelectedImage().isStretched() );
        Images.this.toolbar.updateButtons();
        Images.this.status.setScale( getSelectedImage() );
    }
    
    public void Close()    
    {
        closeSelectedTab();
        Images.this.toolbar.updateButtons();
    }
    
    public void About()    
    {
        JLabel        label  = new JLabel( "<html><body><b><div align=\"center\">Images</b><p>"+
                                           "An images viewer application.<p>"+
                                           "<p>Developed by<br> <i>Francisco Morero Peyrona</i></p></div></body></html>" );
        
        ImageIcon     icon   = new ImageIcon( getClass().getResource( "images/images.png" ) ); 
        
        JoingPanel    panel  = new JoingPanel();
                      panel.setLayout( new BorderLayout( 10,10 ) );
                      panel.add( new JLabel( icon ), BorderLayout.WEST );
                      panel.add( label, BorderLayout.CENTER );
                      panel.setBorder( new EmptyBorder( 7,7,7,7 ) );
        
        DeskDialog dialog = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().createDialog();
                   dialog.setTitle( "About Notes" );
                   dialog.add( (DeskComponent) panel );
                   
        if( icon != null )
            dialog.setIcon( icon.getImage() );

        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );
    }
    
    //------------------------------------------------------------------------//
    // Application entering point
    
    public static void main( String[] asArg ) throws IOException
    {
        (new Images()).showInFrame();
    }
}