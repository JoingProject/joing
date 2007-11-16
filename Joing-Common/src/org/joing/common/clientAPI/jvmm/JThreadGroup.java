/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.jvmm;

import java.io.OutputStream;

/**
 *
 * @author antoniovl
 */
public abstract class JThreadGroup extends ThreadGroup {

    protected Thread disposer;

    /**
     * Any override of this method should call super("..."),
     */
    public JThreadGroup() {
        super("JoingThreadGroup:" + System.nanoTime());
    }

    public void setDisposer(Thread disposer) {
        this.disposer = disposer;
    }

    public abstract OutputStream getOut();

    public abstract OutputStream getErr();

    /**
     * Closes this ThreadGroup calling the specific disposer thread.
     */
    public abstract void close();
    
}
