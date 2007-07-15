/*
 * File.java
 *
 * Created on 21 de mayo de 2007, 14:50
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package ejb.vfs;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO class FileEntity
 *
 *
 * @author Francisco Morero Peyrona
 */
public class FileDescriptor implements Serializable    // TODO hacer el serializable
{
    // PK --------------------
    private int     idParent;          // hidden variable (no set and no get)
    private String  name;              // read-only   TODO: mirar cómo hacer el rename
    private boolean isDir;             // read-only
    //------------------------
    private int     idFile;            // read-only
    private int     idOriginal;        // TODO: no creo que esto vaya así: tendría que estar aquí el original y sobrarían el resto de los campos
    private String  account;           // hidden variable (no set and no get)
    private boolean isHidden;
    private boolean isPublic;
    private boolean isModifiable;
    private boolean isDeleteable;
    private boolean isExecutable;
    private boolean isDuplicable;
    private boolean isLocked;
    private boolean isSystem;          // read-only
    private boolean isAlterable;
    private boolean isInTrashCan;
    private Date    created;           // read-only
    private Date    modified;          // read-only
    private Date    accessed;          // read-only
    private String  notes;
    private long    size;              // read-only
        
    //------------------------------------------------------------------------//    
    
    /** Creates a new instance of File DTO */
    public FileDescriptor( FileEntity _file )
    {
        this.idParent      = _file.getFileEntityPK().getIdParent();
        this.name          = _file.getFileEntityPK().getName();
        this.isDir         = _file.getFileEntityPK().getIsDir() != 0;
        
        this.idFile        = _file.getIdFile();
        this.idOriginal    = _file.getIdOriginal();
        this.account       = _file.getAccount();
        
        this.isHidden      = _file.getIsHidden()     != 0;
        this.isPublic      = _file.getIsPublic()     != 0;
        this.isModifiable  = _file.getIsModifiable() != 0;
        this.isDeleteable  = _file.getIsDeleteable() != 0;
        this.isExecutable  = _file.getIsExecutable() != 0;
        this.isDuplicable  = _file.getIsDuplicable() != 0;
        this.isLocked      = _file.getIsLocked()     != 0;
        this.isSystem      = _file.getIsSystem()     != 0;
        this.isAlterable   = _file.getIsAlterable()  != 0;
        this.isInTrashCan  = _file.getIsInTrashcan() != 0;
        
        this.created       = _file.getCreated();
        this.modified      = _file.getModified();
        this.accessed      = _file.getAccessed();
        this.notes         = _file.getNotes();
        this.size          = (isDirectory() ? 0:
                                              FileSystemTools.getFileSize( this.account, this.idFile ));
    }

    void update( FileEntity _file )
    {
        // This field allows to indentify uniquely the File faster than using the PK
        _file.setIdFile( this.idFile );
        // The PK
        _file.getFileEntityPK().setIdParent( this.idParent );
        _file.getFileEntityPK().setName( getName() );
        _file.getFileEntityPK().setIsDir( (short)(isDirectory() ? 1 : 0) );
        //--------------------------------------------------------
        _file.setIdOriginal(  this.idOriginal );
        _file.setAccount(     this.account    );
        
        _file.setIsHidden(     (short)(isHidden()     ? 1 : 0) );
        _file.setIsPublic(     (short)(isPublic()     ? 1 : 0) );
        _file.setIsModifiable( (short)(isModifiable() ? 1 : 0) );
        _file.setIsDeleteable( (short)(isDeleteable() ? 1 : 0) );
        _file.setIsExecutable( (short)(isExecutable() ? 1 : 0) );
        _file.setIsDuplicable( (short)(isDuplicable() ? 1 : 0) );
        _file.setIsLocked(     (short)(isLocked()     ? 1 : 0) );
        _file.setIsSystem(     (short)(isSystem()     ? 1 : 0) );
        _file.setIsAlterable(  (short)(isAlterable()  ? 1 : 0) );
        _file.setIsInTrashcan( (short)(isInTrashCan() ? 1 : 0) );
        
        _file.setCreated(  getCreated()  );
        _file.setModified( getModified() );
        _file.setAccessed( new Date()    );
        _file.setNotes(    getNotes()    );
    }
    
    public int getId()
    {
        return this.idFile;
    }
    
    public String getName()
    {
        return name;
    }

    public boolean isDirectory()
    {
        return isDir;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public void setHidden(boolean isHidden)
    {
        if( isAlterable() )
            this.isHidden = isHidden;
    }

    public boolean isModifiable()
    {
        return isModifiable;
    }

    public void setModifiable(boolean isModifiable)
    {
        if( isAlterable() )
            this.isModifiable = isModifiable;
    }

    public boolean isDeleteable()
    {
        return isDeleteable;
    }

    public void setDeleteable(boolean isDeleteable)
    {
        if( isAlterable() )
            this.isDeleteable = isDeleteable;
    }

    public boolean isExecutable()
    {
        return isExecutable;
    }

    public void setExecutable(boolean isExecutable)
    {
        if( isAlterable() )
            this.isExecutable = isExecutable;
    }

    public boolean isDuplicable()
    {
        return isDuplicable;
    }

    public void setDuplicable(boolean isDuplicable)
    {
        if( isAlterable() )
            this.isDuplicable = isDuplicable;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic(boolean isPublic)
    {
        if( isAlterable() )
            this.isPublic = isPublic;
    }

    public boolean isAlterable()
    {
        return this.isAlterable;
    }
    
    public void setAlterable(boolean isAlterable)
    {
        if( isAlterable() )
            this.isAlterable = isAlterable;
    }
    
    public boolean isInTrashCan()
    {
        return isInTrashCan;
    }

    public void setInTrashCan(boolean isInTrashCan)
    {
        this.isInTrashCan = isInTrashCan;
    }

    public boolean isLocked()
    {
        return isLocked;
    }

    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }

    public boolean isSystem()
    {
        return isSystem;
    }
    
    public Date getCreated()
    {
        return created;
    }

    public Date getModified()
    {
        return modified;
    }

    public Date getAccessed()
    {
        return accessed;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    
    public long getSize()
    {
        return size;
    }
    
    /**
     * Returns a string representation of the object.  
     * This implementation constructs that representation based on the id 
     * fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return getClass().getName() +"[isDir="+ isDirectory() +", name="+ getName() +", idParent="+ idParent +"]";
    }
}