/*
 * UserEntity.java
 *
 * Created on 12 de octubre de 2007, 20:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.user;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class UserEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "USERS")
@NamedQueries(
            {
        @NamedQuery(name = "UserEntity.findByAccount", query = "SELECT u FROM UserEntity u WHERE u.account = :account"),
        @NamedQuery(name = "UserEntity.findByPassword", query = "SELECT u FROM UserEntity u WHERE u.password = :password"),
        @NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
        @NamedQuery(name = "UserEntity.findByFirstName", query = "SELECT u FROM UserEntity u WHERE u.firstName = :firstName"),
        @NamedQuery(name = "UserEntity.findBySecondName", query = "SELECT u FROM UserEntity u WHERE u.secondName = :secondName"),
        @NamedQuery(name = "UserEntity.findByIsMale", query = "SELECT u FROM UserEntity u WHERE u.isMale = :isMale"),
        @NamedQuery(name = "UserEntity.findByQuota", query = "SELECT u FROM UserEntity u WHERE u.quota = :quota")
    })
public class UserEntity implements Serializable
{

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

    
    /** Creates a new instance of UserEntity */
    public UserEntity()
    {
    }

    /**
     * Creates a new instance of UserEntity with the specified values.
     * @param account the account of the UserEntity
     */
    public UserEntity(String account)
    {
        this.account = account;
    }

    /**
     * Creates a new instance of UserEntity with the specified values.
     * @param account the account of the UserEntity
     * @param password the password of the UserEntity
     */
    public UserEntity(String account, String password)
    {
        this.account = account;
        this.password = password;
    }

    /**
     * Gets the account of this UserEntity.
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }

    /**
     * Sets the account of this UserEntity to the specified value.
     * @param account the new account
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    /**
     * Gets the password of this UserEntity.
     * @return the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Sets the password of this UserEntity to the specified value.
     * @param password the new password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets the email of this UserEntity.
     * @return the email
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * Sets the email of this UserEntity to the specified value.
     * @param email the new email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Gets the firstName of this UserEntity.
     * @return the firstName
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * Sets the firstName of this UserEntity to the specified value.
     * @param firstName the new firstName
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the secondName of this UserEntity.
     * @return the secondName
     */
    public String getSecondName()
    {
        return this.secondName;
    }

    /**
     * Sets the secondName of this UserEntity to the specified value.
     * @param secondName the new secondName
     */
    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    /**
     * Gets the isMale of this UserEntity.
     * @return the isMale
     */
    public Short getIsMale()
    {
        return this.isMale;
    }

    /**
     * Sets the isMale of this UserEntity to the specified value.
     * @param isMale the new isMale
     */
    public void setIsMale(Short isMale)
    {
        this.isMale = isMale;
    }

    /**
     * Gets the quota of this UserEntity.
     * @return the quota
     */
    public Integer getQuota()
    {
        return this.quota;
    }

    /**
     * Sets the quota of this UserEntity to the specified value.
     * @param quota the new quota
     */
    public void setQuota(Integer quota)
    {
        this.quota = quota;
    }

    /**
     * Gets the idLocale of this UserEntity.
     * @return the idLocale
     */
    public LocaleEntity getIdLocale()
    {
        return this.idLocale;
    }

    /**
     * Sets the idLocale of this UserEntity to the specified value.
     * @param idLocale the new idLocale
     */
    public void setIdLocale(LocaleEntity idLocale)
    {
        this.idLocale = idLocale;
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
        hash += (this.account != null ? this.account.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this UserEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a UserEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity)object;
        if (this.account != other.account && (this.account == null || !this.account.equals(other.account))) return false;
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
        return "ejb.user.UserEntity[account=" + account + "]";
    }
    
}
