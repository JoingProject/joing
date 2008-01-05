/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
