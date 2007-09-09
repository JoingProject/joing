/*
 * DefualtLauncher.java
 *
 * Created on 17 de junio de 2007, 03:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.joing.desktop.api.Launcher;
import org.joing.desktop.impl.util.ImageContrastFilter;

/**
 *
 * @author Mario Serrano Leones
 */
public class DefaultLauncher extends JLabel implements Launcher{
    
    private String command;
    
    private String arguments;
    
    private String icon16;
    
    private String icon24;
    
    private String icon32;
    
    private Icon normalIcon,overIcon;
    
    private boolean selected;
    
    private ImageFilter filter;
    
    /** Creates a new instance of DefualtLauncher */
    public DefaultLauncher(String label) {
        setOpaque(false);
        setHorizontalTextPosition(JLabel.CENTER);
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setVerticalAlignment(JLabel.CENTER);
        setForeground(Color.white);
        setText(label);
        selected=false;
        
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public void setArguments(String args) {
        this.arguments = args;
    }
    
    public void setIconResource(String resource, IconType type) {
        switch(type){
            case ICON16: this.icon16 = resource; break;
            case ICON24: this.icon24 = resource; break;
            case ICON32: this.icon32 = resource; break;
        }
        initIconLauncher();
    }
    
    public String getIconResource(IconType type) {
        String res=null;
        switch(type){
            case ICON16: res =  this.icon16; break;
            case ICON24: res =  this.icon24; break;
            case ICON32: res =  this.icon32; break;
        }
        return res;
    }
    
    //Aqui se llama a el AppLauncher
    public boolean execute() {
        //TODO: Implementar
        System.out.println("RUNNING LAUNCHER");
        return true;
    }
    
    private void initIconLauncher() {
        String res = this.getIconResource(IconType.ICON32);
        Image img = getToolkit().createImage(getClass().getResource(res));
        normalIcon = new ImageIcon(img);
        this.setIcon(normalIcon);
        //Crear icono hightligthe
        if(filter==null)
            filter = new ImageContrastFilter(1.5);
        overIcon = new ImageIcon(this.createImage(new FilteredImageSource(img.getSource(),filter)));
    }
    
    public void swapIcons(){
        if(getIcon()==normalIcon)
            setIcon(overIcon);
        else
            setIcon(normalIcon);
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
    
    protected void paintComponent(Graphics g) {        
        if(this.isSelected()){
            g.setColor(new Color(60,60,60,120));
            g.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
        }
        super.paintComponent(g);
    }    
}