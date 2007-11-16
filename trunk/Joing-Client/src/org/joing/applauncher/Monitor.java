/*
 * Monitor.java
 *
 * Created on Aug 5, 2007, 9:45:27 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.applauncher;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Monitor{
    
    public Monitor() {
    }
    
    /**
     * Appends a Line to the SystemMonitor Log.
     */
    public static void log(String msg) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();

        sb.append("[").append(sdf.format(new Date())).append("]: ");
        sb.append(msg);
        // FIXME: hacerlo con eventos --> systemMonitor.addLogMessage(sb.toString());
    }
}