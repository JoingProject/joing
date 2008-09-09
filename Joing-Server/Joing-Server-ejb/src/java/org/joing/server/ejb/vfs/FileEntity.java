/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.server.ejb.vfs;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class FileEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Entity
@Table(name = "FILES")
@NamedQueries(
    {
        @NamedQuery(name  = "FileEntity.findByPathAndName",
                    query = "SELECT f FROM FileEntity f"+
                            " WHERE f.account  = :account"+
                            "   AND f.filePath = :path"+
                            "   AND f.fileName = :name"),
        
        @NamedQuery(name  = "FileEntity.findByPath",
                    query = "SELECT f FROM FileEntity f"+
                            " WHERE f.account  = :account"+
                            "   AND f.filePath = :path" ),
        
        @NamedQuery(name  = "FileEntity.findInTrashcan",
                    query = "SELECT f FROM FileEntity f"+
                            " WHERE f.account = :account"+
                            "   AND f.isInTrashcan = 1" )
    } )
    
public class FileEntity implements Serializable
{
    // Note: Can't use GenerationType.AUTO with TopLink and Derby because 
    //       TopLink tries SEQUENCE, and Derby does not support it.     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FILE")
    private int idFile;

    @Column(name = "ID_ORIGINAL")
    private Integer idOriginal;
    
    @Column(name = "ACCOUNT", nullable = false)
    private String account;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    @Column(name = "FILE_NAME")
    private String fileName;

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
    
    /** Creates a new instance of FileEntity */
    public FileEntity()
    {
    }

    /**
     * Gets the idFile of this FileEntity.
     * @return the idFile
     */
    public int getIdFile()
    {
        return this.idFile;
    }
    
    /**
     * Sets the idFile of this FileEntity to the specified value.
     * @param idFile the new idFile
     */
    public void setIdFile(int idFile)
    {
        this.idFile = idFile;
    }

    /**
     * Gets the idOriginal of this FileEntity.
     * @return the idOriginal
     */
    public Integer getIdOriginal()
    {
        return this.idOriginal;
    }

    /**
     * Sets the idOriginal of this FileEntity to the specified value.
     * @param idOriginal the new idOriginal
     */
    public void setIdOriginal(Integer idOriginal)
    {
        this.idOriginal = idOriginal;
    }

    /**
     * Gets the account of this FileEntityPK.
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }

    /**
     * Sets the account of this FileEntityPK to the specified value.
     * @param account the new account
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    /**
     * Gets the filePath of this FileEntityPK.
     * @return the filePath
     */
    public String getFilePath()
    {
        return this.filePath;
    }

    /**
     * Sets the filePath of this FileEntityPK to the specified value.
     * @param filePath the new filePath
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * Gets the fileName of this FileEntityPK.
     * @return the fileName
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * Sets the fileName of this FileEntityPK to the specified value.
     * @param fileName the new fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
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
     * @param owner the new owner
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * Gets the lockedBy of this FileEntity.
     * @return the lockedBy
     */
    public String getLockedBy()
    {
        return this.lockedBy;
    }

    /**
     * Sets the lockedBy of this FileEntity to the specified value.
     * @param lockedBy the new lockedBy
     */
    public void setLockedBy(String lockedBy)
    {
        this.lockedBy = lockedBy;
    }

    /**
     * Gets the isDir of this FileEntity.
     * @return the isDir
     */
    public Short getIsDir()
    {
        return this.isDir;
    }

    /**
     * Sets the isDir of this FileEntity to the specified value.
     * @param isDir the new isDir
     */
    public void setIsDir(Short isDir)
    {
        this.isDir = isDir;
    }

    /**
     * Gets the isHidden of this FileEntity.
     * @return the isHidden
     */
    public Short getIsHidden()
    {
        return this.isHidden;
    }

    /**
     * Sets the isHidden of this FileEntity to the specified value.
     * @param isHidden the new isHidden
     */
    public void setIsHidden(Short isHidden)
    {
        this.isHidden = isHidden;
    }

    /**
     * Gets the isPublic of this FileEntity.
     * @return the isPublic
     */
    public Short getIsPublic()
    {
        return this.isPublic;
    }

    /**
     * Sets the isPublic of this FileEntity to the specified value.
     * @param isPublic the new isPublic
     */
    public void setIsPublic(Short isPublic)
    {
        this.isPublic = isPublic;
    }

    /**
     * Gets the isReadable of this FileEntity.
     * @return the isReadable
     */
    public Short getIsReadable()
    {
        return this.isReadable;
    }
    
