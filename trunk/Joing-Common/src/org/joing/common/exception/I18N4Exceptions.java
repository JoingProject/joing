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

package org.joing.common.exception;

/**
 * This class is absolutely inefficient (synchronized methods, resource boundle
 * is created on every invocation, etc), but it does not need to be efficent: An
 * exception is something that (hopefully) does no happen frecuently and when it
 * happens, it is important to create it that fast.
 * 
 * @author Francisco Morero Peyrona
 */
class I18N4Exceptions
{
    static synchronized String getLocalized( Class clazz, String id )
    {
        String sMsg       = id;   // It could be that passed msg is not one of defined constants in exception classes
        String sClassName = clazz.getName();
               sClassName = sClassName.substring( sClassName.lastIndexOf( '.' ) + 1 );
        
//        ResourceBundle bundle;
//        
//        try
//        {
//            bundle = ResourceBundle.getBundle( "exceptions.i18n.properties" );
//        }
//        catch( MissingResourceException exc )
//        {
//            exc.printStackTrace();
//        }
        
               
        // NEXT: Place strings in a resource boundle and use class name as string prefix
        
        if( "JoingServerException".equals( sClassName ) )
        {
            if(      "ACCESS_DB".equals(  id ) )  sMsg = "Error accessing database.";
            else if( "ACCESS_NFS".equals( id ) )  sMsg = "Error accessing native file system.";
        }
        else if( "JoingServerAppException".equals( sClassName ) )
        {
            if(      "INVALID_OWNER".equals(    id ) )  sMsg = "The account does not has priviledges to execute this application.";
            else if( "APP_NOT_EXISTS".equals(   id ) )  sMsg = "Requested application does not exists.";
            else if( "JAR_ACCESS_ERROR".equals( id ) )  sMsg = "Error reading associated JAR file.";
        }
        else if( "JoingServerSessionException".equals( sClassName ) )
        {
            if( "LOGIN_EXISTS".equals( id ) ) sMsg = "The combination of account (user name) and password,\n"+
                                                     "already exists. Please, try another one.";
        }
        else if( "JoingServerUserException".equals( sClassName ) )
        {
            if(      "NOT_ATTRIBUTES_OWNER".equals( id ) ) sMsg = "It looks like you are trying to update another user's\n"+
                                                                  "information: an user can change only its own attributes.";
            else if( "INVALID_ACCOUNT".equals(      id ) ) sMsg = "Invalid account: too short, to long and/or contains invalid characters.";
            else if( "INVALID_PASSWORD".equals(     id ) ) sMsg = "Invalid password length, minimum = 6 and maximum = 32 characters.";
        }
        else if( "JoingServerVFSException".equals( sClassName ) )
        {
            if(      "INVALID_OWNER".equals(         id ) ) sMsg = "Operation can not be performed because\nthe account does not ownes the file.";
            else if( "FILE_NOT_EXISTS".equals(       id ) ) sMsg = "File does not exists.";
            else if( "FILE_NAME_EXISTS".equals(      id ) ) sMsg = "File name already exists.";
            else if( "NOT_DELETEABLE".equals(        id ) ) sMsg = "Can not delete: file or directory is marked as not deleteable.";
            else if( "NOT_ALTERABLE".equals(         id ) ) sMsg = "Can not modify attributes:\nfile or directory is marked as not alterable.\nOnly the owner of the entity is allowed to do it.";
            else if( "NOT_READABLE".equals(          id ) ) sMsg = "File is marked as no-readable:\nyou have to change this attribute prior to read it.";
            else if( "NOT_MODIFIABLE".equals(        id ) ) sMsg = "File is marked as un-modifiable:\nyou have to change this attribute prior to write into it.";
            else if( "LOCKED_BY_ANOTHER".equals(     id ) ) sMsg = "Can not write int file:\nit is locked and you do not own the lock.";
            else if( "PARENT_DIR_NOT_EXISTS".equals( id ) ) sMsg = "Invalid parent directory: it does not exists.";
            else if( "INVALID_PARENT".equals(        id ) ) sMsg = "Invalid parent: it is not a directory but a file.";
            else if( "FILE_ALREADY_EXISTS".equals(   id ) ) sMsg = "Invalid name: file already exists.";
            else if( "DIR_ALREADY_EXISTS".equals(    id ) ) sMsg = "Invalid name: directory already exists.";
            else if( "NO_QUOTA".equals(              id ) ) sMsg = "Sorry but file can't be saved:\nyou do not have enought free space in your disk.\nPlease contact with the system administrator.";
        }
        
        return sMsg;
    }
}
