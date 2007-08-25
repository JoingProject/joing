/*
 * AppImpl.java
 *
 * Created on Aug 5, 2007, 6:39:35 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class AppImpl implements App {

    // Todavia no esta claro para que necesita aqui el AppManager
    private AppManager appManager;
    private String mainClass;
    private JThreadGroup threadGroup;

    public AppImpl(AppManager manager, JThreadGroup tg, String className) {
        appManager = manager;
        mainClass = className;
        threadGroup = tg;
    }

    public String getMainClassName() {
        return this.mainClass;
    }

    public void destroy() {
        threadGroup.close();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(mainClass);
        sb.append("(").append(threadGroup.getName()).append(")");
        sb.append("]");
        return sb.toString();
    }
}
