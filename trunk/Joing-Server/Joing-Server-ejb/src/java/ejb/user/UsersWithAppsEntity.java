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
package ejb.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "USERS_WITH_APPS")
@NamedQueries({ @NamedQuery(name = "UsersWithAppsEntity.findByAccount", query = "SELECT u FROM UsersWithAppsEntity u WHERE u.usersWithAppsEntityPK.account = :account"), 
                @NamedQuery(name = "UsersWithAppsEntity.findByIdApplication", query = "SELECT u FROM UsersWithAppsEntity u WHERE u.usersWithAppsEntityPK.idApplication = :idApplication"), @NamedQuery(name = "UsersWithAppsEntity.findByAllowRemote", query = "SELECT u FROM UsersWithAppsEntity u WHERE u.allowRemote = :allowRemote") })
                
public class UsersWithAppsEntity implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected UsersWithAppsEntityPK usersWithAppsEntityPK;
    
    @Column(name = "ALLOW_REMOTE")
    private Short allowRemote = 0;

    public UsersWithAppsEntity()
    {
    }

    public UsersWithAppsEntity( UsersWithAppsEntityPK usersWithAppsEntityPK )
    {
        this.usersWithAppsEntityPK = usersWithAppsEntityPK;
    }

    public UsersWithAppsEntity( String account, int idApplication )
    {
        this.usersWithAppsEntityPK = new UsersWithAppsEntityPK( account, idApplication );
    }

    public UsersWithAppsEntityPK getUsersWithAppsEntityPK()
    {
        return usersWithAppsEntityPK;
    }

    public void setUsersWithAppsEntityPK( UsersWithAppsEntityPK usersWithAppsEntityPK )
    {
        this.usersWithAppsEntityPK = usersWithAppsEntityPK;
    }

    public Short getAllowRemote()
    {
        return allowRemote;
    }

    public void setAllowRemote( Short allowRemote )
    {
        this.allowRemote = allowRemote;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (usersWithAppsEntityPK != null ? usersWithAppsEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof UsersWithAppsEntity) )
        {
            return false;
        }
        UsersWithAppsEntity other = (UsersWithAppsEntity) object;
        if( (this.usersWithAppsEntityPK == null && other.usersWithAppsEntityPK != null) || (this.usersWithAppsEntityPK != null && !this.usersWithAppsEntityPK.equals( other.usersWithAppsEntityPK )) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "ejb.user.UsersWithAppsEntity[usersWithAppsEntityPK=" + usersWithAppsEntityPK + "]";
    }

}
