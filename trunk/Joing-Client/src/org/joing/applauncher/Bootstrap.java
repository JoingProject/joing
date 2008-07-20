/*
 * Bootstrap.java
 *
 * Created on Aug 5, 2007, 8:21:41 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.applauncher;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import javax.swing.SwingUtilities;
import org.joing.Main;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.jvmm.JoingSecurityManager;
import org.joing.jvmm.RuntimeFactory;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.runtime.Bridge2Server;
import org.joing.common.dto.app.Application;
import org.joing.jvmm.BridgeClassLoader;
import org.joing.jvmm.net.JoingURLStreamHandlerFactory;
import org.joing.jvmm.net.URLFormat;

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


        URL u = Bootstrap.class.getResource("resources/java32.png");
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
        
        String logFile = clientProps.getProperty("LogFile", null);
        if (logFile != null) {
            logger.addListener(new FileLogListenerImpl(logFile, true));
        }

        URL.setURLStreamHandlerFactory(new JoingURLStreamHandlerFactory());

        // user dir
        String userHome = System.getProperty("user.home");
        userHome = userHome + "/.joing/";
        File f = new File(userHome);
        if (f.exists() == false) {
            f.mkdir();
        }
        String joingClient = userHome + "joing-client.properties";
        f = new File(joingClient);
        Properties joingProperties = new Properties();
        try {
            if (f.exists() == true) {
                FileInputStream fis = new FileInputStream(f);
                joingProperties.load(fis);
            }
        } catch (IOException ioe) {
            logger.warning("Exception caught while loading client properties: {0}",
                    ioe.getMessage());
        }

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

            if (login.wasSuccessful()) {
                DesktopManager deskmgr =
                        getDesktopManagerInstance(login.getDesktopApplicationId());

                platform.setDesktopManager(deskmgr);

                if (login.isFullScreenRequested()) {
                    deskmgr.showInFullScreen();
                } else {
                    deskmgr.showInFrame();
                }
            } else {
                logger.info("Terminated, bad username/password.");
                platform.halt();
            }
        } catch (Exception e) {
            logger.critical("Error en start: {0}", e.getMessage());
            logger.printStackStrace(e);
        }
    }
    
    // TODO: Obtener el desktop del servidor
    private static DesktopManager getDesktopManagerInstance(int appId) {

        try {
            Platform platform = RuntimeFactory.getPlatform();
            Bridge2Server br = platform.getBridge();

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
            logger.printStackStrace(e);
            
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

            // We need to find a way to know when to break the
            // loop. Currently the Security manager is't preventing
            // the app to terminate via the System.exit() call.
//            DesktopManager desktop = platform.getDesktopManager();
//            
//            if (desktop == null) {
//                done = true;
//                continue;
//            }

            if (platform.isHalted()) {
                done = true;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                // silently ignore...
            }

        }
    }
}
