/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fmorero
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
