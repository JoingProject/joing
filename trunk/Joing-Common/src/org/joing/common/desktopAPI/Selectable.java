/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 * Indicates that the object can change its selection status.
 * <p>
 * Components that implement this interface will be notified when the desktop
 * gets the focus: all selected components become unselected.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Selectable
{
    boolean isSelected();
    void    setSelected( boolean bStatus );
}
