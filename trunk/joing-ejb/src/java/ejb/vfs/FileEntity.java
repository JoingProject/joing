/*
 * FileEntity.java
 * 
 * Created on 09-jul-2007, 21:37:10
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.vfs;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author fmorero
 */
@Entity
@Table(name = "FILES")
@NamedQueries({@NamedQuery(name = "FileEntity.findByIdFile", query = "SELECT f FROM FileEntity f WHERE f.idFile = :idFile"), @NamedQuery(name = "FileEntity.findByIdParent", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.idParent = :idParent"), @NamedQuery(name = "FileEntity.findByIdOriginal", query = "SELECT f FROM FileEntity f WHERE f.idOriginal = :idOriginal"), @NamedQuery(name = "FileEntity.findByName", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.name = :name"), @NamedQuery(name = "FileEntity.findByFullPath", query = "SELECT f FROM FileEntity f WHERE f.fullPath = :fullPath"), @NamedQuery(name = "FileEntity.findByIsDir", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.isDir = :isDir"), @NamedQuery(name = "FileEntity.findByIsHidden", query = "SELECT f FROM FileEntity f WHERE f.isHidden = :isHidden"), @NamedQuery(name = "FileEntity.findByIsPublic", query = "SELECT f FROM FileEntity f WHERE f.isPublic = :isPublic"), @NamedQuery(name = "FileEntity.findByIsModifiable", query = "SELECT f FROM FileEntity f WHERE f.isModifiable = :isModifiable"), @NamedQuery(name = "FileEntity.findByIsDeleteable", query = "SELECT f FROM FileEntity f WHERE f.isDeleteable = :isDeleteable"), @NamedQuery(name = "FileEntity.findByIsExecutable", query = "SELECT f FROM FileEntity f WHERE f.isExecutable = :isExecutable"), @NamedQuery(name = "FileEntity.findByIsDuplicable", query = "SELECT f FROM FileEntity f WHERE f.isDuplicable = :isDuplicable"), @NamedQuery(name = "FileEntity.findByIsLocked", query = "SELECT f FROM FileEntity f WHERE f.isLocked = :isLocked"), @NamedQuery(name = "FileEntity.findByIsSystem", query = "SELECT f FROM FileEntity f WHERE f.isSystem = :isSystem"), @NamedQuery(name = "FileEntity.findByIsAlterable", query = "SELECT f FROM FileEntity f WHERE f.isAlterable = :isAlterable"), @NamedQuery(name = "FileEntity.findByIsInTrashcan", query = "SELECT f FROM FileEntity f WHERE f.isInTrashcan = :isInTrashcan"), @NamedQuery(name = "FileEntity.findByCreated", query = "SELECT f FROM FileEntity f WHERE f.created = :created"), @NamedQuery(name = "FileEntity.findByModified", query = "SELECT f FROM FileEntity f WHERE f.modified = :modified"), @NamedQuery(name = "FileEntity.findByAccessed", query = "SELECT f FROM FileEntity f WHERE f.accessed = :accessed"), @NamedQuery(name = "FileEntity.findByNotes", query = "SELECT f FROM FileEntity f WHERE f.notes = :notes")})
public class FileEntity implements Serializable {
    @EmbeddedId
    protected FileEntityPK fileEntityPK;
    
    @Column(name = "ID_FILE", nullable = false)
    private int idFile;
    
    @Column(name = "ID_ORIGINAL")
    private Integer idOriginal;
    
    @Column(name = "FULL_PATH")
    private String fullPath;
    
    @Column(name = "IS_HIDDEN")
    private Short isHidden;
    
    @Column(name = "IS_PUBLIC")
    private Short isPublic;
    
    @Column(name = "IS_MODIFIABLE")
    private Short isModifiable;
    
    @Column(name = "IS_DELETEABLE")
    private Short isDeleteable;
    
    @Column(name = "IS_EXECUTABLE")
    private Short isExecutable;
    
    @Column(name = "IS_DUPLICABLE")
    private Short isDuplicable;
    
    @Column(name = "IS_LOCKED")
    private Short isLocked;
    
    @Column(name = "IS_SYSTEM")
    private Short isSystem;
    
    @Column(name = "IS_ALTERABLE")
    private Short isAlterable;
    
    @Column(name = "IS_IN_TRASHCAN")
    private Short isInTrashcan;
    
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    
    @Column(name = "ACCESSED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessed;
    
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "ACCOUNT")
    private String account;
    
    @Column(name = "OWNER")
    private String owner;

    //------------------------------------------------------------------------//
    
    public FileEntity() {
    }

    public FileEntity(FileEntityPK fileEntityPK) {
        this.fileEntityPK = fileEntityPK;
    }

    public FileEntity(FileEntityPK fileEntityPK, int idFile) {
        this.fileEntityPK = fileEntityPK;
        this.idFile = idFile;
    }

    public FileEntity(int idParent, String name, short isDir) {
        this.fileEntityPK = new FileEntityPK(idParent, name, isDir);
    }

    public FileEntityPK getFileEntityPK() {
        return fileEntityPK;
    }

    public void setFileEntityPK(FileEntityPK fileEntityPK) {
        this.fileEntityPK = fileEntityPK;
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public Integer getIdOriginal() {
        return idOriginal;
    }

    public void setIdOriginal(Integer idOriginal) {
        this.idOriginal = idOriginal;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Short getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Short isHidden) {
        this.isHidden = isHidden;
    }

    public Short getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Short isPublic) {
        this.isPublic = isPublic;
    }

    public Short getIsModifiable() {
        return isModifiable;
    }

    public void setIsModifiable(Short isModifiable) {
        this.isModifiable = isModifiable;
    }

    public Short getIsDeleteable() {
        return isDeleteable;
    }

    public void setIsDeleteable(Short isDeleteable) {
        this.isDeleteable = isDeleteable;
    }

    public Short getIsExecutable() {
        return isExecutable;
    }

    public void setIsExecutable(Short isExecutable) {
        this.isExecutable = isExecutable;
    }

    public Short getIsDuplicable() {
        return isDuplicable;
    }

    public void setIsDuplicable(Short isDuplicable) {
        this.isDuplicable = isDuplicable;
    }

    public Short getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Short isLocked) {
        this.isLocked = isLocked;
    }

    public Short getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Short isSystem) {
        this.isSystem = isSystem;
    }

    public Short getIsAlterable() {
        return isAlterable;
    }

    public void setIsAlterable(Short isAlterable) {
        this.isAlterable = isAlterable;
    }

    public Short getIsInTrashcan() {
        return isInTrashcan;
    }

    public void setIsInTrashcan(Short isInTrashcan) {
        this.isInTrashcan = isInTrashcan;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getAccessed() {
        return accessed;
    }

    public void setAccessed(Date accessed) {
        this.accessed = accessed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the account of this FileEntity.
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }

    /**
     * Sets the account of this FileEntity to the specified value.
     * @param account the new account
     */
    public void setAccount(String account)
    {
        this.account = account;
    }
    
    /**
     * Gets the owner of this FileEntity.
     * @return the owner
     */
    public String getOwner()
    {
        return this.owner;
    }

    /**
     * Sets the owner of this FileEntity to the specified value.
     * @param owner the new account
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileEntityPK != null ? fileEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
if (!(object instanceof FileEntity)) {
            return false;
        }
        FileEntity other = (FileEntity) object;
        if (this.fileEntityPK != other.fileEntityPK && (this.fileEntityPK == null || !this.fileEntityPK.equals(other.fileEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.vfs.FileEntity[fileEntityPK=" + fileEntityPK + "]";
    }

}
