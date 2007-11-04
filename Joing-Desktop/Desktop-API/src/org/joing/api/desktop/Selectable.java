/*
 * Selectable.java
 *
 * Created on 10 de febrero de 2007, 19:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop;

/**
 * Defines a component as selectable: can be selected or unselected.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Selectable
{
    public boolean isSelected();
    public void setSelected( boolean b );
}