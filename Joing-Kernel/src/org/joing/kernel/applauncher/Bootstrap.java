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

package org.joing.kernel.applauncher;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import javax.swing.SwingUtilities;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.session.LoginResult;
import org.joing.kernel.Main;
import org.joing.kernel.api.bridge.Bridge2Server;
import org.joing.kernel.api.kernel.jvmm.Platform;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Levels;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;
import org.joing.kernel.api.desktop.DesktopManager;
import org.joing.kernel.jvmm.BridgeClassLoader;
import org.joing.kernel.jvmm.JoingSecurityManager;
import org.joing.kernel.jvmm.RuntimeFactory;
import org.joing.kernel.jvmm.net.JoingURLStreamHandlerFactory;
import org.joing.kernel.jvmm.net.URLFormat;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Bootstrap {

    private static final Logger logger =
            SimpleLoggerFactory.getLogger(JoingLogger.ID);

    public Bootstrap() {
    }

    public static void setupTrayIcon() {

        // See JavaSE bugid 6438179
        if (SystemTray.isSupported() == false) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tray Icon not supported. This is a known issue ");
            sb.append("when running Beryl or Compiz on Linux.");
            logger.warning(sb.toString());
            return;
        }


        URL u = Bootstrap.class.getResource("resources/joing_icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(u);

        ActionListener exitListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RuntimeFactory.getPlatform().shutdown();
            }
        };

        final PopupMenu popup = new PopupMenu();
        MenuItem gcItem = new MenuItem("Garbage Collect");
        popup.add(gcItem);
        gcItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Map classLoaderCache = RuntimeFactory.getPlatform().getClassLoaderCache();
                synchronized (classLoaderCache) {
                    classLoaderCache.clear();
                }
                System.gc();
                System.runFinalization();
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
                System.gc();
                int amount = (int) Runtime.getRuntime().totalMemory();
                byte[] data = null;
                try {
                    data = new byte[amount];
                } catch (OutOfMemoryError err) {

                }
                data = null;
                System.gc();
                System.runFinalization();
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
                System.gc();
            }
        });

        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(exitListener);
        popup.add(defaultItem);
        TrayIcon trayIcon = new TrayIcon(image, "Java", popup);
        trayIcon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (Exception exc) {
        // TODO: hacer algo?
        }
    }

    /**
     * <code>init()</code> is the initialization procedure for the main
     * subsystem. The general contract for init() is:
     * <ui>
     * <li>Initialize the low level structures.</li>
     * <li>Initialize the security manager.</li>
     * <li>Determine the Class responible of doing the Login process.</li>
     * </ui>
     */
    public static void init() {
        
        Platform platform = RuntimeFactory.getPlatform();
        
        //debug
        logger.debugJVMM("init(): mainThread: {0}",
                platform.getMainThreadId());
        
        Properties clientProps = platform.getClientProp();
        
        // Initialization of Logging subsystem. 
        // Platform need at least one logger, we need to setup
        // this first.
        logger.addListener(new StdoutLogListenerImpl(true));
        
        if (Boolean.valueOf(clientProps.getProperty("debug"))) {
            logger.addLevel(Levels.DEBUG);
        }
        if (Boolean.valueOf(clientProps.getProperty("debug.JVMM"))) {
            logger.addLevel(Levels.DEBUG_JVMM);
        }
        if (Boolean.valueOf(clientProps.getProperty("debug.Cache"))) {
            logger.addLevel(Levels.DEBUG_CACHE);
        }
        if (Boolean.valueOf(clientProps.getProperty("debug.Desktop"))) {
            logger.addLevel(Levels.DEBUG_DESKTOP);
        }
        
        URL.setURLStreamHandlerFactory(new JoingURLStreamHandlerFactory());

        // user dir
        String userHome = System.getProperty("user.home") + "/.joing/";
        File fJoingDir = new File(userHome);
        if (fJoingDir.exists() == false) {
            fJoingDir.mkdir();
        }
        
        // Join'g Log file (created inside {user.home}.joing directory)
        File fLog = new File( fJoingDir, "joing.log" );
        logger.addListener(new FileLogListenerImpl( fLog.getAbsolutePath(), true) );

        System.setSecurityManager(new JoingSecurityManager());
        logger.info("Join'g Successfully Initialized.");
        logger.info("Main Thread Id is {0}",
                String.valueOf(RuntimeFactory.getPlatform().getMainThreadId()));

        // Debera ser invokeAndWait?
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupTrayIcon();
            }
        });
    }


    public static void loginDialog() {

        Platform platform = RuntimeFactory.getPlatform();

        // Iniciamos la sesión.
        try {
            Login login = new Login();
            login.setVisible(true);

            LoginResult result = login.getLoginResult();
   
            if ((result != null) && (result.isLoginValid())) {
                DesktopManager deskmgr =
                        getDesktopManagerInstance(login.getDesktopApplicationId());

                platform.setDesktopManager(deskmgr);

                if (login.isFullScreenRequested()) {
                    deskmgr.showInFullScreen();
                } else {
                    deskmgr.showInFrame();
                }

                login.disposeSplash();
            } else if (result == null) {
                logger.critical("Got null LoginResult.");
            } else {
                logger.info("Terminated, bad username/password.");
                platform.halt();
            }
        } catch (Exception e) {
            logger.critical("Error en start: {0}", e.getMessage());
            logger.printStackTrace(e);
        }
    }
    
    // TODO: Obtener el desktop del servidor
    private static DesktopManager getDesktopManagerInstance(int appId) {

        try {
            Platform platform = RuntimeFactory.getPlatform();
            Bridge2Server br = org.joing.kernel.jvmm.RuntimeFactory.getPlatform().getBridge();

            Application app = br.getAppBridge().getApplication(appId);
            URL url = URLFormat.toURL(app);

            BridgeClassLoader classLoader =
                    new BridgeClassLoader(br, new URL[]{url},
                    Main.class.getClassLoader());

            JarInputStream jis = new JarInputStream(app.getContent());
            Manifest manifest = jis.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String mainClass = attributes.getValue("Main-Class");

            Class clazz = classLoader.loadClass(mainClass);
            platform.getClassLoaderCache().put(app.getExecutable(), classLoader);

            return (DesktopManager) clazz.newInstance();

        } catch (Exception e) {
            logger.critical("Exception Caught while getting the Desktop Instance",
                    e.getMessage());
            logger.printStackTrace(e);
            
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void mainLoop() {

        Platform platform = RuntimeFactory.getPlatform();
        boolean done = false;

        logger.debugJVMM("Thread {0} ({1}) is running the Main Loop.",
                Thread.currentThread().getName(), 
                Thread.currentThread().getId());
        
        // TODO: Fix this.
        while (!done) {

            if (platform.isHalted()) {
                done = true;
                logger.debugJVMM("Terminating the main loop, see you later.");
                continue;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                // silently ignore...
            }

        }
    }
}
