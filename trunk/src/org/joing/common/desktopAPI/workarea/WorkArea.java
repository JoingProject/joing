/*
 * WorkArea.java
 *
 * Created on 25 de abril de 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.workarea;

import java.awt.Component;
import java.util.List;

/**
 *
 * @author mario
 */
public interface WorkArea
{
    public String          getName();
    public void            setName( String sName );
    public Component       add( Component component );
    public void            remove( Component component );
    public List<Component> getOfType( Class clazz );
    public List<Component> getSelected( Class clazz, boolean bSelected );
    public void            setSelected( Class clazz, boolean bSelected );
    public void            close();
    
    public Wallpaper getWallpaper();
    public void      setWallpaper( Wallpaper wallpaper );
    
    public void addWorkAreaListener( WorkAreaListener wal );
    public void removeWorkAreaListener( WorkAreaListener wal );
}