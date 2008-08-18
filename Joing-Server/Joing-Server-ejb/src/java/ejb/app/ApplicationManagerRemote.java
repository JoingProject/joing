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
package ejb.app;

import java.util.List;
import javax.ejb.Remote;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;
import org.joing.common.exception.JoingServerVFSException;

/**
 * This is the business interface for <code>ApplicationManager</code> enterprise
 * bean.
 */
@Remote
public interface ApplicationManagerRemote
{    
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, all
     * applications that user can install (regardless of their installation
     * status).
     * <p>
     * The list should not be empty, will be <code>null</code> if something 
     * went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param groupKey App group. Refer to <code>org.joing.common.dto.app.AppGroup</code>
     * @return List of applications matching the criteria.
     */
    List<AppGroup> getAvailableForUser( String sSessionId, AppGroupKey groupKey )
                   throws JoingServerAppException;
    
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, all the
     * set of applications that user can install, only those that are not
     * installed yet.
     * <p>
     * The list can be empty if there is none Application matching the criteria,
     * or will be <code>null</code> if something went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param groupKey App group. Refer to <code>org.joing.common.dto.app.AppGroup</code>
     * @return List of applications matching the criteria.
     */
    List<AppGroup> getNotInstalledForUser( String sSessionId, AppGroupKey groupKey )
                   throws JoingServerAppException;
    
    /**
     * Retrieve a <code>List</code> with instances of <code>Application</code> 
     * class (the DTO for <code>ApplicationEntity</code>) containing, from the
     * set of applications that user can install, only those that already have
     * installed.
     * <p>
     * The list can be empty if there is none Application matching the criteria,
     * or will be <code>null</code> if something went wrong.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param groupKey App group. Refer to <code>org.joing.common.dto.app.AppGroup</code>
     * @return List of applications matching the criteria.
     */
    List<AppGroup> getInstalledForUser( String sSessionId, AppGroupKey groupKey )
                   throws JoingServerAppException;
    
    /**
     * Mark an application as to be installed for certain user.
     * <p>
     * This only means that the application will be shown in the user's menu.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param app The <code>Application</code> instance to be installed.
     */
    boolean install( String sSessionId, AppDescriptor app )
            throws JoingServerAppException;
    
    /**
     * Mark an application as not installed for certain user.
     * <p>
     * This only means that the application will not be shown in the user's menu.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param app The <code>Application</code> instance to be uninstalled.
     */
    boolean uninstall( String sSessionId, AppDescriptor app )
            throws JoingServerAppException;
    
    /**
     * Returns the prefrerred application (if any) for requested file extension.
     * <p>
     * Note: As this intormation is not relevant and is not attached to the User,
     * Session ID is not requested by this method.
     *
     * @param sSessionId A valid session (sSessionId in order to obtain the user).
     * @param sFileExtension The file extension to be checked (without the 
     *                       initial dot '.', just the extension)
     * @return The prefrerred application or <code>null</code> if there is not a
     *         registered application for this file extension
     */
    AppDescriptor getPreferredForType( String sSessionId, String sFileExtension )
                  throws JoingServerAppException;
    
    /**
     * Returns an instance of Application to read requested application file.
     *
     * @param sSessionId The session ID assigned to client at login
     * @param nAppId The result of invokink <code>ApplicationDescriptor.getId()</code>
     * @return An instance of <code>Application</code> class that wraps an
     *         stream where the contents of the file will be dropped.
     *         Or <code>null</code> if something went wrong.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     * @see #writeText
     */
    Application getApplication( String sSessionId, int nAppId )
                throws JoingServerAppException;
}