/*
 * Wallpaper.java
 *
 * Created on 20 de junio de 2007, 04:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api;

import java.net.URL;
import org.joing.desktop.enums.WallpaperMode;

/**
 *
 * @author Mario Serrano Leones
 */
public interface Wallpaper {
    
    public void setSource(URL source);
    public void setMode(WallpaperMode mode);
    
    
}
