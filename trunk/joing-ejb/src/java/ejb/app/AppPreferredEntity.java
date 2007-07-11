/*
 * AppPreferredEntity.java
 *
 * Created on 7 de junio de 2007, 11:26
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

package ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class AppPreferredEntity
 * 
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "APP_PREFERRED")
@NamedQueries(
            {
        @NamedQuery(name = "AppPreferredEntity.findByIdApplication", query = "SELECT a FROM AppPreferredEntity a WHERE a.idApplication = :idApplication"),
        @NamedQuery(name = "AppPreferredEntity.findByFileExtension", query = "SELECT a FROM AppPreferredEntity a WHERE a.fileExtension = :fileExtension")
    })
public class AppPreferredEntity implements Serializable
{

    @Column(name = "ID_APPLICATION", nullable = false)
    private int idApplication;

    @Id
    @Column(name = "FILE_EXTENSION", nullable = false)
    private String fileExtension;
    
    /** Creates a new instance of AppPreferredEntity */
    public AppPreferredEntity()
    {
    }

    /**
     * Creates a new instance of AppPreferredEntity with the specified values.
     * @param fileExtension the fileExtension of the AppPreferredEntity
     */
    public AppPreferredEntity(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    /**
     * Creates a new instance of AppPreferredEntity with the specified values.
     * @param fileExtension the fileExtension of the AppPreferredEntity
     * @param idApplication the idApplication of the AppPreferredEntity
     */
    public AppPreferredEntity(String fileExtension, int idApplication)
    {
        this.fileExtension = fileExtension;
        this.idApplication = idApplication;
    }

    /**
     * Gets the idApplication of this AppPreferredEntity.
     * @return the idApplication
     */
    public int getIdApplication()
    {
        return this.idApplication;
    }

    /**
     * Sets the idApplication of this AppPreferredEntity to the specified value.
     * @param idApplication the new idApplication
     */
    public void setIdApplication(int idApplication)
    {
        this.idApplication = idApplication;
    }

    /**
     * Gets the fileExtension of this AppPreferredEntity.
     * @return the fileExtension
     */
    public String getFileExtension()
    {
        return this.fileExtension;
    }

    /**
     * Sets the fileExtension of this AppPreferredEntity to the specified value.
     * @param fileExtension the new fileExtension
     */
    public void setFileExtension(String fileExtension)
    {
        this.fileExtension = fileExtension;
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
        hash += (this.fileExtension != null ? this.fileExtension.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this AppPreferredEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a AppPreferredEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppPreferredEntity)) {
            return false;
        }
        AppPreferredEntity other = (AppPreferredEntity)object;
        if (this.fileExtension != other.fileExtension && (this.fileExtension == null || !this.fileExtension.equals(other.fileExtension))) return false;
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
        return "ejb.app.AppPreferredEntity[fileExtension=" + fileExtension + "]";
    }
    
}
