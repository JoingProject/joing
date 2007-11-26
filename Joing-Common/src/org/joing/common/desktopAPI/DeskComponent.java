/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Common interface for all objects that appear in: WorkAreas and TaskBars.
 * <p>
 * DeskComponents are added/removed into/from DeskContainers.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskComponent
{
    Point getLocation();
    void  setLocation( Point pt );
    
    Dimension getSize();
    void      setSize( Dimension d );
}