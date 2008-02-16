/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ExecutionTaskException extends RuntimeException {

    /**
     * Creates a new instance of <code>ExecutionTaskException</code> without detail message.
     */
    public ExecutionTaskException() {
    }


    /**
     * Constructs an instance of <code>ExecutionTaskException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ExecutionTaskException(String msg) {
        super(msg);
    }

    public ExecutionTaskException(Throwable cause) {
        super(cause);
    }

    public ExecutionTaskException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
