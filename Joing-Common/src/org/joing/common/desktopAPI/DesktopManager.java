/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import org.joing.common.desktopAPI.desktop.Desktop;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DesktopManager
{
    /**
     * Desktop runs inside a Frame (either Frame or JFrame)
     */
    void showInFrame();
    /**
     * Desktop runs in full-screen modes (when available).
     */
    void showInFullScreen();
    
    // Called from Platform
    /**
     * This method exists to be called from Platform and should <u>never</u> be 
     * called from the Desktop.
     */
    void shutdown();
    /**
     * This method exists to be called from Platform and should <u>never</u> be 
     * called from the Desktop.
     */
    void halt();
    
    /**
     * Returns a reference to the Desktop
     * @return A reference to the Desktop
     */
    Desktop getDesktop();
    /**
     * Returns a reference to the Runtime
     * @return A reference to the Runtime
     */
    Runtime getRuntime();
    
    /**
     * Locks desktop
     */
    void lock();
    /**
     * Unlocks dekstop
     */
    void unlock();
    
    /**
     * Desktop comercial name.
     * @return Desktop comercial name.
     */
    String getName();
    /**
     * Desktop version.
     * @return Desktop version.
     */
    String getVersion();
    /**
     * Return comercial information (one or more lines).
     * @return Comercial information
     */
    String getComercialInfo();
}