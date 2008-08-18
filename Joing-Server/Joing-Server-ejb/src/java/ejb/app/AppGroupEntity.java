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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author antoniovl
 */
@Entity
@Table(name = "APP_GROUPS")
@NamedQueries({@NamedQuery(name = "AppGroupEntity.findByIdAppGroup", query = "SELECT a FROM AppGroupEntity a WHERE a.idAppGroup = :idAppGroup")})
public class AppGroupEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID_APP_GROUP", nullable = false)
    private Integer idAppGroup;
    
    @Lob
    @Column(name = "ICON_PNG")
    private byte[] iconPng;
    
    @Lob
    @Column(name = "ICON_SVG")
    private byte[] iconSvg;
    
//    private List<ApplicationEntity> applications =
//            new ArrayList<ApplicationEntity>();
//
//    @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
//    @JoinTable(name="APPS_WITH_GROUPS")
//    @JoinColumn(name="ID_APP_GROUPS")
//    public List<ApplicationEntity> getApplications() {
//        return applications;
//    }
//
//    public void setApplications(List<ApplicationEntity> applications) {
//        this.applications = applications;
//    }
    
    public AppGroupEntity() {
    }

    public AppGroupEntity(Integer idAppGroup) {
        this.idAppGroup = idAppGroup;
    }

    public Integer getIdAppGroup() {
        return idAppGroup;
    }

    public void setIdAppGroup(Integer idAppGroup) {
        this.idAppGroup = idAppGroup;
    }

    public byte[] getIconPng() {
        return iconPng;
    }

    public void setIconPng(byte[] iconPng) {
        this.iconPng = iconPng;
    }

    public byte[] getIconSvg() {
        return iconSvg;
    }

    public void setIconSvg(byte[] iconSvg) {
        this.iconSvg = iconSvg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAppGroup != null ? idAppGroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppGroupEntity)) {
            return false;
        }
        AppGroupEntity other = (AppGroupEntity) object;
        if ((this.idAppGroup == null && other.idAppGroup != null) || (this.idAppGroup != null && !this.idAppGroup.equals(other.idAppGroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.app.AppGroupEntity[idAppGroup=" + idAppGroup + "]";
    }
}