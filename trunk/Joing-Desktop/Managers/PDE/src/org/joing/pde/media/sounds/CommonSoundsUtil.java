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
public class CommonSoundsUtil
{
    public static URL getURL4Sound( StandardSound sound )
    {
        URL    url   = null;
        String sFile = null;
                
        switch( sound )
        {
            case WELCOME  : sFile = "welcome.wav"; break;
            case GOODBYE  : sFile = "goodbye.ogg"; break;
        }
        
        if( sFile != null )
            url = CommonSoundsUtil.class.getResource( sFile );
        
        return url;
    }
}