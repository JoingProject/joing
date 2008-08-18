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
package ejb.session;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "SESSIONS")
@NamedQueries({@NamedQuery(name = "SessionEntity.findByIdSession", query = "SELECT s FROM SessionEntity s WHERE s.idSession = :idSession"), @NamedQuery(name = "SessionEntity.findByCreated", query = "SELECT s FROM SessionEntity s WHERE s.created = :created"), @NamedQuery(name = "SessionEntity.findByAccessed", query = "SELECT s FROM SessionEntity s WHERE s.accessed = :accessed")})
public class SessionEntity implements Serializable {
    @Id
    @Column(name = "ID_SESSION", nullable = false)
    private String idSession;
    
    @Column(name = "ACCOUNT")
    private String account;
    
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "ACCESSED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessed;

    public SessionEntity( )
    {
    }

    public SessionEntity( String idSession )
    {
        this.idSession = idSession;
    }

    public String getIdSession( )
    {
        return idSession;
    }

    protected void setIdSession( String idSession )
    {
        this.idSession = idSession;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount( String account )
    {
        this.account = account;
    }

    public Date getCreated( )
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getAccessed( )
    {
        return accessed;
    }

    public void setAccessed( Date accessed )
    {
        this.accessed = accessed;
    }

    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (idSession != null ? idSession.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof SessionEntity) )
        {
            return false;
        }
        SessionEntity other = (SessionEntity) object;
        if( (this.idSession == null && other.idSession != null) || (this.idSession != null && !this.idSession.equals( other.idSession )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.session.SessionEntity[idSession=" + idSession + "]";
    }

}
