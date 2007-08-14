/*
 * DefaultWallpaper.java
 *
 * Created on 20 de junio de 2007, 04:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joing.desktop.api.Wallpaper;
import org.joing.desktop.enums.WallpaperMode;

/**
 *
 * @author Mario Serrano Leones
 */
public class DefaultWallpaper implements Wallpaper {
    
    private URL source;
    private WallpaperMode mode;
    private Image img;
    
    private int width;
    
    private int height;
    
    private Container parent;
    
    /** Creates a new instance of DefaultWallpaper */
    public DefaultWallpaper() {
        mode = WallpaperMode.EXPANDED;
    }
    
    public void setSource(URL source) {
        this.source=source;
        img=null;
    }
    
    public void setMode(WallpaperMode mode) {
        this.mode=mode;
    }
    
    void draw(Graphics g,Container parent) {
        this.parent=parent;
        if(source!=null){
            if(img==null)
                this.img = Toolkit.getDefaultToolkit().createImage(source);
            
            switch (mode){
                case CENTER: drawCenter(g); break;
                case TILES: drawTiles(g); break;
                case EXPANDED: drawExpanded(g); break;
            }
            
        }
    }
    
    private void drawCenter(Graphics g) {
        
    }
    
    private void drawTiles(Graphics g) {
        
    }
    
    private void drawExpanded(Graphics g) {
        if(img!=null){
            int w = parent.getWidth();
            int h = parent.getHeight();
            g.drawImage(img,0,0,w,h,parent);
        }
    }
    
    
    
}
