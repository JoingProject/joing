/*
 * Bootstrap.java
 *
 * Created on Aug 5, 2007, 8:21:41 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.AppLauncher;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.joing.AppLauncher.gui.SystemMonitor;
import org.joing.jvmm.JoingSecurityManager;
import org.joing.jvmm.Platform;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Bootstrap {

    public Bootstrap() {
    }

    public static void setupSystemMonitor() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
//                SystemMonitor sm = new SystemMonitor();
                final SystemMonitor sm = Monitor.getSystemMonitor();

                JFrame frame = new JFrame("Join'g System Monitor");
                frame.getContentPane().add(sm);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                try {
                    setupTrayIcon(frame);
                    frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
                } catch (LinkageError le) {
                    frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    Monitor.log("Unable to Create Tray Icon.");
                }
            }
        });
    }



    private static void setupTrayIcon(final JFrame frame) {

        if (SystemTray.isSupported() == false) {
            System.err.println("Tray Icon not supported...");
            Monitor.log("Tray Icon not supported.");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
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
        
        ActionListener frameAction = new ActionListener() {
            boolean firstTime = true;

            public void actionPerformed(ActionEvent e) {
                if (firstTime) {
                    firstTime = false;
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    Dimension size = frame.getSize();
                    frame.setLocation(screen.width / 2 - size.width / 2, screen.height / 2 - size.height / 2);
                }
                frame.setVisible(true);
            }
        };

        final PopupMenu popup = new PopupMenu();
        MenuItem appItem = new MenuItem("System Monitor");
        popup.add(appItem);
        appItem.addActionListener(frameAction);
        MenuItem gcItem = new MenuItem("Garbage Collect");
        popup.add(gcItem);
        gcItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Map classLoaderCache = Platform.getClassLoaderCache();
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
        trayIcon.addActionListener(frameAction);
        //trayIcon.addMouseListener(mouseListener);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
//            System.err.println("TrayIcon could not be installed...");
        }
    }

    public static void init() {
        setupSystemMonitor();
        System.setSecurityManager(new JoingSecurityManager());
        Monitor.log("Join'g Successfully Bootstrapped.");
        Monitor.log("Main Thread Id is " + Platform.getMainId());
    }

    // do we really need this method?
    public static void go() {
    }
}
