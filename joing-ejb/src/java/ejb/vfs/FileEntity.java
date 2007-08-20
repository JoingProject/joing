/*
 * FileEntity.java
 * 
 * Created on 20-ago-2007, 10:57:56
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
@NamedQueries({@NamedQuery(name = "FileEntity.findByIdFile", query = "SELECT f FROM FileEntity f WHERE f.idFile = :idFile"), @NamedQuery(name = "FileEntity.findByIdOriginal", query = "SELECT f FROM FileEntity f WHERE f.idOriginal = :idOriginal"), @NamedQuery(name = "FileEntity.findByAccount", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.account = :account"), @NamedQuery(name = "FileEntity.findByOwner", query = "SELECT f FROM FileEntity f WHERE f.owner = :owner"), @NamedQuery(name = "FileEntity.findByFilePath", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.filePath = :filePath"), @NamedQuery(name = "FileEntity.findByFileName", query = "SELECT f FROM FileEntity f WHERE f.fileEntityPK.fileName = :fileName"), @NamedQuery(name = "FileEntity.findByLockedBy", query = "SELECT f FROM FileEntity f WHERE f.lockedBy = :lockedBy"), @NamedQuery(name = "FileEntity.findByIsDir", query = "SELECT f FROM FileEntity f WHERE f.isDir = :isDir"), @NamedQuery(name = "FileEntity.findByIsHidden", query = "SELECT f FROM FileEntity f WHERE f.isHidden = :isHidden"), @NamedQuery(name = "FileEntity.findByIsPublic", query = "SELECT f FROM FileEntity f WHERE f.isPublic = :isPublic"), @NamedQuery(name = "FileEntity.findByIsReadable", query = "SELECT f FROM FileEntity f WHERE f.isReadable = :isReadable"), @NamedQuery(name = "FileEntity.findByIsModifiable", query = "SELECT f FROM FileEntity f WHERE f.isModifiable = :isModifiable"), @NamedQuery(name = "FileEntity.findByIsDeleteable", query = "SELECT f FROM FileEntity f WHERE f.isDeleteable = :isDeleteable"), @NamedQuery(name = "FileEntity.findByIsExecutable", query = "SELECT f FROM FileEntity f WHERE f.isExecutable = :isExecutable"), @NamedQuery(name = "FileEntity.findByIsDuplicable", query = "SELECT f FROM FileEntity f WHERE f.isDuplicable = :isDuplicable"), @NamedQuery(name = "FileEntity.findByIsAlterable", query = "SELECT f FROM FileEntity f WHERE f.isAlterable = :isAlterable"), @NamedQuery(name = "FileEntity.findByIsInTrashcan", query = "SELECT f FROM FileEntity f WHERE f.isInTrashcan = :isInTrashcan"), @NamedQuery(name = "FileEntity.findByCreated", query = "SELECT f FROM FileEntity f WHERE f.created = :created"), @NamedQuery(name = "FileEntity.findByModified", query = "SELECT f FROM FileEntity f WHERE f.modified = :modified"), @NamedQuery(name = "FileEntity.findByAccessed", query = "SELECT f FROM FileEntity f WHERE f.accessed = :accessed"), @NamedQuery(name = "FileEntity.findByNotes", query = "SELECT f FROM FileEntity f WHERE f.notes = :notes")})
public class FileEntity implements Serializable {
    @EmbeddedId
    protected FileEntityPK fileEntityPK;
    @Column(name = "ID_FILE", nullable = false)
    private int idFile;
    @Column(name = "ID_ORIGINAL")
    private Integer idOriginal;
    @Column(name = "OWNER", nullable = false)
    private String owner;
    @Column(name = "LOCKED_BY")
    private String lockedBy;
    @Column(name = "IS_DIR")
    private Short isDir;
    @Column(name = "IS_HIDDEN")
    private Short isHidden;
    @Column(name = "IS_PUBLIC")
    private Short isPublic;
    @Column(name = "IS_READABLE")
    private Short isReadable;
    @Column(name = "IS_MODIFIABLE")
    private Short isModifiable;
    @Column(name = "IS_DELETEABLE")
    private Short isDeleteable;
    @Column(name = "IS_EXECUTABLE")
    private Short isExecutable;
    @Column(name = "IS_DUPLICABLE")
    private Short isDuplicable;
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

    public FileEntity( )
    {
    }

    public FileEntity( FileEntityPK fileEntityPK )
    {
        this.fileEntityPK = fileEntityPK;
    }

    public FileEntity( FileEntityPK fileEntityPK, int idFile, String owner )
    {
        this.fileEntityPK = fileEntityPK;
        this.idFile = idFile;
        this.owner = owner;
    }

    public FileEntity( String account, String filePath, String fileName )
    {
        this.fileEntityPK = new FileEntityPK( account, filePath, fileName );
    }

    public FileEntityPK getFileEntityPK( )
    {
        return fileEntityPK;
    }

    protected void setFileEntityPK( FileEntityPK fileEntityPK )
    {
        this.fileEntityPK = fileEntityPK;
    }

    public int getIdFile( )
    {
        return idFile;
    }

    public void setIdFile( int idFile )
    {
        this.idFile = idFile;
    }

    public Integer getIdOriginal( )
    {
        return idOriginal;
    }

    public void setIdOriginal( Integer idOriginal )
    {
        this.idOriginal = idOriginal;
    }

    public String getOwner( )
    {
        return owner;
    }

    public void setOwner( String owner )
    {
        this.owner = owner;
    }

    public String getLockedBy( )
    {
        return lockedBy;
    }

    public void setLockedBy( String lockedBy )
    {
        this.lockedBy = lockedBy;
    }

    public Short getIsDir( )
    {
        return isDir;
    }

    public void setIsDir( Short isDir )
    {
        this.isDir = isDir;
    }

    public Short getIsHidden( )
    {
        return isHidden;
    }

    public void setIsHidden( Short isHidden )
    {
        this.isHidden = isHidden;
    }

    public Short getIsPublic( )
    {
        return isPublic;
    }

    public void setIsPublic( Short isPublic )
    {
        this.isPublic = isPublic;
    }

    public Short getIsReadable( )
    {
        return isReadable;
    }

    public void setIsReadable( Short isReadable )
    {
        this.isReadable = isReadable;
    }

    public Short getIsModifiable( )
    {
        return isModifiable;
    }

    public void setIsModifiable( Short isModifiable )
    {
        this.isModifiable = isModifiable;
    }

    public Short getIsDeleteable( )
    {
        return isDeleteable;
    }

    public void setIsDeleteable( Short isDeleteable )
    {
        this.isDeleteable = isDeleteable;
    }

    public Short getIsExecutable( )
    {
        return isExecutable;
    }

    public void setIsExecutable( Short isExecutable )
    {
        this.isExecutable = isExecutable;
    }

    public Short getIsDuplicable( )
    {
        return isDuplicable;
    }

    public void setIsDuplicable( Short isDuplicable )
    {
        this.isDuplicable = isDuplicable;
    }

    public Short getIsAlterable( )
    {
        return isAlterable;
    }

    public void setIsAlterable( Short isAlterable )
    {
        this.isAlterable = isAlterable;
    }

    public Short getIsInTrashcan( )
    {
        return isInTrashcan;
    }

    public void setIsInTrashcan( Short isInTrashcan )
    {
        this.isInTrashcan = isInTrashcan;
    }

    public Date getCreated( )
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getModified( )
    {
        return modified;
    }

    public void setModified( Date modified )
    {
        this.modified = modified;
    }

    public Date getAccessed( )
    {
        return accessed;
    }

    public void setAccessed( Date accessed )
    {
        this.accessed = accessed;
    }

    public String getNotes( )
    {
        return notes;
    }

    public void setNotes( String notes )
    {
        this.notes = notes;
    }

    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (fileEntityPK != null ? fileEntityPK.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof FileEntity) )
        {
            return false;
        }
        FileEntity other = (FileEntity) object;
        if( (this.fileEntityPK == null && other.fileEntityPK != null) || (this.fileEntityPK != null && !this.fileEntityPK.equals( other.fileEntityPK )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.vfs.FileEntity[fileEntityPK=" + fileEntityPK + "]";
    }

}
