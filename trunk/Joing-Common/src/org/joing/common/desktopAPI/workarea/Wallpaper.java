/*
 * Wallpaper.java
 *
 * Created on 20 de junio de 2007, 04:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.workarea;

import java.awt.Image;
import java.io.FileDescriptor;
import java.net.URL;

/**
 *
 * @author Mario Serrano Leones
 */
public interface Wallpaper
{
    public enum Mode { EXPANDED, CENTER, TILES; }
    
    Image getSource();
    void  setSource( Image image );
    Mode  getMode();
    void  setMode( Mode mode );
}