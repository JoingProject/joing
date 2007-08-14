/*
 * DeskApplication.java
 *
 * Created on 25 de abril de 2007, 11:55 AM
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
public interface Application {
    
    
    public String getDisplayName();
    public String iconResource(IconType iconType);
    
    
}
