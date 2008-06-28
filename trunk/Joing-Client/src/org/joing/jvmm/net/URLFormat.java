/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joing.jvmm.net;

import java.net.MalformedURLException;
import java.net.URL;
import org.joing.common.dto.app.Application;

/**
 * Class with utility methods to build bridge2server style URL's. The URL 
 * format is:
 * <ol>
 *  <li>
 *   bridge2server:/!/?appId=NN
 *  </li>
 *  <li>
 *   bridge2server:/!/?appId=NN#/path/to/file
 *  </li>
 *  </ol>
 * The NN value in the query part indicates the appId value found in 
 * Application.getId(). The optional part following the pound signs indicates 
 * a file within the enclosed jar file. The former format it's used by 
 * ClassLoader.findResource().
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class URLFormat {

    /**
     * Builds an URL object from the Application and extra parameter.
     * @param application Application referred.
     * @param entry Entry within the enclosed jar. If this argument is
     * supplied with null value, the method will build an URL pointing to
     * the Jar file. 
     * @return URL Object.
     * @throws java.net.MalformedURLException
     */
    public static URL toURL(Application application, String entry)
            throws MalformedURLException {

        //StringBuilder sb = new StringBuilder("bridge2server:/!/?appId=");
        StringBuilder sb = new StringBuilder("bridge2server:appId=");

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
