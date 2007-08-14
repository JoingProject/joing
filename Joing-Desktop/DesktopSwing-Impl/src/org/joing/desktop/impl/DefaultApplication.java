/*
 * DefaultApplication.java
 *
 * Created on 28 de abril de 2007, 10:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.impl;

import javax.swing.JInternalFrame;
import org.joing.desktop.api.Application;
import org.joing.desktop.enums.IconType;

/**
 *
 * @author mario
 */
public abstract class DefaultApplication extends JInternalFrame implements Application{
    public String getDisplayName() {
        return "";
    }

    public String iconResource(IconType iconType) {
        return "";
    }
    

    
}
