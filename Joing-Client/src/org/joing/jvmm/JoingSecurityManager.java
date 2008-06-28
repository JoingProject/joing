/*
 * JoingSecurityManager.java
 *
 * Created on Aug 5, 2007, 7:06:31 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.jvmm.RuntimeFactory;
import org.joing.common.clientAPI.jvmm.JThreadGroup;
import org.joing.common.clientAPI.log.JoingLogger;
import org.joing.common.clientAPI.log.Levels;
import org.joing.common.clientAPI.log.Logger;
import org.joing.common.clientAPI.log.SimpleLoggerFactory;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JoingSecurityManager extends SecurityManager {

    private final Logger logger = SimpleLoggerFactory.getLogger(JoingLogger.ID);
    
    public JoingSecurityManager() {
    }

    @Override
    public void checkExit(int status) {     
        
        Throwable t = new Throwable();
        StackTraceElement[] trace = t.getStackTrace();
        String name = Thread.currentThread().getName();

        for (int i = 0; i < trace.length; i++) {

            String className = trace[i].getClassName();
            String methodName = trace[i].getMethodName();

            if (className.equals("java.lang.System") && methodName.equals("exit")) {

                JThreadGroup tg = RuntimeFactory.getPlatform().getJThreadGroup();
                if (tg != null) {
                    tg.close();
                    throw new RuntimeException("exit");
                }
                break;
            }
        }
        
        super.checkExit(status);
        
        if (Thread.currentThread().getId() !=
                RuntimeFactory.getPlatform().getMainThreadId()) {

            StringBuilder sb = new StringBuilder("Call to exit() ");
            sb.append("with status ").append(status).append(" by ");
            sb.append("unauthorized Thread[").append(name).append("] ");
            sb.append("with id ");
            sb.append(Thread.currentThread().getId()).append("\n");
            for (int i = 0; i < trace.length; i++) {
                sb.append(trace[i].toString()).append("\n");
            }
            logger.warning(sb.toString());

            // TODO: Fix this.
            // uncommenting this will prevent the application to exit.
            // We must fix the mainLoop() before doing it.
           // throw new SecurityException(sb.toString());
        }
    }

    @Override
    public void checkPermission(java.security.Permission p) {
    }
}
