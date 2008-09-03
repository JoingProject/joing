/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.pde.media.images;

import org.joing.kernel.api.desktop.StandardImage;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class ImagesUtil
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
            case FILE        : sFileName = "file.png";         break;
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