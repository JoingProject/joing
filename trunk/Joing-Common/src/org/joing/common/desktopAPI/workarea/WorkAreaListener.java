/*
 * WorkAreaListener.java
 * 
 * Created on 11-sep-2007, 1:05:09
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.workarea;

import org.joing.common.desktopAPI.DeskComponent;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface WorkAreaListener
{
    public void componentAdded( DeskComponent dc );
    public void componentRemoved( DeskComponent dc );
    public void componentSelected( DeskComponent dcOldSelected, DeskComponent dcNewSelected );
    
    /**
     * wpNew can be <code>null</code> if wallpaper was removed
     */
    public void wallpaperChanged( Wallpaper wpNew );
}