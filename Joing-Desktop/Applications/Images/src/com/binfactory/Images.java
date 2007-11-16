/*
 * (c) Telco Domótica S.L.
 * Todos los derechos reservados.
 * 
 * Creado el 03-sep-2005 a las 11:44:21
 */

package com.binfactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.binfactory.client.desktop.DesktopDialog;
import com.binfactory.client.desktop.DesktopFrame;
import com.binfactory.client.runtime.Runtime;

/**
 * A simple image viewer.
 * 
 * @author Francisco Morero Peyrona
 */
public class Images extends JPanel
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
    }
    
    //------------------------------------------------------------------------//
    // THIS CLASS API 
    
    public void addTab( URL url ) throws IOException 
    {
        String sName  = url.getPath();
        int    nIndex = sName.lastIndexOf( '/' );
        
        if( nIndex > -1 )
            sName = sName.substring( nIndex + 1 );
        
        addTab( sName, ImageIO.read( url ) );
    }
    
    public void addTab( String sName, Image image )
    {
        if( sName == null )
            sName = "Noname";
        
        WImage is = new WImage( image );
        JScrollPane   sp = new JScrollPane( is );
                      sp.getViewport().setScrollMode( JViewport.BLIT_SCROLL_MODE );
                      sp.setWheelScrollingEnabled( true );
        
        tabs.addTab( sName, Runtime.getIcon( this, "images/image.png", 16,16 ), sp );
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
        // TODO hacerlo
        Images.this.toolbar.updateButtons();
    }
    
    public void Print()    
    {
        // TODO hacerlo
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
                                           "An images viwer application.</div>"+
                                           "<p>Developed by <i>Francisco Morero Peyrona</i></p></body></html>" );
        JPanel        panel  = new JPanel( new BorderLayout( 10,10 ) );
                      panel.add( new JLabel( Runtime.getIcon( this, "images/images.png" ) ), BorderLayout.WEST );
                      panel.add( label, BorderLayout.CENTER );
        DesktopDialog dialog = new DesktopDialog( "About", panel, new String[] { "Ok" }, 0 );
                      dialog.setVisible( true );
    }
    
    //------------------------------------------------------------------------//
    // Application entering point
    
    public static void main( String[] asArg ) throws IOException
    {
        Images viewer = new Images();
               viewer.addTab( Images.class.getResource( "images/images.png" ) );

        DesktopFrame df = new DesktopFrame( "Images" );
                     df.setFrameIcon( Runtime.getIcon( viewer, "images/images.png", 16,16 ) );
                     df.add( viewer );
                     df.pack();
                     
        Runtime.getDesktop().add( df );
        df.center();   // After it was added to the desktop
        
        /*javax.swing.JDesktopPane desktop = new javax.swing.JDesktopPane();
                                 desktop.add( viewer, new Integer( 2 ) );
                                 
        viewer.setLocation( 180,90 );
        viewer.setVisible( true );
                     
        javax.swing.JFrame frame = new javax.swing.JFrame( "NotePad Test" );
                           frame.setContentPane( desktop );
                           frame.setSize( 1024, 768 );
                           frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
                           frame.setVisible( true );*/
    }
}