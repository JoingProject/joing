/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.awt.Color;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface ColorSchema
{
    Color getDeskLauncherBackgroundSelected();
    
    Color getDeskLauncherBackgroundUnSelected();
    
    Color getDeskLauncherTextBackground();

    Color getDeskLauncherTextForegroundSelected();

    Color getDeskLauncherTextForegroundUnSelected();    

    Color getDesktopBackground();

    Color getTaskBarBackground();

    Color getUserNameBackground();

    Color getUserNameForeground();
}