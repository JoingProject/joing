/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.common.clientAPI.log;

import org.joing.common.clientAPI.log.LogListener;
import org.joing.common.clientAPI.log.Levels;
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
}
