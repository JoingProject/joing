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

import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author Francisco Morero Peyrona
 */
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    private String  account;      // read-only
    private String  email;
    private String  firstName;
    private String  secondName;
    private boolean isMale;
    private int     idLocale;
    private String  language;     // hidden
    private String  country;      // hidden
    private long    totalSpace;   // read-only
    private long    usedSpace;    // read-only
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of User.
     * <p>
     * As instances of this class are heavy in creation, they should not be
     * created just for fun.
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    User()
    {
    }
    
    /**
     * Gets the account of this User.
     * 
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }
    
    /**
     * Gets the email of this User.
     * 
     * @return the email
     */
    public String getEmail()
    {
        return this.email;
    }
    
    /**
     * Sets the email of this User to the specified value.
     * 
     * @param email the new email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    /**
     * Gets the firstName of this User.
     * 
     * @return the firstName
     */
    public String getFirstName()
    {
        return this.firstName;
    }
    
    /**
     * Sets the firstName of this User to the specified value.
     * 
     * @param firstName the new firstName
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    /**
     * Gets the secondName of this User.
     * 
     * @return the secondName
     */
    public String getSecondName()
    {
        return this.secondName;
    }
    
    /**
     * Sets the secondName of this User to the specified value.
     * 
     * @param secondName the new secondName
     */
    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }
    
    /**
     * Gets the isMale of this User.
     * 
     * @return the isMale
     */
    public boolean isMale()
    {
        return this.isMale;
    }
    
    /**
     * Sets the isMale of this User to the specified value.
     * 
     * @param bMale true if the user is male and false if she is female
     */
    public void setMale(boolean bMale)
    {
        this.isMale = bMale;
    }
    
    /**
     * Gets the locale of this User.
     * 
     * @return the locale
     */
    public Locale getLocale()
    {
        return new Locale( this.language, this.country );
    }
    
    public int getIdLocale()
    {
        return this.idLocale;
    }
    
    /**
     * Sets the locale of this User to the specified value.
     * 
     * @param idLocale the new locale ID
     */
    public void setIdLocale( int idLocale )
    {
        this.idLocale = idLocale;
    }
    
    /**
     * Returns the quota (toltal disk space) of this User.
     *
     * @return The user quota. If there is no limit, then available disk space
     *         is returned.
     */
    public long getTotalSpace()
    {
        return ((this.totalSpace <= 0) ? Long.MAX_VALUE : this.totalSpace);
    }
    
    /**
     * Returns the used disk space for this User.
     * <p>
     * This amount is static and calculated when User instance is created.
     *
     * @return the used space
     */
    public long getUsedSpace()
    {
        return this.usedSpace;
    }
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    void setAccount( String account )
    {
        this.account = account;
    }
    
    void setTotalSpace( long totalSpace )
    {
        this.totalSpace = totalSpace;
    }
    
    void setUsedSpace( long usedSpace )
    {
        this.usedSpace = usedSpace;
    }
}