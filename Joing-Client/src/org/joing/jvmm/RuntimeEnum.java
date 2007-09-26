/*
 * RuntimeEnum.java
 * 
 * Created on Sep 25, 2007, 11:54:35 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 * This definitions will populate the Client Properties file.
 * 
 * @author antoniovl
 */
public enum RuntimeEnum {
    
    JOING_SERVER_URL("JoingServerURL");
    
    private final String key;
    
    public String getKey() {
        return this.key;
    }
    
    RuntimeEnum(String key) {
        this.key = key;
    }
}
