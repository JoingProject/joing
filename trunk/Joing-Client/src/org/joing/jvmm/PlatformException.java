/*
 * PlatformException.java
 * 
 * Created on Aug 5, 2007, 5:44:13 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class PlatformException extends Exception {

    /**
     * Creates a new instance of <code>PlatformException</code> without detail message.
     */
    public PlatformException() {
    }


    /**
     * Constructs an instance of <code>PlatformException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PlatformException(String msg) {
        super(msg);
    }
}
