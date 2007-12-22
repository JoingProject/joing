/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple Log Listener implementation who writes messages to Stdout.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class StdoutListenerImpl implements LogListener {

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private boolean timestamps = false;
    
    /**
     * Concrete Implementation of write.
     * @param level Log Level.
     * @param msg Message to be written.
     * @see org.joing.log.LogListener#write LogListener.write()
     */
    public void write(Levels level, String msg) {
        
        StringBuilder sb = new StringBuilder("");
        
        if (isTimestamps() == true) {
            sb.append("[").append(dateFormat.format(new Date())).append("]");
        }
        
        sb.append("[").append(level.toString()).append("]: ");
        sb.append(msg);
        
        System.out.println(sb.toString());
        
    }

    public boolean isTimestamps() {
        return timestamps;
    }

    public void setTimestamps(boolean timestamps) {
        this.timestamps = timestamps;
    }

}