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
    // Showing in different ways
    void showInFrame();
    void showInFullScreen();
    
    // Called from Platform
    void shutdown();
    void halt();
    
    // References
    Desktop getDesktop();
    Runtime getRuntime();
    
    // Locking & unlocking
    void lock();
    void unlock();
}