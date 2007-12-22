/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.log;

/**
 * Levels of Logging.
 * NORMAL: Standard messages.
 * INFO: More verbose standard messages.
 * WARNING:
 * CRITICAL: Critical Errors, ex. exceptions.
 * DEBUG*: Messages intended for debugging.
 * 
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public enum Levels {
    NORMAL,
    INFO,
    WARNING,
    CRITICAL,
    DEBUG,
    DEBUG_JVMM,
    DEBUG_DESKTOP
}
