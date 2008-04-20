/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 * Base class for: Canvas, Frames and Dialogs.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskPane extends DeskContainer
{
    void center();
    void setLocationRelativeTo( DeskComponent parent );
}
