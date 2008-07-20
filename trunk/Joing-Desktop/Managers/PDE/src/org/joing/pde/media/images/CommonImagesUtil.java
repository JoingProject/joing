/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.media.images;

import org.joing.common.desktopAPI.StandardImage;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class CommonImagesUtil
{
    public static String getFileName4Icon( StandardImage image )
    {
        String sFileName = null;
        
        switch( image )
        {
            case ALERT       : sFileName = "alert.png";        break;
            case COPY        : sFileName = "copy.png";         break;
            case CUT         : sFileName = "cut.png";          break;
            case DELETE      : sFileName = "delete.png";       break;
            case DESKTOP     : sFileName = "pde.png";          break; // Desktop logo (in this case pde.png)
            case FOLDER      : sFileName = "folder.png";       break;
            case INFO        : sFileName = "info.png";         break;
            case JOING       : sFileName = "joing.png";        break;
            case LAUNCHER    : sFileName = "launcher.png";     break;
            case LOCK        : sFileName = "lock.png";         break;
            case MOVE        : sFileName = "move.png";         break;
            case NEW         : sFileName = "new.png";          break;
            case NO_IMAGE    : sFileName = "no_image.png";     break;
            case NOTIFICATION: sFileName = "notification.png"; break;
            case PASTE       : sFileName = "paste.png";        break;
            case PRINT       : sFileName = "printer.png";      break;
            case PROPERTIES  : sFileName = "properties.png";   break;
            case REMOVE      : sFileName = "remove.png";       break;
            case SEARCH      : sFileName = "search.png";       break;
            case TRASHCAN    : sFileName = "trashcan.png";     break;
            case USER_FEMALE : sFileName = "user_female.png";  break;
            case USER_MALE   : sFileName = "user_male.png";    break;
        }
        
        return sFileName;
    }
}