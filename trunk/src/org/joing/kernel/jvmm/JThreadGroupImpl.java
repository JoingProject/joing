/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.joing.kernel.jvmm;

import java.io.OutputStream;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.joing.kernel.api.kernel.jvmm.JThreadGroup;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

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
