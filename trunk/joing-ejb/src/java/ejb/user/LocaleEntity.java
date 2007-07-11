/*
 * LocaleEntity.java
 * 
 * Created on 09-jul-2007, 21:33:00
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.user;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author fmorero
 */
@Entity
@Table(name = "LOCALES")
@NamedQueries({@NamedQuery(name = "LocaleEntity.findByIdLocale", query = "SELECT l FROM LocaleEntity l WHERE l.idLocale = :idLocale"), @NamedQuery(name = "LocaleEntity.findByLanguage", query = "SELECT l FROM LocaleEntity l WHERE l.language = :language"), @NamedQuery(name = "LocaleEntity.findByCountry", query = "SELECT l FROM LocaleEntity l WHERE l.country = :country")})
public class LocaleEntity implements Serializable {
    @Id
    @Column(name = "ID_LOCALE", nullable = false)
    private Integer idLocale;
    @Column(name = "LANGUAGE", nullable = false)
    private String language;
    @Column(name = "COUNTRY")
    private String country;
    @OneToMany(mappedBy = "idLocale")
    private Collection<UserEntity> userEntityCollection;

    public LocaleEntity() {
    }

    public LocaleEntity(Integer idLocale) {
        this.idLocale = idLocale;
    }

    public LocaleEntity(Integer idLocale, String language) {
        this.idLocale = idLocale;
        this.language = language;
    }

    public Integer getIdLocale() {
        return idLocale;
    }

    public void setIdLocale(Integer idLocale) {
        this.idLocale = idLocale;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Collection<UserEntity> getUserEntityCollection() {
        return userEntityCollection;
    }

    public void setUserEntityCollection(Collection<UserEntity> userEntityCollection) {
        this.userEntityCollection = userEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocale != null ? idLocale.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
if (!(object instanceof LocaleEntity)) {
            return false;
        }
        LocaleEntity other = (LocaleEntity) object;
        if (this.idLocale != other.idLocale && (this.idLocale == null || !this.idLocale.equals(other.idLocale))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.user.LocaleEntity[idLocale=" + idLocale + "]";
    }

}
