/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde.joingswingtools;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.joing.common.clientAPI.jvmm.ApplicationExecutionException;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.workarea.WorkArea;

/**
 * Extra functions used internally by PDE.
 * <p>
 * <b>NOTE: </b>If an application uses these functions, it will not be a portable 
 * (cross-desktops) Joing application.
 *
 * @author Francisco Morero Peyrona
 */
public class JoingSwingUtilities
{    
    public static byte[] icon2ByteArray( ImageIcon icon )
    {
        byte[] image = null;
        
        try
        {
            BufferedImage bufferedImage = new BufferedImage( icon.getIconWidth(),
                                                             icon.getIconHeight(),
                                                             BufferedImage.TYPE_INT_ARGB );
                          bufferedImage.createGraphics().drawImage( icon.getImage(), 0, 0, null );
                          
            ByteArrayOutputStream baos = new ByteArrayOutputStream( 1024 * 64 );
            ImageIO.write( bufferedImage, "png", baos );
            baos.flush();
            image = baos.toByteArray();
            baos.close();
        }
        catch( IOException exc )
        {
            // Nothing to do
        }
        
        return image;
    }
    
    /**
     * Return an icon which location is relative to passed class.
     * 
     * @param invoker The class to be used as base to find the files. 
     * @param sName   Name of file with its extension.
     * @return        The icon or the standard one <code>StandardImage.NO_IMAGE</code>
     *                if the requested was not found.
     */
    public static ImageIcon getIcon( Object invoker, String sName )
    {
        URL       url  = null;
        ImageIcon icon = null;
        
        if( sName != null && sName.length() > 0 )
        {
            if( sName.indexOf( ".png" ) == -1 )
                sName += ".png";
            
            if( invoker instanceof Class )
                url = ((Class) invoker).getResource( sName );
            else
                url = invoker.getClass().getResource( sName );
            
            if( url != null )
                icon = new ImageIcon( url );
        }
        
        if( icon == null )
            icon = new ImageIcon( org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.NO_IMAGE ) );
        
        return icon;
    }
    
    /**
     * Return an icon (with specific dimension) which location is relative to 
     * passed class.
     * 
     * @param invoker The class to be used as base to find the files.
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one <code>StandardImage.NO_IMAGE</code>
     *                if the requested was not found.
     */
    public static ImageIcon getIcon( Object invoker, String sName, int nWidth, int nHeight  )
    {
        ImageIcon icon = getIcon( invoker, sName  );
        
        if( icon.getIconWidth() != nWidth || icon.getIconHeight() != nHeight )
            icon.setImage( icon.getImage().getScaledInstance( nWidth, nHeight, Image.SCALE_SMOOTH ) );
        
        return icon;
    }
    
    /**
     * Creates a custom cursor asuming that the image is under cursor 
     * application directory.
     * 
     * @param sImageName
     * @param pHotSpot
     * @param sName
     * @return
     */
    public static Cursor createCursor( String sImageName, Point pHotSpot, String sName )
    {
        if( sImageName.indexOf( '.' ) == -1 )
            sImageName += ".png";
        
        Image image = getIcon( null, "cursors/"+ sImageName ).getImage();
        
        return Toolkit.getDefaultToolkit().createCustomCursor( image, pHotSpot, sName );        
    }
    
    /**
     * 
     * @param dl
     */
    public static void launch( DeskLauncher dl )
    {
        launch( dl.getType(), dl.getTarget(), dl.getArguments() );
    }
    
    /**
     * 
     * @param type    See DeskLauncher
     * @param sTarget See DeskLauncher
     * @param sArguments Arguments to be passed if any, otherwise null.
     */
    public static void launch( DeskLauncher.Type type, String sTarget, String sArguments )
    {
        DesktopManager dm = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        
        switch( type )
        {
            case APPLICATION:
                Image launch = dm.getRuntime().getImage( StandardImage.LAUNCHER );
                int nId = dm.getDesktop().showNotification( "Launching application", launch, true );
                
                try
                {
                    org.joing.jvmm.RuntimeFactory.getPlatform().start( Integer.valueOf( sTarget ) );
                }

                catch( ApplicationExecutionException exc )
                {
                    dm.getRuntime().showException( exc, null );
                }                catch( NumberFormatException nfe )
                {
                    // If it is not an app Id (a number), it has to be an app name
                    // TODO: hacerlo
                }
                finally
                {
                    dm.getDesktop().hideNotification( nId );   // TODO: Invocar hide cuando la app haya finalizado de mostrarse (quizás sólo lo sepa Platform)
                }
                break;
                
             case DIRECTORY:
                 // TODO: hacerlo
                 break;
        }
    }
    
    //------------------------------------------------------------------------//
    
    public static WorkArea findWorkAreaFor( DeskComponent comp )
    {
        List<WorkArea> lstWA = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas();
        
        for( WorkArea wa : lstWA )
        {
            Component[] ac = ((java.awt.Container) wa).getComponents();
            
            for( int n = 0; n < ac.length; n++ )
            {
                if( ac[n] == comp )
                    return wa;
            }
        }
        
        return null;
    }   
}