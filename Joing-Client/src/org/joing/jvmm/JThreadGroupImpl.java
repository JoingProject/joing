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
import org.joing.common.jvmm.JThreadGroup;

/**
 * Joing's own ThreadGroup implementation. Based on Chris Oliver's example.
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JThreadGroupImpl extends JThreadGroup {

    OutputStream out;
    OutputStream err;

    public JThreadGroupImpl(OutputStream out, OutputStream err) {
        super();
        this.out = out;
        this.err = err;
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
            System.out.println("Launching Disposer Thread...");
            t.start();
        }
    }
}
