/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.swingtools;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.joing.common.clientAPI.jvmm.ApplicationExecutionException;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.desktopAPI.StandardImage;
import org.joing.common.desktopAPI.deskwidget.deskLauncher.DeskLauncher;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.swingtools.filesystem.fsviewer.FSExplorerPanel;

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
    public static synchronized byte[] icon2ByteArray( ImageIcon icon )
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
    public static synchronized ImageIcon getIcon( Object invoker, String sName )
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
    public static synchronized ImageIcon getIcon( Object invoker, String sName, int nWidth, int nHeight  )
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
    public static synchronized Cursor createCursor( String sImageName, Point pHotSpot, String sName )
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
    public static synchronized void launch( DeskLauncher dl )
    {
        launch( dl.getType(), dl.getTarget(), dl.getArguments() );
    }
    
    /**
     * 
     * @param type    See DeskLauncher
     * @param sTarget See DeskLauncher
     * @param sArguments Arguments to be passed if any, otherwise null.
     */
    public static synchronized void launch( DeskLauncher.Type type, String sTarget, String sArguments )
    {
        if( sTarget == null || sTarget.length() == 0 )
            return;
        
        switch( type )
        {
            case APPLICATION:
                launchApp( sTarget );
                break;
                
             case DIRECTORY:
                 (new FSExplorerPanel( new File( sTarget ) )).showInFrame();
                 break;
        }
    }
    
    public static synchronized WorkArea findWorkAreaFor( DeskComponent comp )
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
    
    //------------------------------------------------------------------------//
    
    private static void launchApp( final String sApp )
    {
        DesktopManager dm        = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
        Image          imgLaunch = dm.getRuntime().getImage( StandardImage.LAUNCHER );
        final int      nIdNote   = dm.getDesktop().showNotification( "Launching application", imgLaunch );
        
        // Normally, launching applications is done from inside the EDT, a good place to invoke 
        // showNotification(...), but a bad place to invoke Platform.
        // That is why a SwingWorker() is created to invoke Platform.
        
        SwingWorker sw = new SwingWorker() 
        {
            protected Object doInBackground() throws Exception
            {
                int nApp = -1;
                
                try{ nApp = Integer.valueOf( sApp ); }
                catch( NumberFormatException nfe ) { }
                
                try
                { 
                    if( nApp > -1)
                    {
                        org.joing.jvmm.RuntimeFactory.getPlatform().start( nApp );
                    }
                    else
                    {    
                        // TODO: Hacerlo: lanzar la app por su nombre
                        //    org.joing.jvmm.RuntimeFactory.getPlatform().start( 
                    }
                }
                catch( ApplicationExecutionException exc )
                {
                    org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                            getRuntime().showException( exc, null );
                }
                
                return null;
            }

            protected void done()
            {
                org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                        getDesktop().hideNotification( nIdNote );
            }
        };
        
        sw.execute();
    }
}