/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.common.desktopAPI;

/**
 * Indicates that the object has operations to do before being closed.
 * <p>
 * Components that implement this interface will be notified when the desktop is 
 * going to be closed. Having in this way an opportunity to perform the 
 * house-keeping.
 * 
 * @author Francisco Morero Peyrona
 */
public interface Closeable
{    
    /** 
     * Informs to the object that it has to close.
     * <p>
     * Gives an opportunity to the object to make the house-keeping (auto-clean).
     * <p>
     * Containers traverse recursively all its components invoking close().
     */ 
    void close();
}