    /**
     * Sets the isReadable of this FileEntity to the specified value.
     * @param isReadable the new isReadable
     */
    public void setIsReadable(Short isReadable)
    {
        this.isReadable = isReadable;
    }

    /**
     * Gets the isModifiable of this FileEntity.
     * @return the isModifiable
     */
    public Short getIsModifiable()
    {
        return this.isModifiable;
    }

    /**
     * Sets the isModifiable of this FileEntity to the specified value.
     * @param isModifiable the new isModifiable
     */
    public void setIsModifiable(Short isModifiable)
    {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets the isDeleteable of this FileEntity.
     * @return the isDeleteable
     */
    public Short getIsDeleteable()
    {
        return this.isDeleteable;
    }

    /**
     * Sets the isDeleteable of this FileEntity to the specified value.
     * @param isDeleteable the new isDeleteable
     */
    public void setIsDeleteable(Short isDeleteable)
    {
        this.isDeleteable = isDeleteable;
    }

    /**
     * Gets the isExecutable of this FileEntity.
     * @return the isExecutable
     */
    public Short getIsExecutable()
    {
        return this.isExecutable;
    }

    /**
     * Sets the isExecutable of this FileEntity to the specified value.
     * @param isExecutable the new isExecutable
     */
    public void setIsExecutable(Short isExecutable)
    {
        this.isExecutable = isExecutable;
    }

    /**
     * Gets the isDuplicable of this FileEntity.
     * @return the isDuplicable
     */
    public Short getIsDuplicable()
    {
        return this.isDuplicable;
    }

    /**
     * Sets the isDuplicable of this FileEntity to the specified value.
     * @param isDuplicable the new isDuplicable
     */
    public void setIsDuplicable(Short isDuplicable)
    {
        this.isDuplicable = isDuplicable;
    }

    /**
     * Gets the isAlterable of this FileEntity.
     * @return the isAlterable
     */
    public Short getIsAlterable()
    {
        return this.isAlterable;
    }

    /**
     * Sets the isAlterable of this FileEntity to the specified value.
     * @param isAlterable the new isAlterable
     */
    public void setIsAlterable(Short isAlterable)
    {
        this.isAlterable = isAlterable;
    }

    /**
     * Gets the isInTrashcan of this FileEntity.
     * @return the isInTrashcan
     */
    public Short getIsInTrashcan()
    {
        return this.isInTrashcan;
    }

    /**
     * Sets the isInTrashcan of this FileEntity to the specified value.
     * @param isInTrashcan the new isInTrashcan
     */
    public void setIsInTrashcan(Short isInTrashcan)
    {
        this.isInTrashcan = isInTrashcan;
    }

    /**
     * Gets the created of this FileEntity.
     * @return the created
     */
    public Date getCreated()
    {
        return this.created;
    }

    /**
     * Sets the created of this FileEntity to the specified value.
     * @param created the new created
     */
    public void setCreated(Date created)
    {
        this.created = created;
    }

    /**
     * Gets the modified of this FileEntity.
     * @return the modified
     */
    public Date getModified()
    {
        return this.modified;
    }

    /**
     * Sets the modified of this FileEntity to the specified value.
     * @param modified the new modified
     */
    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    /**
     * Gets the accessed of this FileEntity.
     * @return the accessed
     */
    public Date getAccessed()
    {
        return this.accessed;
    }

    /**
     * Sets the accessed of this FileEntity to the specified value.
     * @param accessed the new accessed
     */
    public void setAccessed(Date accessed)
    {
        this.accessed = accessed;
    }

    /**
     * Gets the notes of this FileEntity.
     * @return the notes
     */
    public String getNotes()
    {
        return this.notes;
    }

    /**
     * Sets the notes of this FileEntity to the specified value.
     * @param notes the new notes
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        int hash = idFile;
        return hash;
    }

    /**
     * Determines whether another object is equal to this FileEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a FileEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof FileEntity) )
            return false;
        
        FileEntity other = (FileEntity)object;
        
        return idFile == other.getIdFile();
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        String sFullName = "";
        
        if( filePath == null )
        {
            sFullName = fileName;
        }
        else
        {
            if( filePath.endsWith( "/" ) && ! fileName.startsWith( "/") )
                sFullName = "/";
            
            sFullName += fileName;
        }
        
        return "ejb.vfs.FileEntity[name="+ sFullName +", account="+ account +"]";
    }   
}