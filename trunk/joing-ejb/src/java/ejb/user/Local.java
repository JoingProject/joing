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
    
    public Local( LocaleEntity _local )
    {
        this.idLocale  = _local.getIdLocale();
        this.sLanguage = _local.getLanguage();
        this.sCountry  = _local.getCountry();
    }
    
    public int getId()
    {
        return this.idLocale;
    }
    
    public Locale getLocale()
    {
        return new Locale( this.sLanguage, this.sCountry );
    }
}