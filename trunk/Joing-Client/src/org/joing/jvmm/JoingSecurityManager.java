/*
 * JoingSecurityManager.java
 *
 * Created on Aug 5, 2007, 7:06:31 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.applauncher.Monitor;
import org.joing.common.jvmm.JThreadGroup;

/**
 *
 * @author antoniovl
 */
public class JoingSecurityManager extends SecurityManager {

    public JoingSecurityManager() {
    }

    public void checkExit(int status) {

        Throwable t = new Throwable();
        StackTraceElement[] trace = t.getStackTrace();

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
        
        if(Thread.currentThread().getId() != RuntimeFactory.getPlatform().getMainThreadId()) {
            Monitor.log("Call to exit() by unauthorized Thread with id " + Thread.currentThread().getId());
            //throw new RuntimeException("Call to exit() by unauthorized entity.");
        } 
    }

    public void checkPermission(java.security.Permission p) {
    }
}
