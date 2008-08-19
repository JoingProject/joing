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
     * Locks desktop.
     * <p>
     * Note: For security reasons, unlock does not exists because there should 
     * not be an API (a programatically way) to unlock the screen.<br>
     * Therefore, the lock module has to provide the mechanisim to unlock the 
     * desktop.
     */
    void lock();
}