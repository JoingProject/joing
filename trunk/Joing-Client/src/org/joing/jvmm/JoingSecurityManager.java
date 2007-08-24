/*
 * JoingSecurityManager.java
 *
 * Created on Aug 5, 2007, 7:06:31 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

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

                JThreadGroup tg = Platform.getJThreadGroup();
                if (tg != null) {
                    tg.close();
                    throw new RuntimeException("exit");
                }
                break;
            }
        }
    }

    public void checkPermission(java.security.Permission p) {
    }
}
