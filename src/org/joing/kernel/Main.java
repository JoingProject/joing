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

package org.joing.kernel;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.joing.common.dto.app.Application;
import org.joing.kernel.applauncher.Bootstrap;
import org.joing.kernel.api.kernel.jvmm.ApplicationExecutionException;
import org.joing.kernel.api.kernel.jvmm.Platform;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;
import org.joing.kernel.jvmm.RuntimeFactory;
import org.joing.kernel.jvmm.net.BridgeURLConnection;

/**
 *
 * @author Francisco Morero Peyrona
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Main {

    public Main() {
    }

    public static void main(final String[] args) {
        
        // Ejecutamos Bootstrap.init() en su propio Thread.
//        Thread initWorker = new Thread(new Runnable() {
//            public void run() {
//                Bootstrap.init();
//            }
//        }, "Bootstrap-Thread");
//        initWorker.start();
        Bootstrap.init();

        boolean useGUI = true;
        
        final List<URL> urls = new ArrayList<URL>();
        final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
        final Platform platform = RuntimeFactory.getPlatform();
      
        // Analizamos los argumentos
        for (String arg : args) {
            if ("-nogui".equals(arg)) {
                // No presentaremos el dialogo Login
                useGUI = false;
            } else {
                // URLs que ejecutaremos en modo standalone
                try {
                    URL u = new URL(arg);
                    urls.add(u);
                } catch (MalformedURLException mue) {
                    logger.critical("Unable to parse URL: {0}", arg);
                }
            }
        }
        
        if (useGUI) {
 
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Bootstrap.loginDialog();
                }
            });
        }
        
        if ((urls.size() == 0) && (!useGUI)) {
            logger.info("No standalone jars launched, nothing to do.");
        } else {
            Thread auxWorker = new Thread(new Runnable() {

                public void run() {
                    for (URL url : urls) {
                        try {
                            Application a = applicationFromURL(url);
                            URL[] classPath = new URL[]{url};
                            String[] localArgs = new String[]{};
                            platform.start(classPath, a, localArgs,
                                    System.out, System.err);

                        } catch (ApplicationExecutionException aee) {
                            logger.critical("Unable to launch app: {0}", aee.getMessage());
                        } catch (MalformedURLException mue) {
                            logger.critical("Unable to parse URL: {0}", mue.getMessage());
                        } catch (IOException ioe) {
                            logger.critical("IOException: {0}", ioe.getMessage());
                        }
                    }
                }
            }, "Joing-StandaloneLauncher-Thread");
            auxWorker.start();
            
            Bootstrap.mainLoop();
        }
        
        
    }
    
    private static Application applicationFromURL(URL url) 
            throws IOException, MalformedURLException {

        Application app = null;
        
        // Construimos una URL
        URLConnection c = url.openConnection();

        if (c instanceof BridgeURLConnection) {
            BridgeURLConnection buc = (BridgeURLConnection) c;
            return buc.getApplication();
        }

        DataInputStream dis = new DataInputStream(c.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        boolean done = false;
        while (!done) {
            byte[] b = new byte[8192]; // 8k blocks
            int r = dis.read(b);
            if (r == -1) {
                done = true;
            } else {
                dos.write(b);
            }
        }

        app = new Application();
        app.setContents(baos.toByteArray());

        dos.close();
        dis.close();

        return app;
    }
}
