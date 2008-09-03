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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joing.kernel.api.kernel.log.Levels;
import org.joing.kernel.api.kernel.log.LogListener;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class FileLogListenerImpl implements LogListener {

    private FileOutputStream logFile;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean timestamps = true;

    public FileLogListenerImpl(String fileName, boolean create) {

        File file = new File(fileName);

        try {
            this.logFile = new FileOutputStream(file, create);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe.getMessage());
        }
    }

    public void write(Levels level, String msg) {

        StringBuilder sb = new StringBuilder("");

        if (isTimestamps() == true) {
            sb.append("[").append(dateFormat.format(new Date())).append("]");
        }

        sb.append("[").append(level.toString()).append("]: ");
        sb.append(msg);
        sb.append("\n");

        try {
            logFile.write(sb.toString().getBytes());
        } catch (IOException ioe) {
        // do nothing
        }

    }

    public boolean isTimestamps() {
        return timestamps;
    }

    public void setTimestamps(boolean timestamps) {
        this.timestamps = timestamps;
    }
}
