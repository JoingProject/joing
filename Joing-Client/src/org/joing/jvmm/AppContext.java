/*
 * AppContext.java
 * 
 * Created on Aug 5, 2007, 4:53:54 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class AppContext implements java.io.Serializable {

    /**
     * do we really need this class???
     */
    
    
    /** Session id otorgado por WebPcServer. */
    private String sessionId;
    /** URL del FileServer */
    private String fileServerUrl;

    public AppContext() {
    }
    
    // <editor-fold defaultstate="collapsed" desc="setter/getter">
    public String getFileServerUrl() {
        return fileServerUrl;
    }

    public void setFileServerUrl(String fileServerUrl) {
        this.fileServerUrl = fileServerUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    // </editor-fold>
}
