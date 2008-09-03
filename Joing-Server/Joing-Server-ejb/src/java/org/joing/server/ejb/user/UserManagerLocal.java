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
package org.joing.server.ejb.user;

import java.util.Locale;
import javax.ejb.Local;
import org.joing.common.dto.user.User;
import org.joing.common.exception.JoingServerUserException;

/**
 * This is the business interface for UserManager enterprise bean.
 */
@Local
public interface UserManagerLocal extends UserManagerRemote
{
    /**
     * Add a new user to the community.
     * 
     * Important: Classes invoking this class must ensure that all 
     * pre-requisites are satisfied prior to the invocation:
     *<pre>
     * a) User.Account is:
     *    a.1) Not NULL
     *    a.2) Is lower case
     *    a.3) Does not exists
     *    a.4) Is at least 6 chars long
     *    a.5) Is not longer that 32 chars
     *
     * b) User.Password is:
     *    b.1) Not NULL
     *    b.2) Is at least 6 chars long
     *    b.3) Is not longer that 32 chars
     *
     * c) For both:
     *    c.1) Only letters (ASCII 65-90 and 97-122), numbers (ASCII 48-57) 
     *         and following chars "-", "_" and "." (spaces are not allowed)
     *</pre>
     * @param sAccount    Account (as specified before)
     * @param sPassword   Password (as specified before)
     * @param sEmail      User's preferred email
     * @param sFirstName  First name
     * @param sSecondName Second name
     * @param bIsMale     Is male?
     * @param locale      User's locale
     * @param nQuota      Max amout of disk space in MegaBytes (0 == no limit)
     * @return boolean <code>false</code> if user was not added. Most probably
     *                 because the Account (User ID) already exists.
     * @see isAccountAvailable( String sAccount )
     */
    User createUser( String sAccount, String sPassword, String sEmail,
                     String sFirstName, String sSecondName, 
                     boolean bIsMale, Locale locale, int nQuota )
         throws JoingServerUserException;
    
    /**
     * Delete the user and all his/her information.
     *
     * @param user The user to be deleted
     */
    void removeUser( User user )
         throws JoingServerUserException;
}