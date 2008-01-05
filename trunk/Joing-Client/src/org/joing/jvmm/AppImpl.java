/*
 * AppImpl.java
 *
 * Created on Aug 5, 2007, 6:39:35 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

import org.joing.common.clientAPI.jvmm.App;
import org.joing.common.clientAPI.jvmm.JThreadGroup;
import org.joing.common.dto.app.Application;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class AppImpl implements App {

    private String mainClass;
    private JThreadGroup threadGroup;
    private Application application;

    public AppImpl(JThreadGroup tg, String className, Application application) {
        mainClass = className;
        threadGroup = tg;
        this.application = application;
    }

    @Override
    public String getMainClassName() {
        return this.mainClass;
    }

    @Override
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
    
    public Application getApplication() {
        return this.application;
    }
}
