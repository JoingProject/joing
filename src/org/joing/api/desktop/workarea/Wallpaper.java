/*
 * Wallpaper.java
 *
 * Created on 20 de junio de 2007, 04:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea;

import java.io.FileDescriptor;
import java.net.URL;
import org.joing.api.desktop.enums.WallpaperMode;

/**
 *
 * @author Mario Serrano Leones
 */
public interface Wallpaper
{   
    public Object        getSource();
    public void          setSource(FileDescriptor fd);
    public void          setSource(URL source);
    public WallpaperMode getMode();
    public void          setMode(WallpaperMode mode);
}