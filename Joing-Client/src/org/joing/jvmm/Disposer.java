/*
 * Disposer.java
 * 
 * Created on Aug 5, 2007, 6:33:44 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 * This is an example Disposer Thread. Invoked from
 * JThreadGroup.close(), should clean up threading infrastructure.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Disposer implements Runnable {

    public Disposer() {
    }

    public void run() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
