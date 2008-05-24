/*
 * DeskCanvas.java
 *
 * Created on 4 de octubre de 2007, 0:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

import org.joing.common.desktopAPI.DeskComponent;
import org.joing.common.desktopAPI.DeskContainer;

/**
 * Base interface for Desktop Containers.
 * 
 * @author Francisco Morero Peyrona
 */
public interface DeskCanvas extends DeskContainer
{
    void center();
    void setLocationRelativeTo( DeskComponent parent );
}