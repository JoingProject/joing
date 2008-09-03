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

package org.joing.kernel.swingtools;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import org.joing.kernel.api.kernel.jvmm.ApplicationExecutionException;
import org.joing.kernel.api.desktop.DeskComponent;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.api.desktop.StandardImage;
import org.joing.kernel.api.desktop.deskwidget.deskLauncher.DeskLauncher;
import org.joing.kernel.api.desktop.workarea.WorkArea;
import org.joing.kernel.runtime.vfs.JoingFileSystemView;
import org.joing.kernel.swingtools.filesystem.fsviewer.FSExplorerPanel;

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
            icon = new ImageIcon( org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getRuntime().getImage( StandardImage.NO_IMAGE ) );
        
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
     * Scales passed image to new size keeping the aspect (ratio between width
     * and height)
     * 
     * @param icon Icon to scale.
     * @param newWidth New width.
     * @param newHeight New height.
     * @param observer The observer needed to scale.
     * @return Scaled icon.
     */
//    public ImageIcon scaleImageKeepingAspect( ImageIcon icon, int newWidth, int newHeight, ImageObserver observer )
//    {
//        Image  image   = icon.getImage();
//        int    nWidth  = image.getWidth( observer );
//        int    nHeight = image.getHeight( observer );
//        double nRatio  = 1.0;
//        
//        if( nWidth >= nHeight )
//        {
//            nRatio = (double) (nCOMP_SIZE - 5) / nWidth;
//            nWidth = nCOMP_SIZE - 5;
//            nHeight = (int) (nHeight * nRatio);
//        }
//        else
//        {
//            if( getHeight() > 150 )
//            {
//                nRatio = (double) (nCOMP_SIZE - 5) / nHeight;
//                nHeight = nCOMP_SIZE - 5;
//                nWidth = (int) (nWidth * nRatio);
//            }
//            else
//            {
//                nRatio = (double) getHeight() / nHeight;
//                nHeight = getHeight();
//                nWidth = (int) (nWidth * nRatio);
//            }
//        }
//
//        image = image.getScaledInstance( nWidth, nHeight, Image.SCALE_DEFAULT );
//        
//        return new ImageIcon( image );
//    }
    
    public static synchronized WorkArea findWorkAreaFor( DeskComponent comp )
    {
        List<WorkArea> lstWA = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas();
        
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
                if( sTarget != null && sTarget.trim().length() > 0 )
                    launchApp( sTarget.trim(), sArguments );
                break;
                
             case DIRECTORY:
                 if( sTarget != null && sTarget.trim().length() > 0 )
                    launchFolder( sTarget.trim() );
                 break;
        }
    }
    
    //------------------------------------------------------------------------//
    
    private static void launchApp( final String sApp, final String sArgs )
    {
        DesktopManager dm        = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager();
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
                
                 if( nApp > -1)
                 {
                    String[] asArgs = null;

                    if( sArgs != null )
                         asArgs = parseFiles( sArgs );
                    
                    try
                    {         
                        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().start( nApp, asArgs, System.out, System.err );
                    }
                    catch( ApplicationExecutionException exc )
                    {
                        org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                                getRuntime().showException( exc, null );
                    }
                }
                
                return null;
            }

            protected void done()
            {
                org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                        getDesktop().hideNotification( nIdNote );
            }
        };
        
        sw.execute();
    }
    
    private static void launchFolder( String sFile )
    {
        if( sFile.charAt( 0 ) == '"' )
            sFile = sFile.substring( 1 );
                
        if( sFile.charAt( sFile.length() - 1 ) == '"' )
            sFile = sFile.substring( 0, sFile.length() - 1 );
        
        File fTarget = JoingFileSystemView.getFileSystemView().createFileObject( sFile );
        
        if( fTarget.exists() )
        {
            (new FSExplorerPanel( fTarget )).showInFrame();
        }
        else
        {
            org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getDesktopManager().
                    getRuntime().showMessageDialog( "Error opening folder", 
                                                    "Requested folder\n["+ sFile +"]\ndoes not exists.");
        }
    }
    
    /**
     * For example, recieving:
     *     /home/peyrona/file1 "/home/peyrona/another file" /home/peyrona/file3 /home/peyrona/file4
     * will return:
     *     { /home/peyrona/file1, /home/peyrona/another file, /home/peyrona/file3, /home/peyrona/file4 }
     * 
     * @param sFiles File names to pase (guaranteed to be not null)
     * @return An array of file names
     */
    private static String[] parseFiles( String sFiles )
    {
        ArrayList<String> list     = new ArrayList<String>( 16 );
        StringBuilder     sbFile   = new StringBuilder( 256 );
        boolean           bInQuote = false;
        
        sFiles = sFiles.trim();
        
        for( int n = 0; n < sFiles.length(); n++ )
        {
            char c = sFiles.charAt( n );
            
            if( c == '"' )
            {
                bInQuote = ! bInQuote;
            }
            else if( c == ' ' && ! bInQuote )
            {
                sbFile.setLength( 0 );
                
                if( sbFile.length() > 0 )
                    list.add( sbFile.toString() );
            }
            else
            {
                sbFile.append( c );
            }
        }
        
        if( sbFile.length() > 0 )
            list.add( sbFile.toString() );
        
        return list.toArray( new String[0] );
    }
}