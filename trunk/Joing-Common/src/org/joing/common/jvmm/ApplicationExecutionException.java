/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.jvmm;

/**
 *
 * @author antoniovl
 */
public class ApplicationExecutionException extends Exception {

    /**
     * Creates a new instance of <code>ApplicationExecutionException</code> without detail message.
     */
    public ApplicationExecutionException() {
    }


    /**
     * Constructs an instance of <code>ApplicationExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ApplicationExecutionException(String msg) {
        super(msg);
    }
}
