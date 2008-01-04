/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.applauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.LogListener;

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
