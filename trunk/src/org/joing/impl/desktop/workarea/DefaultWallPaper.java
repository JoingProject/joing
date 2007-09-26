/*
 * DefaultWallPaper.java
 * 
 * Created on 11-sep-2007, 17:59:41
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.impl.desktop.workarea;

import java.io.FileDescriptor;
import java.net.URL;
import org.joing.api.desktop.enums.WallpaperMode;
import org.joing.api.desktop.workarea.Wallpaper;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class DefaultWallPaper implements Wallpaper
{// TODO: hay que añadir un  (mirar PropertyChangeEvent)
    private FileDescriptor fdSource;
    private URL            urlSource;
    private WallpaperMode  mode;
    
    //------------------------------------------------------------------------//
    
    public DefaultWallPaper()
    {
        mode = WallpaperMode.CENTER;
    }
    
    //------------------------------------------------------------------------//
    
    public Object getSource()
    {
        return (fdSource == null) ? urlSource : fdSource;
    }
    
    public void setSource( FileDescriptor fd )
    {
        Object oOldValue = getSource();
        
        fdSource  = fd;
        urlSource = null;
        
        firePropertyChangedEvent( "source", oOldValue, fd );
    }
    
    public void setSource( URL url )
    {
        Object oOldValue = getSource();
        
        fdSource  = null;
        urlSource = url;
        
        firePropertyChangedEvent( "source", oOldValue, url );
    }
    
    public WallpaperMode getMode()
    {
        return mode;
    }
    
    public void setMode( WallpaperMode mode )
    {
        Object oOldValue = getMode();
        
        this.mode = mode;
        
        firePropertyChangedEvent( "mode", oOldValue, mode );
    }
    
    //------------------------------------------------------------------------//
    
    protected void firePropertyChangedEvent( String sPropertyName, Object oOldValue, Object oNewValue )
    {
        // TODO: hacerlo
    }
}