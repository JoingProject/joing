/*
 * WorkArea.java
 *
 * Created on 25 de abril de 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api;

import java.net.URL;
import java.util.List;

/**
 *
 * @author mario
 */
public interface WorkArea {
    
    public static final int WALLPAPER_LAYER     = -10;    
    public static final int LAUNCHER_LAYER      = 10;
    public static final int DESKLET_LAYER       = 20;
    public static final int APPLICATION_LAYER   = 30;
    public static final int DIALOG_LAYER        = 40;
    public static final int GLASS_LAYER         = 50;
    
    
    
    public void addApplication(Application application);
    public List<Application> getApplications();
    
    public void setSelectedLauncher(Launcher launcher);
    public Launcher getSelectedLauncher();
    public void addLauncher(Launcher launcher);
    public List<Launcher> getLaunchers();
    
    public void setWallpaper(Wallpaper wallpaper);
}
