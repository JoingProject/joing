/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.awt.Image;
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
    /**
     * This method is normally invoked from Platform to inform that a long task
     * is in progress against Join'g Server.
     * Every Joing client can implement this information as it would like: using
     * a dialog, a mesagge in the status bar, etc.
     * 
     * @param sMesaage Message to be shown: if null an standard one will be used.
     * @param icon     Icon to be shown: if null an standard one will be used.
     * @return A handle to the message GUI to be closed when the trade is over.
     */
    int  startServerTradeInfo( String sMesaage, Image icon );
    /**
     * Hides an information GUI previously opended via startServerTradeInfo(...)
     * 
     * @param nHandle The one returned by startServerTradeInfo(...)
     */
    void stopServerTradeInfo( int nHandle );
    
    // References
    Desktop getDesktop();
    Runtime getRuntime();
}