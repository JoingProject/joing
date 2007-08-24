/*
 * Monitor.java
 *
 * Created on Aug 5, 2007, 9:45:27 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.AppLauncher;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joing.AppLauncher.gui.SystemMonitor;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Monitor extends SystemMonitor {

    private static final SystemMonitor systemMonitor = new SystemMonitor();

    public Monitor() {
    }

    public static SystemMonitor getSystemMonitor() {
        return systemMonitor;
    }
    
    /**
     * Appends a Line to the SystemMonitor Log.
     */
    public static void log(String msg) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();

        sb.append("[").append(sdf.format(new Date())).append("]: ");
        sb.append(msg);
        systemMonitor.addLogMessage(sb.toString());  
    }
}
