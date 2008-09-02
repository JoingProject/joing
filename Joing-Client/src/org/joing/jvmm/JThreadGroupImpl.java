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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.joing.common.clientAPI.jvmm.JThreadGroup;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 * Joing's own ThreadGroup implementation. Based on Chris Oliver's example.
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JThreadGroupImpl extends JThreadGroup {

    OutputStream out;
    OutputStream err;
    Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public JThreadGroupImpl(OutputStream out, OutputStream err) {
        super();
        this.out = out;
        this.err = err;
    }

    public JThreadGroupImpl(String name, OutputStream out, OutputStream err) {
        super(name);
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

        //try {
            // esto me trae problemas con el debugger
            //out.close();
        //} catch (IOException e) {
            // No fue posible cerrar el output Stream, sin embargo
            // podemos continuar.
        //}

//        if (err != out) {
//            try {
//                err.close();
//            } catch (IOException e) {
//                // similar al anterior
//            }
//        }

        err = null;
        // Esta transformación es amigable al Garbage Collector
        final Thread t = disposer;
        disposer = null;

        if (t != null) {
            logger.debugJVMM("Launching disposer Thread...");
            if (SwingUtilities.isEventDispatchThread()) {
                SwingWorker worker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        t.start();
                        return null;
                    }
                    
                };
                worker.execute();
            } else {
                t.start();
            }
        }
    }
}
