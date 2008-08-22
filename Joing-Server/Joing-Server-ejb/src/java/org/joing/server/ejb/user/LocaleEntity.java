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
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class LocaleEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "LOCALES")
@NamedQueries(
            {
        @NamedQuery(name = "LocaleEntity.findByIdLocale", query = "SELECT l FROM LocaleEntity l WHERE l.idLocale = :idLocale"),
        @NamedQuery(name = "LocaleEntity.findByIdiom", query = "SELECT l FROM LocaleEntity l WHERE l.idiom = :idiom"),
        @NamedQuery(name = "LocaleEntity.findByCountry", query = "SELECT l FROM LocaleEntity l WHERE l.country = :country")
    })
public class LocaleEntity implements Serializable
{

    @Id
    @Column(name = "ID_LOCALE", nullable = false)
    private Integer idLocale;

    @Column(name = "IDIOM", nullable = false)
    private String idiom;

    @Column(name = "COUNTRY")
    private String country;

    @OneToMany(mappedBy = "idLocale")
    private Collection<UserEntity> userEntityCollection;
    
    /** Creates a new instance of LocaleEntity */
    public LocaleEntity()
    {
    }

    /**
     * Creates a new instance of LocaleEntity with the specified values.
     * @param idLocale the idLocale of the LocaleEntity
     */
    public LocaleEntity(Integer idLocale)
    {
        this.idLocale = idLocale;
    }

    /**
     * Creates a new instance of LocaleEntity with the specified values.
     * @param idLocale the idLocale of the LocaleEntity
     * @param idiom the idiom of the LocaleEntity
     */
    public LocaleEntity(Integer idLocale, String idiom)
    {
        this.idLocale = idLocale;
        this.idiom = idiom;
    }

    /**
     * Gets the idLocale of this LocaleEntity.
     * @return the idLocale
     */
    public Integer getIdLocale()
    {
        return this.idLocale;
    }

    /**
     * Sets the idLocale of this LocaleEntity to the specified value.
     * @param idLocale the new idLocale
     */
    public void setIdLocale(Integer idLocale)
    {
        this.idLocale = idLocale;
    }

    /**
     * Gets the idiom of this LocaleEntity.
     * @return the idiom
     */
    public String getIdiom()
    {
        return this.idiom;
    }

    /**
     * Sets the idiom of this LocaleEntity to the specified value.
     * @param idiom the new idiom
     */
    public void setIdiom(String idiom)
    {
        this.idiom = idiom;
    }

    /**
     * Gets the country of this LocaleEntity.
     * @return the country
     */
    public String getCountry()
    {
        return this.country;
    }

    /**
     * Sets the country of this LocaleEntity to the specified value.
     * @param country the new country
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Gets the userEntityCollection of this LocaleEntity.
     * @return the userEntityCollection
     */
    public Collection<UserEntity> getUserEntityCollection()
    {
        return this.userEntityCollection;
    }

    /**
     * Sets the userEntityCollection of this LocaleEntity to the specified value.
     * @param userEntityCollection the new userEntityCollection
     */
    public void setUserEntityCollection(Collection<UserEntity> userEntityCollection)
    {
        this.userEntityCollection = userEntityCollection;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.idLocale != null ? this.idLocale.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this LocaleEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a LocaleEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LocaleEntity)) {
            return false;
        }
        LocaleEntity other = (LocaleEntity)object;
        if (this.idLocale != other.idLocale && (this.idLocale == null || !this.idLocale.equals(other.idLocale))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return "ejb.user.LocaleEntity[idLocale=" + idLocale + "]";
    }
    
}
