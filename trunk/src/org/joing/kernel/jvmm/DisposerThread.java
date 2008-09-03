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

package org.joing.kernel.jvmm;

import java.util.UUID;
import org.joing.kernel.api.kernel.log.JoingLogger;
import org.joing.kernel.api.kernel.log.Logger;
import org.joing.kernel.api.kernel.log.SimpleLoggerFactory;

/**
 * This is an example Disposer Thread. Invoked from
 * JThreadGroup.close(), should clean up threading infrastructure.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class DisposerThread extends Thread {

    private Runnable disposerTask;
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    
//    public DisposerThread(ThreadGroup threadGroup, DisposerTask disposerTask) {
//        super(threadGroup, 
//                "Disposer Thread[".concat(UUID.randomUUID().toString()).concat("]"));
//        this.disposerTask = disposerTask;
//    }

    public DisposerThread(ThreadGroup threadGroup) {
        super(threadGroup, 
                "DisposerThread[".concat(UUID.randomUUID().toString()).concat("]"));
    }

    
    public void setDisposerTask(DisposerTask task) {
        this.disposerTask = task;
    }
    
    @Override
    public void run() {
        logger.debugJVMM("DisposerThread running...");
        if (disposerTask != null) {
            disposerTask.run();
        }
    }

}
