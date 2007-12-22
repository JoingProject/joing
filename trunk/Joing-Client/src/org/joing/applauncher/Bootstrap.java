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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import org.joing.Main;
import org.joing.common.desktopAPI.DesktopManager;
import org.joing.common.clientAPI.jvmm.Platform;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.jvmm.JoingSecurityManager;
import org.joing.jvmm.RuntimeFactory;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;
import org.joing.applauncher.StdoutListenerImpl;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Bootstrap {

    private static final Logger logger = 
            SimpleLoggerFactory.getLogger(Main.class);
    
    public Bootstrap() {
    }

    private static void setupTrayIcon() {

        if (SystemTray.isSupported() == false) {
            logger.write(Levels.WARNING, "Tray Icon not supported.");
            return;
        }

        URL u = Bootstrap.class.getResource("resources/java32.png");
        Image image = Toolkit.getDefaultToolkit().getImage(u);

//        MouseListener mouseListener = new MouseListener() {
//
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("Tray Icon - Mouse clicked!");
//            }
//
//            public void mouseEntered(MouseEvent e) {
//                System.out.println("Tray Icon - Mouse entered!");
//            }
//
//            public void mouseExited(MouseEvent e) {
//                System.out.println("Tray Icon - Mouse exited!");
//            }
//
//            public void mousePressed(MouseEvent e) {
//                System.out.println("Tray Icon - Mouse pressed!");
//            }
//
//            public void mouseReleased(MouseEvent e) {
//                System.out.println("Tray Icon - Mouse released!");
//            }
//        };
        ActionListener exitListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
//                    System.out.println("Tray Icon - Mouse pressed!");
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
        
        try
        {
            SystemTray.getSystemTray().add(trayIcon);    
        }
        catch ( Exception exc )
        {
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
     * The Class responsible of the Login must call <code>Bootstrap.go()</code> in the event
     * of fetching a session Id.
     */
    public static void init() {
        
        // Initialization of Logging subsystem.
        logger.addListener(new StdoutListenerImpl(true));
        
        System.setSecurityManager(new JoingSecurityManager());
        logger.write(Levels.NORMAL, "Join'g Successfully Bootstrapped.");
        logger.write(Levels.NORMAL, "Main Thread Id is {0}",  
                String.valueOf(RuntimeFactory.getPlatform().getMainThreadId()));

        setupTrayIcon();
        
        // Iniciamos la sesión.
        try {
            Login login = new Login();
            login.setVisible(true);

            if (login.wasSuccessful()) {
                DesktopManager deskmgr = getDesktopManagerInstance( login.getApplicationDescriptor() );
                               deskmgr.setPlatform( RuntimeFactory.getPlatform() );
                RuntimeFactory.getPlatform().setDesktopManager(deskmgr);

                if (login.fullScreen()) {
                    deskmgr.showInFullScreen();
                } else {
                    deskmgr.showInFrame();
                }
            } else {
                RuntimeFactory.getPlatform().halt();
            }
        } catch (Exception e) {
            logger.write(Levels.CRITICAL, "Error en start: {0}", e.getMessage());
        }
    }
    

    // TODO: Obtener el desktop del servidor
    private static DesktopManager getDesktopManagerInstance( AppDescriptor appDesc ) {
        try {
            Platform platform = RuntimeFactory.getPlatform();
            // TODO: Bajarse del server el DesktopManager descrito en appDesc y lanzarla
            String desktop = platform.getClientProp().getProperty("DesktopApp");
            String[] tmp = desktop.split("\\?");
            desktop = tmp[0];
            String mainClass = tmp[1];
            //String serverEjb = platform.getClientProp().getProperty("JoingServerEjb");
            //String desktopApi = platform.getClientProp().getProperty("DesktopApi");
            //String joingClient = platform.getClientProp().getProperty("JoingClient");
            
            URL[] url = new URL[] {
                new URL(desktop),
                //new URL(serverEjb),
                //new URL(desktopApi),
                //new URL(joingClient)
            };
            URLClassLoader ucl = new URLClassLoader(url, platform.getClass().getClassLoader());
            Class clazz = ucl.loadClass(mainClass);
            
            return (DesktopManager) clazz.newInstance();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}