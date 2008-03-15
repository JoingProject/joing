/*
 * Disposer.java
 * 
 * Created on Aug 5, 2007, 6:33:44 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 * This is an example Disposer Thread. Invoked from
 * JThreadGroup.close(), should clean up threading infrastructure.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class DisposerThread extends Thread {

    private Runnable disposerTask;
    private Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    
    public DisposerThread(DisposerTask disposerTask) {
        this.disposerTask = disposerTask;
    }

    @Override
    public void run() {
        logger.debugJVMM("DisposerThread running...");
        if (disposerTask != null) {
            disposerTask.run();
        }
    }

}
