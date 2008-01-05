/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.jvmm;

import org.joing.common.dto.app.Application;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface App {

    String getMainClassName();
    void destroy();
    Application getApplication();
    JThreadGroup getThreadGroup();
}
