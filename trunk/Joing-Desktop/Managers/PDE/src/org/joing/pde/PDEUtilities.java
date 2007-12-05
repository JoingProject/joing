/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.pde;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import org.joing.common.desktopAPI.DesktopManagerFactory;
import org.joing.common.desktopAPI.workarea.WorkArea;
import org.joing.pde.desktop.workarea.PDEWorkArea;
import org.joing.pde.misce.images.ImagesFactory;

/**
 * Extra functions.
 * They are only used internally by PDE but can't be used by portable
 * (cross-desktops) Joing applications.
 *
 * @author Francisco Morero Peyrona
 */
public class PDEUtilities
{
    public static byte[] image2ByteArray( Image image )
    {        
        return null;
    }
    
    public static byte[] icon2ByteArray( ImageIcon icon )
    {
        return image2ByteArray( icon.getImage() );
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
            if( invoker == null )
            {
                url = ImagesFactory.class.getResource( sName );
            }
            else
            {
                if( invoker instanceof Class )
                    url = ((Class) invoker).getResource( sName );
                else
                    url = invoker.getClass().getResource( sName );
            }
            
            if( url != null )
                icon = new ImageIcon( url );
        }
        
        if( icon == null )
        {
            url  = ImagesFactory.class.getResource( "no_image.png" );
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
    public static ImageIcon getIcon( Object o, String s, int nWidth, int nHeight )
    {
        ImageIcon icon = getIcon( o, s );
        
        if( icon.getIconWidth() != nWidth || icon.getIconHeight() != nHeight )
            icon.setImage( icon.getImage().getScaledInstance( nWidth, nHeight, Image.SCALE_SMOOTH ) );
        
        return icon;
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
    
    
    /**
     * Get all objects in dekstop that match passed class,
     * or an empty <code>Vector</code> one if there is no one selected.
     * <p>
     * To select objects of all classes, simply pass <code>Component</code>
     */
    public static <T> List<T> getOfType( Component container, Class<T> clazz )
    {
        /* TODO: hcaerlo y que sea recursivo
        ArrayList<Object> list  = new ArrayList<Object>();                
        Component[]       aComp = getComponents();
        
        for( int n = 0; n < aComp.length; n++ )
        {
            if( clazz.isInstance( aComp[n] ) )
                list.add( aComp[n] );
        }
        
        return list;
        */
        return null;
    }
    
    //------------------------------------------------------------------------//
    
    public static WorkArea findWorkAreaFor( Component comp )
    {
        List<WorkArea> lstWA = DesktopManagerFactory.getDM().getDesktop().getWorkAreas();
        
        for( WorkArea wa : lstWA )
        {
            Component[] ac  = ((PDEWorkArea) wa).getComponents();
            
            for( int n = 0; n < ac.length; n++ )
            {
                if( ac[n] == comp )
                    return wa;
            }
        }
        
        return null;
    }
}
