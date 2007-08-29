/*
 * UserEntity.java
 * 
 * Created on 20-ago-2007, 10:55:46
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author fmorero
 */
@Entity
@Table(name = "USERS")
@NamedQueries({@NamedQuery(name = "UserEntity.findByAccount", query = "SELECT u FROM UserEntity u WHERE u.account = :account"), @NamedQuery(name = "UserEntity.findByPassword", query = "SELECT u FROM UserEntity u WHERE u.password = :password"), @NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"), @NamedQuery(name = "UserEntity.findByFirstName", query = "SELECT u FROM UserEntity u WHERE u.firstName = :firstName"), @NamedQuery(name = "UserEntity.findBySecondName", query = "SELECT u FROM UserEntity u WHERE u.secondName = :secondName"), @NamedQuery(name = "UserEntity.findByIsMale", query = "SELECT u FROM UserEntity u WHERE u.isMale = :isMale"), @NamedQuery(name = "UserEntity.findByQuota", query = "SELECT u FROM UserEntity u WHERE u.quota = :quota")})
public class UserEntity implements Serializable {
    @Id
    @Column(name = "ACCOUNT", nullable = false)
    private String account;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "SECOND_NAME")
    private String secondName;
    @Column(name = "IS_MALE")
    private Short isMale;
    @Column(name = "QUOTA")
    private Integer quota;
    @JoinColumn(name = "ID_LOCALE", referencedColumnName = "ID_LOCALE")
    @ManyToOne
    private LocaleEntity idLocale;

    public UserEntity( )
    {
    }

    public UserEntity( String account )
    {
        this.account = account;
    }

    public UserEntity( String account, String password )
    {
        this.account = account;
        this.password = password;
    }

    public String getAccount( )
    {
        return account;
    }

    protected void setAccount( String account )
    {
        this.account = account;
    }

    public String getPassword( )
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getEmail( )
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getFirstName( )
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getSecondName( )
    {
        return secondName;
    }

    public void setSecondName( String secondName )
    {
        this.secondName = secondName;
    }

    public Short getIsMale( )
    {
        return isMale;
    }

    public void setIsMale( Short isMale )
    {
        this.isMale = isMale;
    }

    public Integer getQuota( )
    {
        return quota;
    }

    public void setQuota( Integer quota )
    {
        this.quota = quota;
    }

    public LocaleEntity getIdLocale( )
    {
        return idLocale;
    }

    public void setIdLocale( LocaleEntity idLocale )
    {
        this.idLocale = idLocale;
    }

    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (account != null ? account.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof UserEntity) )
        {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if( (this.account == null && other.account != null) || (this.account != null && !this.account.equals( other.account )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.user.UserEntity[account=" + account + "]";
    }

}
