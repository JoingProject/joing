/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.common.clientAPI.runtime;

/**
 * Interface to access server side from client side.
 * 
 * @author antoniovl
 */
public interface Bridge2Server
{
    /**
     * Bridge to server for actions related to Sessions.
     * @return A bridge to server for actions related to Sessions.
     */
    SessionBridge getSessionBridge();

    /**
     * Bridge to server for actions related to Users.
     * @return A bridge to server for actions related to Users.
     */
    UserBridge getUserBridge();

    /**
     * Bridge to server for actions related to Applications.
     * @return A bridge to server for actions related to Applications.
     */
    AppBridge getAppBridge();

    /**
     * Bridge to server for actions related to Files.
     * @return A bridge to server for actions related to Files.
     */
    VFSBridge getFileBridge();
}
