/*
 * ApplicationEntity.java
 *
 * Created on 13 de octubre de 2007, 20:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class ApplicationEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "APPLICATIONS")
@NamedQueries(
            {
        @NamedQuery(name = "ApplicationEntity.findByIdApplication", query = "SELECT a FROM ApplicationEntity a WHERE a.idApplication = :idApplication"),
        @NamedQuery(name = "ApplicationEntity.findByApplication", query = "SELECT a FROM ApplicationEntity a WHERE a.application = :application"),
        @NamedQuery(name = "ApplicationEntity.findByVersion", query = "SELECT a FROM ApplicationEntity a WHERE a.version = :version"),
        @NamedQuery(name = "ApplicationEntity.findByExtraPath", query = "SELECT a FROM ApplicationEntity a WHERE a.extraPath = :extraPath"),
        @NamedQuery(name = "ApplicationEntity.findByExecutable", query = "SELECT a FROM ApplicationEntity a WHERE a.executable = :executable"),
        @NamedQuery(name = "ApplicationEntity.findByArguments", query = "SELECT a FROM ApplicationEntity a WHERE a.arguments = :arguments"),
        @NamedQuery(name = "ApplicationEntity.findByFileTypes", query = "SELECT a FROM ApplicationEntity a WHERE a.fileTypes = :fileTypes")
    })
public class ApplicationEntity implements Serializable
{

    @Id
    @Column(name = "ID_APPLICATION", nullable = false)
    private Integer idApplication;

    @Column(name = "APPLICATION", nullable = false)
    private String application;

    @Column(name = "VERSION", nullable = false)
    private String version;

    @Column(name = "EXTRA_PATH")
    private String extraPath;

    @Column(name = "EXECUTABLE", nullable = false)
    private String executable;

    @Column(name = "ARGUMENTS")
    private String arguments;

    @Lob
    @Column(name = "ICON_PNG")
    private byte [] iconPng;

    @Lob
    @Column(name = "ICON_SVG")
    private byte [] iconSvg;

    @Column(name = "FILE_TYPES")
    private String fileTypes;
    
//    private List<AppGroupEntity> groups = new ArrayList<AppGroupEntity>();
//
//    @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
//    @JoinTable(name="APPS_WITH_GROUPS")
//    public List<AppGroupEntity> getGroups() {
//        return groups;
//    }
//
//    public void setGroups(List<AppGroupEntity> groups) {
//        this.groups = groups;
//    }
//    
    /** Creates a new instance of ApplicationEntity */
    public ApplicationEntity()
    {
    }

    /**
     * Creates a new instance of ApplicationEntity with the specified values.
     * @param idApplication the idApplication of the ApplicationEntity
     */
    public ApplicationEntity(Integer idApplication)
    {
        this.idApplication = idApplication;
    }

    /**
     * Creates a new instance of ApplicationEntity with the specified values.
     * @param idApplication the idApplication of the ApplicationEntity
     * @param application the application of the ApplicationEntity
     * @param version the version of the ApplicationEntity
     * @param executable the executable of the ApplicationEntity
     */
    public ApplicationEntity(Integer idApplication, String application, String version, String executable)
    {
        this.idApplication = idApplication;
        this.application = application;
        this.version = version;
        this.executable = executable;
    }

    /**
     * Gets the idApplication of this ApplicationEntity.
     * @return the idApplication
     */
    public Integer getIdApplication()
    {
        return this.idApplication;
    }

    /**
     * Sets the idApplication of this ApplicationEntity to the specified value.
     * @param idApplication the new idApplication
     */
    public void setIdApplication(Integer idApplication)
    {
        this.idApplication = idApplication;
    }

    /**
     * Gets the application of this ApplicationEntity.
     * @return the application
     */
    public String getApplication()
    {
        return this.application;
    }

    /**
     * Sets the application of this ApplicationEntity to the specified value.
     * @param application the new application
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * Gets the version of this ApplicationEntity.
     * @return the version
     */
    public String getVersion()
    {
        return this.version;
    }

    /**
     * Sets the version of this ApplicationEntity to the specified value.
     * @param version the new version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Gets the extraPath of this ApplicationEntity.
     * @return the extraPath
     */
    public String getExtraPath()
    {
        return this.extraPath;
    }

    /**
     * Sets the extraPath of this ApplicationEntity to the specified value.
     * @param extraPath the new extraPath
     */
    public void setExtraPath(String extraPath)
    {
        this.extraPath = extraPath;
    }

    /**
     * Gets the executable of this ApplicationEntity.
     * @return the executable
     */
    public String getExecutable()
    {
        return this.executable;
    }

    /**
     * Sets the executable of this ApplicationEntity to the specified value.
     * @param executable the new executable
     */
    public void setExecutable(String executable)
    {
        this.executable = executable;
    }

    /**
     * Gets the arguments of this ApplicationEntity.
     * @return the arguments
     */
    public String getArguments()
    {
        return this.arguments;
    }

    /**
     * Sets the arguments of this ApplicationEntity to the specified value.
     * @param arguments the new arguments
     */
    public void setArguments(String arguments)
    {
        this.arguments = arguments;
    }

    /**
     * Gets the iconPng of this ApplicationEntity.
     * @return the iconPng
     */
    public byte [] getIconPng()
    {
        return this.iconPng;
    }

    /**
     * Sets the iconPng of this ApplicationEntity to the specified value.
     * @param iconPng the new iconPng
     */
    public void setIconPng(byte [] iconPng)
    {
        this.iconPng = iconPng;
    }

    /**
     * Gets the iconSvg of this ApplicationEntity.
     * @return the iconSvg
     */
    public byte [] getIconSvg()
    {
        return this.iconSvg;
    }

    /**
     * Sets the iconSvg of this ApplicationEntity to the specified value.
     * @param iconSvg the new iconSvg
     */
    public void setIconSvg(byte [] iconSvg)
    {
        this.iconSvg = iconSvg;
    }

    /**
     * Gets the fileTypes of this ApplicationEntity.
     * @return the fileTypes
     */
    public String getFileTypes()
    {
        return this.fileTypes;
    }

    /**
     * Sets the fileTypes of this ApplicationEntity to the specified value.
     * @param fileTypes the new fileTypes
     */
    public void setFileTypes(String fileTypes)
    {
        this.fileTypes = fileTypes;
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
        hash += (this.idApplication != null ? this.idApplication.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ApplicationEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ApplicationEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationEntity)) {
            return false;
        }
        ApplicationEntity other = (ApplicationEntity)object;
        if (this.idApplication != other.idApplication && (this.idApplication == null || !this.idApplication.equals(other.idApplication))) return false;
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
        return "ejb.app.ApplicationEntity[idApplication=" + idApplication + "]";
    }
    
}
