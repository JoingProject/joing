/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DeskContainer extends DeskComponent, Closeable
{
    /**
     * Add a component to this container.<br>
     * Depending on the layout (any or null) that this container has,
     * Deskcopmponent:getLocation() property of passed components will be 
     * used or ignored to position passed component inside the container.
     * 
     * @param dc Component to add to this container.
     */
    void add( DeskComponent dc );
    void remove( DeskComponent dc );
}