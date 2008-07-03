/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.nasaphoto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.pane.DeskFrame;
import org.joing.pde.desktop.deskwidget.desklet.PDEDesklet;

/**
 * DeskletApplet example: load and show pictures from NASA.
 * 
 * @author Francisco Morero Peyrona
 */
public class NasaPhoto extends PDEDesklet implements Runnable
{
    private final static int nMIN_DELAY = 5000;   // 5 secs
    private final static String sBASE_URL = "http://antwrp.gsfc.nasa.gov/apod/";
    private List<String> lstPhotos;    // Small references to the "pictures of the day" index page. 
    private JLabel lblPhoto;
    private Image image;               // Full size image (just in case user wants to see it)
    private int nDelay;                // Minimum delay between 2 consecutive pictures (in milli-secs). 
    // Maximum delay depends on connection speed and picture size.
    private boolean bContinue;
  
    //------------------------------------------------------------------------//
    public NasaPhoto()
    {
        lstPhotos = new ArrayList<String>();

        lblPhoto = new JLabel( "NASA photos" );
        lblPhoto.setForeground( Color.white );
        lblPhoto.setHorizontalAlignment( JLabel.CENTER );
        lblPhoto.setToolTipText( "NASA astronomy pictures carousel" );
        lblPhoto.addMouseListener( new MouseAdapter()
           {
               public void mouseClicked( MouseEvent me )
               {
                   if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 )
                       showImage();
               }
           } );
        add( lblPhoto );

        setDelay( 60 );   // 1 min

        setOpaque( true );
        setBackground( Color.black );
        setBorder( new LineBorder( Color.gray ) );
        setMinimumSize( new Dimension( 80, 60 ) );
        setMaximumSize( new Dimension( 800, 600 ) );
        setPreferredSize( new Dimension( 160, 120 ) );
    }

    public void onShow()
    {
        play();
    }

    public void onGrow()
    {
        showImage();
        toogleSizeButton();
    }

    public void onSetup()
    {
        DesktopManagerFactory.getDM().getRuntime().showMessageDialog( null, "Option not yet implemented" );
        // TODO: hacerlo
    }

    public void play()
    {
        (new Thread( this )).start();
    }

    public void pause()
    {
        bContinue = false;
    }

    public int getDelay()
    {
        return nDelay / 1000;
    }

    public void setDelay( int nSecs )
    {
        nDelay = Math.max( nSecs * 1000, nMIN_DELAY );
    }

    public void run()
    {
        Thread.currentThread().setPriority( Thread.MIN_PRIORITY );
        bContinue = connect();      // Creating the URL should be inside the Thread
        long nNow = 0;

        if( bContinue )
            lblPhoto.setText( "Getting picture..." );
            
        while( bContinue )
        {
            try
            {
                if( System.currentTimeMillis() - nNow >= nDelay )
                {
                    nNow = System.currentTimeMillis();
                    image = ImageIO.read( getNextImageURL() );
                    final ImageIcon icon = new ImageIcon( image.getScaledInstance( getSize().width, getSize().height, Image.SCALE_DEFAULT ) );

                    if( icon != null )
                    {
                        SwingUtilities.invokeLater( new Runnable()
                        {
                            public void run()
                            {
                                lblPhoto.setText( null );
                                lblPhoto.setIcon( icon );
                            }
                        } );
                    }
                }
                else
                {
                    Thread.sleep( nMIN_DELAY );
                }
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------------------//
    private boolean connect()
    {
        URL url = null;

        try
        {
            lblPhoto.setText( "Connecting to NASA..." );
            url = new URL( sBASE_URL + "archivepix.html" );

            // Load all (over 5.000) "Astronomy Picture of the Day" page names
            Reader input = new InputStreamReader( url.openStream() );
            char buf[] = new char[1024 * 4];
            StringBuffer sbPage = new StringBuffer( 500 * 1024 );

            while( true )
            {
                int n = input.read( buf );

                if( n < 0 )
                    break;

                sbPage.append( buf, 0, n );
            }

            Pattern pattern = Pattern.compile( "a\\s+href\\s*=\\s*\"(ap.+?([?].+?)?)\"" );
            Matcher matcher = pattern.matcher( sbPage );

            while( matcher.find() )
            {
                String sName = matcher.group( 1 );
                lstPhotos.add( sName.substring( 0, sName.indexOf( '.' ) ) );   // Removes ".html" to save memory
            }
        }
        catch( Exception exc )
        {
            url = null;    // Must exists
            lblPhoto.setText( "Error in connection" );
        }

        return (url != null);
    }

    private URL getNextImageURL() throws Exception
    {
        int nPhoto = (int) (Math.random() * lstPhotos.size());

        // The web page for the selected day (the image is inside this page)
        URL urlWebPage = new URL( sBASE_URL + lstPhotos.get( nPhoto ) + ".html" );

        // Now we load the page, and search inside it for the image
        Reader input = new InputStreamReader( urlWebPage.openStream() );
        char buf[] = new char[1024];
        StringBuffer sbPage = new StringBuffer( 1024 * 10 );

        while( true )
        {
            int n = input.read( buf );

            if( n < 0 )
                break;

            sbPage.append( buf, 0, n );
        }

        Pattern pattern = Pattern.compile( "<IMG SRC=\"(.*)\"" );
        Matcher matcher = pattern.matcher( sbPage );
                matcher.find();

        return new URL( sBASE_URL + matcher.group( 1 ) );
    }

    private void showImage()
    {
        if( image != null )
        {
            MyScrollPane sp = new MyScrollPane( new JLabel( new ImageIcon( image ) ) );
            Image icon = null;
            
            try
            {
                icon = ImageIO.read( getClass().getResource( "nasa_logo.png" ) );
            }
            catch( IOException exc )
            {
                // Nothing to do
            }
            
            DeskFrame frame = DesktopManagerFactory.getDM().getRuntime().createFrame();
                      frame.setTitle( "NASA Astronomy Picture" );
                      frame.setIcon( icon );
                      frame.add( (DeskComponent) sp );

            DesktopManagerFactory.getDM().getDesktop().getActiveWorkArea().add( frame );
        }
    }

    private class MyScrollPane extends JScrollPane implements DeskComponent
    {
        private MyScrollPane( JComponent comp )
        {
            super( comp );
        }
    }
}