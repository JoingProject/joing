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

/**
 * DTO for LocaleEntity.
 * <p>
 * Note: there is not an update(...) method because this is a read-only class.
 *
 * @author Francisco Morero Peyrona
 */
public class Local
{   
    private int    idLocale;     // read-only
    private String sLanguage;    // hidden
    private String sCountry;     // hidden
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    Local()
    {
    }
    
    public int getId()
    {
        return this.idLocale;
    }
    
    public Locale getLocale()
    {
        return new Locale( this.sLanguage, this.sCountry );
    }
    
    //------------------------------------------------------------------------//
    
    protected void setIdLocale( int idLocale )
    {
        this.idLocale = idLocale;
    }
    
    protected void setLanguage( String sLanguage )
    {
        this.sLanguage = sLanguage;
    }
    
    protected void setCountry( String sCountry )
    {
        this.sCountry = sCountry;
    }
}