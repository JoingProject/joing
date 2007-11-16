/*
 * Local.java
 *
 * Created on 14 de junio de 2007, 12:37
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
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

package ejb.user;

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