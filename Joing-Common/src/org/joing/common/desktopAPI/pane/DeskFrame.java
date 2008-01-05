/*
 * DeskFrame.java
 *
 * Created on 4 de octubre de 2007, 0:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DeskFrame extends DeskWindow
{
    public enum Status { MINIMIZED, MAXIMIZED, RESTORED }
    
    void setStatus( Status status );
    
    Status getStatus();
    
    boolean isAlwaysOnTop();
    
    void setAlwaysOnTop( boolean b );
}