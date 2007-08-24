/*
 * JThreadGroup.java
 *
 * Created on Aug 5, 2007, 2:20:19 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Joing's own ThreadGroup implementation. Based on Chris Oliver's example.
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JThreadGroup extends ThreadGroup {

    OutputStream out;
    OutputStream err;
    Thread disposer;

    public JThreadGroup(OutputStream out, OutputStream err) {
        super("JoingThreadGroup:" + System.nanoTime());
        this.out = out;
        this.err = err;
    }

    public void setDisposer(Thread disposer) {
        this.disposer = disposer;
    }

    public OutputStream getOut() {
        return this.out;
    }

    public OutputStream getErr() {
        return this.err;
    }

    public void close() {

        try {
            out.close();
        } catch (IOException e) {
            // No fue posible cerrar el output Stream, sin embargo
            // podemos continuar.
        }

        if (err != out) {
            try {
                err.close();
            } catch (IOException e) {
                // similar al anterior
            }
        }

        err = null;
        // Esta transformación es amigable al Garbage Collector
        Thread t = disposer;
        disposer = null;

        if (t != null) {
            System.out.println("disposing...");
            t.start();
        }
    }
}
