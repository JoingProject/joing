/*
 * Main.java
 *
 * Created on 10-jul-2007, 12:28:15
 *
 * Author: Francisco Morero Peyrona.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing;

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
import org.joing.applauncher.Bootstrap;
import org.joing.common.clientAPI.jvmm.ApplicationExecutionException;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.common.dto.app.Application;
import org.joing.jvmm.RuntimeFactory;
import org.joing.jvmm.net.BridgeURLConnection;

/**
 *
 * @author fmorero
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Main {

    public Main() {
    }

    public static void main(final String[] args) {
        
        Thread initWorker = new Thread(new Runnable() {
            public void run() {
                Bootstrap.init();
            }
        });
        initWorker.start();

        boolean noGUI = false;    
        
        final List<URL> urls = new ArrayList<URL>();
        final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
        final Platform platform = RuntimeFactory.getPlatform();
        
        for (String arg : args) {
            if ("-nogui".equals(arg)) {
                noGUI = true;
            } else {
                try {
                    URL u = new URL(arg);
                    urls.add(u);
                } catch (MalformedURLException mue) {
                    logger.critical("Unable to parse URL: {0}", arg);
                }
            }
        }
        
        if (noGUI == false) {
            // LoginDialog blocks the current thread
            Thread loginWorker = new Thread(new Runnable() {
                public void run() {
                    Bootstrap.loginDialog();
                }
            });
            loginWorker.start();
        }
        
        if ((urls.size() == 0) && (noGUI)) {
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
            });
            auxWorker.start();
        }
        
        //Bootstrap.mainLoop();
 
        
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
