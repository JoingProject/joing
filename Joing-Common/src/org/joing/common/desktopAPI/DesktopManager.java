/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import org.joing.common.desktopAPI.desktop.Desktop;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.common.clientAPI.runtime.Bridge2Server;

/**
 *
 * @author antoniovl
 */
public interface DesktopManager
{
    // Showing in different ways
    void showInFrame();
    void showInFullScreen();
    
    // Exiting
    void lock();
    void close();
    
    // References
    Desktop       getDesktop();
    Runtime       getRuntime();
    Bridge2Server getBridge();
    
    // Called from Client
    void setPlatform(Platform platform);
}