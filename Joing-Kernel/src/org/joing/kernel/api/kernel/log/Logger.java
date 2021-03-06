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

package org.joing.kernel.api.kernel.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Message Logger main class.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class Logger {

    private final Object listenerLock = new Object();
    private final Object levelsLock = new Object();
    private final List<LogListener> listeners = new ArrayList<LogListener>();
    private final Set<Levels> levels = new HashSet<Levels>();

    public Logger() {
        levels.addAll(EnumSet.of(Levels.NORMAL, Levels.INFO, Levels.WARNING,
                Levels.CRITICAL));
    }

    public void addListener(LogListener listener) {

        synchronized (listenerLock) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(LogListener listener) {

        synchronized (listenerLock) {
            this.listeners.remove(listener);
        }
    }

    public void addLevel(Levels level) {

        synchronized (levelsLock) {
            this.levels.add(level);
        }
    }

    public void addLevels(Levels... levels) {
        for (Levels level : levels) {
            addLevel(level);
        }
    }

    public void removeLevel(Levels level) {

        // NORMAL level can't be removed.
        if (level.equals(Levels.NORMAL)) {
            return;
        }

        synchronized (levelsLock) {
            this.levels.remove(level);
        }
    }

    public void fireListeners(Levels level, String msg) {

        synchronized (listenerLock) {
            for (LogListener listener : this.listeners) {
                listener.write(level, msg);
            }
        }

    }

    public void write(Levels level, String format, Object... args) {

        synchronized (levelsLock) {
            if (this.levels.contains(level) == true) {
                String s = MessageFormat.format(format, args);
                fireListeners(level, s);
            }
        }
    }
    
    public void normal(String format, Object... args) {
        write(Levels.NORMAL, format, args);
    }
    
    public void info(String format, Object... args) {
        write(Levels.INFO, format, args);
    }
    
    public void warning(String format, Object... args) {
        write(Levels.WARNING, format, args);
    }
    
    public void critical(String format, Object... args) {
        write(Levels.CRITICAL, format, args);
    }
    
    public void debug(String format, Object... args) {
        write(Levels.DEBUG, format, args);
    }
    
    public void debugJVMM(String format, Object... args) {
        write(Levels.DEBUG_JVMM, format, args);
    }
    
    public void printStackTrace(Exception e) {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        
        e.printStackTrace(ps);
        
        String s = baos.toString();
        
        critical(s);
    }
}
