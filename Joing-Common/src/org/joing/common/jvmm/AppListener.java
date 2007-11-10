/*
 * AppListener.java
 *
 * Created on Aug 5, 2007, 6:46:30 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface AppListener {

    public void applicationAdded(App app);

    public void applicationRemoved(App app);
}
