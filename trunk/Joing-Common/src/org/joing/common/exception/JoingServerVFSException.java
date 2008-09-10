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
 *
 * @author Francisco Morero Peyrona
 */
public class JoingServerVFSException extends JoingServerException
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    public final static String INVALID_OWNER         = "INVALID_OWNER";
    public final static String FILE_NOT_EXISTS       = "FILE_NOT_EXISTS";
    public final static String FILE_NAME_EXISTS      = "FILE_NAME_EXISTS";
    public final static String NOT_DELETEABLE        = "NOT_DELETEABLE";
    public final static String NOT_ALTERABLE         = "NOT_ALTERABLE";
    public final static String NOT_READABLE          = "NOT_READABLE";
    public final static String NOT_MODIFIABLE        = "NOT_MODIFIABLE";
    public final static String LOCKED_BY_ANOTHER     = "LOCKED_BY_ANOTHER";
    public final static String PARENT_DIR_NOT_EXISTS = "PARENT_DIR_NOT_EXISTS";
    public final static String INVALID_PARENT        = "INVALID_PARENT";
    public final static String FILE_ALREADY_EXISTS   = "FILE_ALREADY_EXISTS";
    public final static String DIR_ALREADY_EXISTS    = "DIR_ALREADY_EXISTS";
    public final static String NO_QUOTA              = "NO_QUOTA";
    
    public JoingServerVFSException()
    {
        super();
    }
    
    public JoingServerVFSException( String message )
    {
        this( message, null );
    }
    
    public JoingServerVFSException( String message, Throwable cause )
    {
        super( I18N4Exceptions.getLocalized( JoingServerVFSException.class, message ), cause );
    }
}