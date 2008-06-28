/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.jvmm;

import java.io.OutputStream;
import java.util.UUID;

/**
 * Thread Group Abstract Class.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public abstract class JThreadGroup extends ThreadGroup {

    protected Thread disposer;

    public JThreadGroup() {
        super("JoingThreadGroup_" + UUID.randomUUID().toString());
    }
    
    public JThreadGroup(String name) {
        super(name);
    }
    
    public JThreadGroup(String name, Thread disposer, ThreadGroup parent) {
        super(parent, name);
        this.disposer = disposer;
    }
    
    public JThreadGroup(String name, ThreadGroup parent) {
        this(name, null, parent);
    }
    
    public JThreadGroup(Thread disposer) {
        this();
        this.disposer = disposer;
    }
    
    public JThreadGroup(Thread disposer, ThreadGroup parent) {
        super(parent, "JoingThreadGroup " + UUID.randomUUID().toString());
        this.disposer = disposer;
    }

    public void setDisposer(Thread disposer) {
        this.disposer = disposer;
    }

    public abstract OutputStream getOut();

    public abstract OutputStream getErr();

    /**
     * Closes this ThreadGroup calling the disposer thread.
     */
    public abstract void close();
    
}
