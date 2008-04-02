/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 * Common interface for all objects that appear in: WorkAreas and TaskBars.
 * <p>
 * DeskComponents are added/removed into/from DeskContainers.
 * <p>
 * Desktop elements either will detach themselves from their container 
 * (Desktop) or the Desktop has to know when the element is about to be
 * closed to detach them. In any case, it is not programmer resposability to
 * manually detach elements from Desktop.
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