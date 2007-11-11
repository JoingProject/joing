/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.api;

import org.joing.common.jvmm.Platform;

/**
 *
 * @author antoniovl
 */
public interface DesktopManager<D> {

    // Showing in different ways
    void showInFrame();
    void showInFullScreen();
    
    // Others
    void lock();
    void close();
    
    // original method was "Desktop getDesktop()"
    public D getDesktop();
    
    void setPlatform(Platform platform);
    
}
