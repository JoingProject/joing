/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.clientAPI.log;

import org.joing.common.clientAPI.log.Levels;

/**
 * Public interface for the Event Logger.
 * The concrete implementation must add all relevant info, like
 * timestamps and log level details.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public interface LogListener {
    
    /**
     * Writes the message specified in the argument msg. The concrete 
     * implementation can add relevant aditional info, like timestamps.
     * @param level Level of the message. If the level is not listed in the
     * Logger class, this method will be ignored.
     * @param msg String with the message.
     */
    void write(Levels level, String msg);
    
}
