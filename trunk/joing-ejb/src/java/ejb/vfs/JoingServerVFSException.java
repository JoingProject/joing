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
package ejb.vfs;

import ejb.JoingServerException;

/**
 *
 * @author fmorero
 */
public class JoingServerVFSException extends JoingServerException
{
    final static String INVALID_OWNER         = "Operation can not be performed because the account does not ownes the file.";
    final static String FILE_NOT_EXISTS       = "File does not exists.";
    final static String FILE_NAME_EXISTS      = "File name already exists.";
    final static String NOT_DELETEABLE        = "Can not delete: file or directory is marked as not deleteable.";
    final static String NOT_ALTERABLE         = "Can not modfify attributes: file or directory is marked as not alterable.";
    final static String PARENT_DIR_NOT_EXISTS = "Invalid parent directory: it does not exists.";
    final static String INVALID_PARENT        = "Invalid parent: it is not a directory but a file.";
    final static String FILE_ALREADY_EXISTS   = "Invalid name: file already exists.";
    final static String DIR_ALREADY_EXISTS    = "Invalid name: directory already exists.";
    final static String NO_QUOTA              = "Sorry but file can't be saved: you do not have eought free space in your disk. Please contact with the system administrator.";
    
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