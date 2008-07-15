/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author fmorero
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
