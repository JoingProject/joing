/*
 * DesktopManager.java
 *
 * Created on 17 de junio de 2007, 03:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop;

/**
 *
 * @author Mario Serrano Leones
 */
public class DesktopManager {
    
    private static Desktop desktop;
    
    /** Creates a new instance of DesktopManager */
    private DesktopManager() {
    }
    
    public static void setDesktop(Desktop desk){
        desktop = desk;
    }
    
    
    public static Desktop getDesktop(){
        return desktop;
    }
    
   
    
}
