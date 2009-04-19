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
package org.joing.server.ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class ApplicationEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "APPLICATIONS")
@NamedQueries(
            {
        @NamedQuery(name = "ApplicationEntity.findByIdApplication", query = "SELECT a FROM ApplicationEntity a WHERE a.idApplication = :idApplication"),        
        @NamedQuery(name = "ApplicationEntity.findByExtraPath", query = "SELECT a FROM ApplicationEntity a WHERE a.extraPath = :extraPath"),
        @NamedQuery(name = "ApplicationEntity.findByExecutable", query = "SELECT a FROM ApplicationEntity a WHERE a.executable = :executable")
    })
public class ApplicationEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID_APPLICATION", nullable = false)
    private Integer idApplication;

    @Column(name = "EXTRA_PATH")
    private String extraPath;

    @Column(name = "EXECUTABLE", nullable = false)
    private String executable;

    
//    private List<AppGroupEntity> groups = new ArrayList<AppGroupEntity>();
//
//    @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
//    @JoinTable(name="APPS_WITH_GROUPS")
//    public List<AppGroupEntity> getGroups() {
//        return groups;
//    }
//
//    public void setGroups(List<AppGroupEntity> groups) {
//        this.groups = groups;
//    }
//    
    /** Creates a new instance of ApplicationEntity */
    public ApplicationEntity()
    {
    }

    /**
     * Creates a new instance of ApplicationEntity with the specified values.
     * @param idApplication the idApplication of the ApplicationEntity
     */
    public ApplicationEntity(Integer idApplication)
    {
        this.idApplication = idApplication;
    }

    /**
     * Creates a new instance of ApplicationEntity with the specified values.
     * @param idApplication the idApplication of the ApplicationEntity
     * @param application the application of the ApplicationEntity
     * @param version the version of the ApplicationEntity
     * @param executable the executable of the ApplicationEntity
     */
    public ApplicationEntity(Integer idApplication, String executable)
    {
        this.idApplication = idApplication;
        this.executable = executable;
    }

    /**
     * Gets the idApplication of this ApplicationEntity.
     * @return the idApplication
     */
    public Integer getIdApplication()
    {
        return this.idApplication;
    }

    /**
     * Sets the idApplication of this ApplicationEntity to the specified value.
     * @param idApplication the new idApplication
     */
    public void setIdApplication(Integer idApplication)
    {
        this.idApplication = idApplication;
    }

    /**
     * Gets the extraPath of this ApplicationEntity.
     * @return the extraPath
     */
    public String getExtraPath()
    {
        return this.extraPath;
    }

    /**
     * Sets the extraPath of this ApplicationEntity to the specified value.
     * @param extraPath the new extraPath
     */
    public void setExtraPath(String extraPath)
    {
        this.extraPath = extraPath;
    }

    /**
     * Gets the executable of this ApplicationEntity.
     * @return the executable
     */
    public String getExecutable()
    {
        return this.executable;
    }

    /**
     * Sets the executable of this ApplicationEntity to the specified value.
     * @param executable the new executable
     */
    public void setExecutable(String executable)
    {
        this.executable = executable;
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
        hash += (this.idApplication != null ? this.idApplication.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ApplicationEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ApplicationEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationEntity)) {
            return false;
        }
        ApplicationEntity other = (ApplicationEntity)object;
        if (this.idApplication != other.idApplication && (this.idApplication == null || !this.idApplication.equals(other.idApplication))) return false;
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
        return "ejb.app.ApplicationEntity[idApplication=" + idApplication + "]";
    }
}