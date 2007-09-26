/*
 * WorkAreaListener.java
 * 
 * Created on 11-sep-2007, 1:05:09
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea;

import java.awt.Component;
import java.util.EventListener;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface WorkAreaListener extends EventListener
{
    public void componentAdded( Component comp );
    public void componentRemoved( Component comp );
    
    /**
     * wpNew can be <code>null</code> if wallpaper was removed
     */
    public void wallpaperChanged( Wallpaper wpNew );
}