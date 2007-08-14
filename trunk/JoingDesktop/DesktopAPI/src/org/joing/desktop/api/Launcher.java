/*
 * Launcher.java
 *
 * Created on 17 de junio de 2007, 03:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api;

import org.joing.desktop.enums.IconType;
 

/**
 *
 * @author mario
 */
public interface Launcher {    
    
    public void setCommand(String command);
    public void setArgumments(String args);
    public void setIconResource(String resource, IconType type);
    public String getIconResource(IconType type);    
    public boolean execute();
}
