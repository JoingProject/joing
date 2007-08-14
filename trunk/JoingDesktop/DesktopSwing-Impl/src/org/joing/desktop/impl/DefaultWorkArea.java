/*
 * DefaultWorkArea.java
 *
 * Created on 28 de abril de 2007, 10:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import org.joing.desktop.api.Application;
import org.joing.desktop.api.Launcher;
import org.joing.desktop.api.Wallpaper;
import org.joing.desktop.api.WorkArea;
import org.joing.desktop.enums.IconType;
import org.joing.desktop.impl.util.LauncherMouseListener;

/**
 *
 * @author Mario Serrano Leones
 */
public class DefaultWorkArea extends JLayeredPane implements WorkArea {
    
    private List<Launcher> launchers;
    private DefaultWallpaper wallpaper;
    private LauncherMouseListener launcherMouseListener;
    
    private Launcher selectedLauncher;
    
    /** Creates a new instance of DefaultWorkArea */
    public DefaultWorkArea() {
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
        launchers = new LinkedList<Launcher>();
        launcherMouseListener = new LauncherMouseListener();
        addMouseMotionListener(launcherMouseListener);
        addMouseListener(launcherMouseListener);
        
    }
    
    public void addApplication(Application application) {
        if(application instanceof DefaultApplication){
            DefaultApplication defaultApp = (DefaultApplication) application;
            add(defaultApp,APPLICATION_LAYER);
            defaultApp.setVisible(true);
        }
    }
    
    public List<Application> getApplications() {
        return null;
    }
    
    public void setWallpaper(Wallpaper wallpaper) {
        if(wallpaper!=null && wallpaper instanceof DefaultWallpaper){
            this.wallpaper = (DefaultWallpaper) wallpaper;
        }
    }
    
    public void addLauncher(Launcher launcher) {
        if(launcher!=null && launcher instanceof DefaultLauncher){
            launchers.add(launcher);
            DefaultLauncher defaultLauncher = (DefaultLauncher) launcher;            
            defaultLauncher.addMouseListener(launcherMouseListener);
            defaultLauncher.addMouseMotionListener(launcherMouseListener);
            add(defaultLauncher,LAUNCHER_LAYER);
        }
    }
    
    public List<Launcher> getLaunchers() {
        return launchers;
    }
    
    public void setSelectedLauncher(Launcher launcher) {
        this.selectedLauncher=launcher;
        if(launcher instanceof DefaultLauncher){
            DefaultLauncher defLauncher = (DefaultLauncher) launcher;
            defLauncher.setSelected(true);
        }
        unselectOtherLaunchers();
    }
    
    public Launcher getSelectedLauncher() {
        return this.selectedLauncher;
    }
    
    protected void paintComponent(Graphics g) {
        if(wallpaper!=null){
            wallpaper.draw(g,this);
        }
    }
    
    private void unselectOtherLaunchers() {
        for(Launcher launcher : getLaunchers()){            
            if(launcher!=selectedLauncher){
                DefaultLauncher defLauncher = (DefaultLauncher) launcher;
                defLauncher.setSelected(false);
            }
        }
    }
    
    
    
    
    
}
