/*
 * WorkArea.java
 *
 * Created on 25 de abril de 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.workarea;

import org.joing.common.desktopAPI.Closeable;
import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.pane.DeskWindow;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface WorkArea extends Closeable
{
    String getName();
    void   setName( String name );
    
    void add( DeskComponent dc );    
    void remove( DeskComponent dc );
    
    void add( DeskWindow dc, boolean bAutoArrange );    
    
    void moveToFront( DeskComponent dc );
    void moveToBack( DeskComponent dc );
    
    int  getWidth();
    int  getHeight();
    
    Wallpaper getWallpaper();
    void      setWallpaper( Wallpaper wallpaper );
    
    void addWorkAreaListener( WorkAreaListener wal );
    void removeWorkAreaListener( WorkAreaListener wal );
}