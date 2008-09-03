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

import org.joing.kernel.api.kernel.log.LogListener;
import org.joing.kernel.api.kernel.log.Levels;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple Log Listener implementation who writes messages to Stdout.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class StdoutLogListenerImpl implements LogListener {

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private boolean timestamps = false;

    public StdoutLogListenerImpl() {
    }

    public StdoutLogListenerImpl(boolean timestamps) {
        this.timestamps = timestamps;
    }
    
    
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
