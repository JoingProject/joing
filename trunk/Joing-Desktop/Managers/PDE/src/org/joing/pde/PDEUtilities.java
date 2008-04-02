/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerVFSException;
import org.joing.pde.desktop.container.PDEDialog;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.misce.images.ImagesFactory;

/**
 * Extra functions used internally by PDE.
 * <p>
 * <b>NOTE: </b>If an application uses these functions, it will not be a portable 
 * (cross-desktops) Joing application.
 *
 * @author Francisco Morero Peyrona
 */
public class PDEUtilities
{
    /**
     * 
     * @param sFullPath
     * @return One of following:
     *         <ul>
     *         <li>An instance of <code>java.io.File</code> if file exists in
     *         local file system 
     *         <li> An instance of <code>org.joing.common.dto.vfs.FileDescriptor</code> 
     *              if file exists in remote file system (VFS)
     *         <li><code>null</code> if file does not exists neither in local or
     *             remote file systems.
     *         </ul>
     */
    public static Object getFile( String sFullPath )
    {
        Object oRet = null;
        
        // 1st check local FS
        java.io.File file = new java.io.File( sFullPath );
        
        if( file.exists() )
        {
            oRet = file;
        }
        else
        {   // Now check in VFS
            
            try
            {
                FileDescriptor fdRet = org.joing.jvmm.RuntimeFactory.getPlatform().getBridge().getFileBridge().getFile( sFullPath );
            }
            catch( JoingServerVFSException exc )
            {
                // Nothing to do: oRet is already null
            }
        }
        
        return oRet;
    }
    
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
     * @param invoker The class to be used as base to find the files. If null,
     *                <code>ImagesFactory</code> will be used (common images).
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one if teh requested was not found
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
        {
            url  = ImagesFactory.class.getResource( "no_image" );
            icon = new ImageIcon( url );
        }
        
        return icon;
    }
    
    /**
     * Return an icon (with specific dimension) which location is relative to 
     * passed class.
     * 
     * @param invoker The class to be used as base to find the files. If null,
     *                <code>ImagesFactory</code> will be used (common images).
     * @param sName   Name of file with its extension.
     * @return        The icon or an standard one if teh requested was not found
     */
    public static ImageIcon getIcon( Object invoker, String sName, int nWidth, int nHeight  )
    {
        ImageIcon icon = getIcon( invoker, sName  );
        
        if( icon.getIconWidth() != nWidth || icon.getIconHeight() != nHeight )
            icon.setImage( icon.getImage().getScaledInstance( nWidth, nHeight, Image.SCALE_SMOOTH ) );
        
        return icon;
    }
    
    public static ImageIcon getStandardIcon( ImagesFactory.Icon icon )
    {
        return getIcon( ImagesFactory.class, icon.getName() );
    }
    
    public static ImageIcon getStandardIcon( ImagesFactory.Icon icon, int nWidth, int nHeight )
    {
        return getIcon( ImagesFactory.class, icon.getName(), nWidth, nHeight );
    }
    
    public static void play( URL urlSound )
    {
        Applet.newAudioClip( urlSound ).play();
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
    
    public static boolean showBasicDialog( ImageIcon icon, String sTitle, JComponent content )
    {
        BasicDialog dialog = new BasicDialog( icon, sTitle, content );
        
        org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getActiveWorkArea().add( dialog );
        
        return dialog.bExitWithAccept;
    }
    
    
    /**
     * Shows an exception in a JDialog.
     * 
     * @param exc     Exception to be shown
     * @param bReport <code>true</code> when error should be reported to TelcoDomo
     */
    public void showException( Throwable exc, String sTitle )
    {
        exc.printStackTrace();   // TODO: quitarlo en la version final 
                                 //       y hacer una ventana para mostrar exc -->
        /*
        JShowException dialog = new JShowException( sTitle, exc );
                       dialog.setLocationRelativeTo( getDesktop() );
                       dialog.setVisible( true );*/
    }
    
    //------------------------------------------------------------------------//
    
    public static WorkArea findWorkAreaFor( DeskComponent comp )
    {
        List<WorkArea> lstWA = org.joing.jvmm.RuntimeFactory.getPlatform().getDesktopManager().getDesktop().getWorkAreas();
        
        for( WorkArea wa : lstWA )
        {
            Component[] ac = ((PDEWorkArea) wa).getComponents();
            
            for( int n = 0; n < ac.length; n++ )
            {
                if( ac[n] == comp )
                    return wa;
            }
        }
        
        return null;
    }
    
    //------------------------------------------------------------------------//
    // INNER CLASS: Basic Dialog
    //------------------------------------------------------------------------//
    private static final class BasicDialog extends PDEDialog
    {
        private boolean bExitWithAccept = false;
        
        private BasicDialog( ImageIcon icon, String sTitle, JComponent content )
        {
            super();
            
            JButton btnAccept = new JButton( "Accept" );
                    btnAccept.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            BasicDialog.this.bExitWithAccept = true;
                            BasicDialog.this.close();
                        }
                    } );
        
            JButton btnCancel = new JButton( "Cancel" );
                    btnCancel.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            BasicDialog.this.close();
                        }
                    } );
                    
            JPanel  pnlButtons = new JPanel( new FlowLayout( FlowLayout.TRAILING, 5, 0 ) );
                    pnlButtons.setBorder( new EmptyBorder( 0, 10, 10, 10 ) );
                    pnlButtons.add( btnAccept );
                    pnlButtons.add( btnCancel );
            JPanel  pnlContent = new JPanel( new BorderLayout() );
                    pnlContent.add( content   , BorderLayout.CENTER );
                    pnlContent.add( pnlButtons, BorderLayout.SOUTH  );
                    
            getContentPane().add( pnlContent, BorderLayout.CENTER );
            setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
            getRootPane().setDefaultButton( btnAccept );
            setTitle( (sTitle == null) ? "" : sTitle );
            
            if( icon != null )
                setFrameIcon( icon );
        }
    }
}