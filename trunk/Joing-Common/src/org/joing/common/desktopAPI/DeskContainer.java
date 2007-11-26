/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 *
 * @author fmorero
 */
public interface DeskContainer extends DeskComponent, Closeable
{
    void setContent( DeskComponent dc );
    void removeContent();
}