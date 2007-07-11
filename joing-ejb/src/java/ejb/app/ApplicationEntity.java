/*
 * ApplicationEntity.java
 * 
 * Created on 09-jul-2007, 21:23:11
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author fmorero
 */
@Entity
@Table(name = "APPLICATIONS")
@NamedQueries({@NamedQuery(name = "ApplicationEntity.findByIdApplication", query = "SELECT a FROM ApplicationEntity a WHERE a.idApplication = :idApplication"), @NamedQuery(name = "ApplicationEntity.findByName", query = "SELECT a FROM ApplicationEntity a WHERE a.applicationEntityPK.name = :name"), @NamedQuery(name = "ApplicationEntity.findByVersion", query = "SELECT a FROM ApplicationEntity a WHERE a.applicationEntityPK.version = :version"), @NamedQuery(name = "ApplicationEntity.findByExtraPath", query = "SELECT a FROM ApplicationEntity a WHERE a.extraPath = :extraPath"), @NamedQuery(name = "ApplicationEntity.findByExecutable", query = "SELECT a FROM ApplicationEntity a WHERE a.executable = :executable"), @NamedQuery(name = "ApplicationEntity.findByFileTypes", query = "SELECT a FROM ApplicationEntity a WHERE a.fileTypes = :fileTypes")})
public class ApplicationEntity implements Serializable {
    @EmbeddedId
    protected ApplicationEntityPK applicationEntityPK;
    @Column(name = "ID_APPLICATION", nullable = false)
    private int idApplication;
    @Column(name = "EXTRA_PATH", nullable = false)
    private String extraPath;
    @Column(name = "EXECUTABLE", nullable = false)
    private String executable;
    @Lob
    @Column(name = "ICON_PNG")
    private byte[] iconPng;
    @Lob
    @Column(name = "ICON_SVG")
    private byte[] iconSvg;
    @Column(name = "FILE_TYPES")
    private String fileTypes;

    public ApplicationEntity() {
    }

    public ApplicationEntity(ApplicationEntityPK applicationEntityPK) {
        this.applicationEntityPK = applicationEntityPK;
    }

    public ApplicationEntity(ApplicationEntityPK applicationEntityPK, int idApplication, String extraPath, String executable) {
        this.applicationEntityPK = applicationEntityPK;
        this.idApplication = idApplication;
        this.extraPath = extraPath;
        this.executable = executable;
    }

    public ApplicationEntity(String name, String version) {
        this.applicationEntityPK = new ApplicationEntityPK(name, version);
    }

    public ApplicationEntityPK getApplicationEntityPK() {
        return applicationEntityPK;
    }

    public void setApplicationEntityPK(ApplicationEntityPK applicationEntityPK) {
        this.applicationEntityPK = applicationEntityPK;
    }

    public int getIdApplication() {
        return idApplication;
    }

    public void setIdApplication(int idApplication) {
        this.idApplication = idApplication;
    }

    public String getExtraPath() {
        return extraPath;
    }

    public void setExtraPath(String extraPath) {
        this.extraPath = extraPath;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
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

    public String getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applicationEntityPK != null ? applicationEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
if (!(object instanceof ApplicationEntity)) {
            return false;
        }
        ApplicationEntity other = (ApplicationEntity) object;
        if (this.applicationEntityPK != other.applicationEntityPK && (this.applicationEntityPK == null || !this.applicationEntityPK.equals(other.applicationEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.app.ApplicationEntity[applicationEntityPK=" + applicationEntityPK + "]";
    }

}
