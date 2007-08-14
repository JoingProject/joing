/*
 * LauncherMouseListener.java
 *
 * Created on 20 de junio de 2007, 05:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;
import org.joing.desktop.api.DesktopManager;
import org.joing.desktop.impl.DefaultLauncher;
import org.joing.desktop.impl.DefaultWorkArea;

/**
 *
 * @author Mario Serrano Leones
 */
public class LauncherMouseListener extends MouseAdapter{
    
    private boolean isPressed;
    
    private int lastX;
    private int lastY;
    
    /** Creates a new instance of LauncherMouseListener */
    public LauncherMouseListener() {
        isPressed=false;
    }
    
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() instanceof DefaultLauncher){
            DefaultLauncher launcher = (DefaultLauncher) e.getSource();
            launcher.swapIcons();
        }
    }
    
    public void mouseExited(MouseEvent e) {
        if(e.getSource() instanceof DefaultLauncher){
            DefaultLauncher launcher = (DefaultLauncher) e.getSource();
            launcher.swapIcons();
        }
        
    }
    
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if(src instanceof DefaultLauncher){
            DefaultLauncher launcher = (DefaultLauncher) e.getSource();
            if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2){
                launcher.execute();
            }
        }
    }
    
    
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        
       /* if(e.getSource() instanceof DefaultLauncher && isPressed){
        
            DefaultLauncher launcher = (DefaultLauncher) e.getSource();
            if(launcher.contains(e.getPoint())){
                launcher.setLocation(e.getPoint());
            }
        }*/
    }
    
    public void mouseDragged(MouseEvent e) {
        if(e.getSource() instanceof DefaultLauncher){
            DefaultLauncher launcher = (DefaultLauncher) e.getSource();
            int x = e.getX() - lastX;
            int y = e.getY() - lastY;
            launcher.setLocation(x,y);
            System.out.println("Dragging "+e.getPoint());
            
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        
    }
    
    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        if(SwingUtilities.isLeftMouseButton(e)){
            if(src instanceof DefaultLauncher){
                DefaultLauncher launcher = (DefaultLauncher) src;
                DesktopManager.getDesktop().getCurrentWorkArea().setSelectedLauncher(launcher);
            }else if(src instanceof DefaultWorkArea){
                DefaultWorkArea dwa = (DefaultWorkArea) src;
                dwa.setSelectedLauncher(null);
            }
        }
    }
    
    
    
    
}
