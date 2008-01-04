/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm.net;

import java.net.MalformedURLException;
import java.net.URL;
import org.joing.common.dto.app.Application;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class URLFormat {

    /**
     * Builds an URL object from the Application and extra parameter.
     * @param application Application referred.
     * @param entry Entry within the enclosed jar.
     * @return URL Object.
     * @throws java.net.MalformedURLException
     */
    public static URL toURL(Application application, String entry)
            throws MalformedURLException {

        StringBuilder sb = new StringBuilder("bridge2server:/!/?appId=");

        sb.append(application.getId());
        
        if (entry != null) {
            sb.append("#").append(entry);
        }

        return new URL(sb.toString());
    }
    
    public static URL toURL(Application application) 
            throws MalformedURLException {
        return toURL(application, null);
    }
    
}
