/*
 * User.java
 *
 * Created on 17 de mayo de 2007, 20:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.user;

import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author fmorero
 */
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private String  account;      // read-only
    private String  email;
    private String  firstName;
    private String  secondName;
    private boolean isMale;
    private int     idLocale;
    private String  language;     // hidden
    private String  country;      // hidden
    private long    totalSpace;   // read-only
    private long    usedSpace;    // read-only
    
    //------------------------------------------------------------------------//
    
    /**
     * Creates a new instance of User.
     * <p>
     * As instances of this class are heavy in creation, they should not be
     * created just for fun.
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    User( UserEntity _user )
    {
        LocaleEntity _locale = _user.getIdLocale();
        
        this.account    = _user.getAccount();
        this.email      = _user.getEmail();
        this.firstName  = _user.getFirstName();
        this.secondName = _user.getSecondName();
        this.isMale     = _user.getIsMale() != 0;
        this.idLocale   = _locale.getIdLocale();
        this.language   = _locale.getLanguage();
        this.country    = _locale.getCountry();
        this.totalSpace = (long) _user.getQuota();
    }
    
    /**
     * Updates passed instace with the values stored in this instance
     * 
     * @para _user UserEntity instance to be updated
     */
    void update( UserEntity _user )
    {
        LocaleEntity _locale = new LocaleEntity();
                     _locale.setIdLocale( this.idLocale );
                     _locale.setLanguage( this.language );
                     _locale.setCountry( this.country );
                     
        _user.setAccount( getAccount() );   // Allows to indentify uniquely the user (PK)
        _user.setEmail( getEmail() );
        _user.setFirstName( getFirstName() );
        _user.setSecondName( getSecondName() );
        _user.setIsMale( (short)(isMale() ? 1 : 0) );
        _user.setIdLocale( _locale );
    }
    
    /**
     * Gets the account of this User.
     * 
     * 
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }
    
    /**
     * Gets the email of this User.
     * 
     * 
     * 
     * 
     * 
     * 
     * @return the email
     */
    public String getEmail()
    {
        return this.email;
    }
    
    /**
     * Sets the email of this User to the specified value.
     * 
     * 
     * 
     * 
     * 
     * 
     * @param email the new email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    /**
     * Gets the firstName of this User.
     * 
     * 
     * 
     * 
     * 
     * 
     * @return the firstName
     */
    public String getFirstName()
    {
        return this.firstName;
    }
    
    /**
     * Sets the firstName of this User to the specified value.
     * 
     * 
     * 
     * 
     * 
     * 
     * @param firstName the new firstName
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    /**
     * Gets the secondName of this User.
     * 
     * 
     * 
     * 
     * 
     * 
     * @return the secondName
     */
    public String getSecondName()
    {
        return this.secondName;
    }
    
    /**
     * Sets the secondName of this User to the specified value.
     * 
     * 
     * 
     * 
     * 
     * 
     * @param secondName the new secondName
     */
    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }
    
    /**
     * Gets the isMale of this User.
     * 
     * 
     * 
     * 
     * 
     * 
     * @return the isMale
     */
    public boolean isMale()
    {
        return this.isMale;
    }
    
    /**
     * Sets the isMale of this User to the specified value.
     * 
     * @param bMale true if the user is male and false if she is female
     */
    public void setMale(boolean bMale)
    {
        this.isMale = bMale;
    }
    
    /**
     * Gets the locale of this User.
     * 
     * 
     * 
     * 
     * 
     * 
     * @return the locale
     */
    public Locale getLocale()
    {
        return new Locale( this.language, this.country );
    }
    
    public int getIdLocale()
    {
        return this.idLocale;
    }
    
    /**
     * Sets the locale of this User to the specified value.
     * 
     * @param idLocale the new locale ID
     */
    public void setIdLocale( int idLocale )
    {
        this.idLocale = idLocale;
    }
    
    /**
     * Returns the quota (toltal disk space) of this User.
     *
     * @return The user quota. If there is no limit, then available disk space
     *         is returned.
     */
    public long getTotalSpace()
    {
        return ((this.totalSpace == 0) ? Long.MAX_VALUE : this.totalSpace);
    }
    
    /**
     * Returns the used disk space for this User.
     * <p>
     * This amount is static and calculated when User instance is created.
     *
     * @return the used space
     */
    public long getUsedSpace()
    {
        return this.usedSpace;
    }
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    // This information is injected because I don't want User.class to travel
    // with a reference to class FileSystemTools
    
    void setUsedSpace( long usedSpace )
    {
        this.usedSpace = usedSpace;
    }
}