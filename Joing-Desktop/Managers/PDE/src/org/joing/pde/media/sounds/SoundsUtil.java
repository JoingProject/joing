/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.media.sounds;

import java.net.URL;
import org.joing.common.desktopAPI.StandardSound;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class SoundsUtil
{
    public static URL getURL4Sound( StandardSound sound )
    {
        URL    url   = null;
        String sFile = null;
                
        switch( sound )
        {
            case WELCOME  : sFile = "welcome.wavkk"; break;
            case GOODBYE  : sFile = "goodbye.wavkk"; break;
          //case MAXIMIZE
          //case MINIMIZE
          //case RESTORE
          //case WARNING
          //case ERROR
          //case QUESTION
        }
        
        if( sFile != null )
            url = SoundsUtil.class.getResource( sFile );
        
        return url;
    }
}