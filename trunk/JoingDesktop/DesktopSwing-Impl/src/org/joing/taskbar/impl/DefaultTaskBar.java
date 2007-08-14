/*
 * DefaultTaskBar.java
 *
 * Created on 17 de junio de 2007, 02:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.taskbar.impl;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.joing.desktop.enums.TaskBarOrientation;
import org.joing.taskbar.api.TaskBar;
import org.joing.taskbar.api.TaskBarPanel;

/**
 *
 * @author mario
 */
public class DefaultTaskBar extends JPanel implements TaskBar {
    
    private boolean expanded;
    
    private boolean autohide;
    
    private int size;
    
    private TaskBarOrientation orientation;
    
    /** Creates a new instance of DefaultTaskBar */
    public DefaultTaskBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        
        //Default values
        setOrientation(TaskBarOrientation.BOTTOM);
        setAutoHide(false);
        setExpanded(true);
        setHeightSize(24); //in pixels
        setOpaque(false);
    }   
    
    public void addPanel(TaskBarPanel panel) {
        if(panel instanceof DefaultTaskBarPanel){
            DefaultTaskBarPanel tbPanel = (DefaultTaskBarPanel) panel;                        
            add(tbPanel);
        }
    }
    
    public List<TaskBarPanel> getPanels() {
        return null;
    }
    
    public void setExpanded(boolean expanded) {
        this.expanded=expanded;
    }
    
    public boolean isExpanded() {
        return expanded;
    }
    
    public void setAutoHide(boolean autohide) {
        this.autohide=autohide;
    }
    
    public boolean isAutoHide() {
        return this.autohide;
    }
    
    public void setHeightSize(int size) {
        this.size = size;
        if(size>0){
            this.setSize(this.getWidth(),size);
            this.setPreferredSize(this.getSize());
        }
    }
    
    public int getHeightSize() {
        return size;
    }
    
    public void setOrientation(TaskBarOrientation orientation) {
        this.orientation = orientation;
    }
    
    public TaskBarOrientation getOrientation() {
        return this.orientation;
    }
    
}
