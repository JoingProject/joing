/*
 * Selectable.java
 *
 * Created on 10 de febrero de 2007, 19:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.pde.desktop;

/**
 * Used by <code>WDesktopPane</code>
 * 
 * @author Francisco Morero Peyrona
 */
public interface Selectable
{
    public boolean isSelected();
    
    public void setSelected( boolean b );
}