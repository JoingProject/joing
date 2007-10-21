/*
 * JoingVFSException.java
 * 
 * Created on 25-jul-2007, 16:40:02
 * 
 * Author: Francisco Morero Peyrona.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.joing.common.exceptions;

import org.joing.common.exceptions.JoingServerException;

/**
 *
 * @author fmorero
 */
public class JoingServerVFSException extends JoingServerException
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    public final static String INVALID_OWNER         = "Operation can not be performed because\nthe account does not ownes the file.";
    public final static String FILE_NOT_EXISTS       = "File does not exists.";
    public final static String FILE_NAME_EXISTS      = "File name already exists.";
    public final static String NOT_DELETEABLE        = "Can not delete: file or directory is marked as not deleteable.";
    public final static String NOT_ALTERABLE         = "Can not modify attributes:\nfile or directory is marked as not alterable.\nOnly the owner of the entity is allowed to do it.";
    public final static String NOT_READABLE          = "File is marked as no-readable:\nyou have to change this attribute prior to read it.";
    public final static String NOT_MODIFIABLE        = "File is marked as un-modifiable:\nyou have to change this attribute prior to write into it.";
    public final static String LOCKED_BY_ANOTHER     = "Can not write int file:\nit is locked and you do not own the lock.";
    public final static String PARENT_DIR_NOT_EXISTS = "Invalid parent directory: it does not exists.";
    public final static String INVALID_PARENT        = "Invalid parent: it is not a directory but a file.";
    public final static String FILE_ALREADY_EXISTS   = "Invalid name: file already exists.";
    public final static String DIR_ALREADY_EXISTS    = "Invalid name: directory already exists.";
    public final static String NO_QUOTA              = "Sorry but file can't be saved:\nyou do not have enought free space in your disk.\nPlease contact with the system administrator.";
    
    public JoingServerVFSException()
    {
        super();
    }
    
    public JoingServerVFSException( String message )
    {
        super( message );
    }
    
    public JoingServerVFSException( String message, Throwable cause )
    {
        super( message, cause );
    }
}