/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.runtime;

/**
 *
 * @author antoniovl
 */
public interface Bridge2Server {

    SessionBridge getSessionBridge();
    UserBridge getUserBridge();
    AppBridge getAppBridge();
    VFSBridge getFileBridge();
    
}
