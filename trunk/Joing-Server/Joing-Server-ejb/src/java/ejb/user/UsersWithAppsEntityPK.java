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
import javax.persistence.Embeddable;

/**
 *
 * @author Francisco Morero Peyrona
 */
@Embeddable
public class UsersWithAppsEntityPK implements Serializable {
    @Column(name = "ACCOUNT", nullable = false)
    private String account;
    @Column(name = "ID_APPLICATION", nullable = false)
    private int idApplication;

    public UsersWithAppsEntityPK()
    {
    }

    public UsersWithAppsEntityPK( String account, int idApplication )
    {
        this.account = account;
        this.idApplication = idApplication;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount( String account )
    {
        this.account = account;
    }

    public int getIdApplication()
    {
        return idApplication;
    }

    public void setIdApplication( int idApplication )
    {
        this.idApplication = idApplication;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (account != null ? account.hashCode() : 0);
        hash += (int) idApplication;
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof UsersWithAppsEntityPK) )
        {
            return false;
        }
        UsersWithAppsEntityPK other = (UsersWithAppsEntityPK) object;
        if( (this.account == null && other.account != null) || (this.account != null && !this.account.equals( other.account )) )
        {
            return false;
        }
        if( this.idApplication != other.idApplication )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "ejb.user.UsersWithAppsEntityPK[account=" + account + ", idApplication=" + idApplication + "]";
    }

}
