/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 * Common interface for all objects that appear in: WorkAreas and TaskBars.
 * <p>
 * DeskComponents are added/removed into/from DeskContainers.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskComponent
{
    int  getX();
    int  getY();
    void setLocation( int x, int y );
    
    int  getWidth();
    int  getHeight();
    void setSize( int width, int height );
}