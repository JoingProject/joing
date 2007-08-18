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
 * DTO class for FileDescriptor.
 * <p>
 * This class implements the logic to handle (validate) file attributes). In 
 * this way, there is no need to send them to the Server in order to be 
 * validated (this class is used by the Client).
 *
 * @author Francisco Morero Peyrona
 */
public class FileDescriptor implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    // PK --------------------
    private int     idParent;          // read-only
    private String  name;              // read-only
    private boolean isDir;             // read-only
    //------------------------
    private int     idFile;            // read-only
    private int     idOriginal;        // TODO: no creo que esto vaya así: tendría que estar aquí el original y sobrarían el resto de los campos
    private String  absolutePath;      // read-only
    private String  account;           // hidden
    private String  owner;             // read-only
    private String  lockedBy;
    
    private boolean isHidden;
    private boolean isPublic;
    private boolean isReadable;
    private boolean isModifiable;
    private boolean isDeleteable;
    private boolean isExecutable;
    private boolean isDuplicable;
    private boolean isSystem;          // read-only
    private boolean isAlterable;
    private boolean isInTrashcan;
    private Date    created;           // read-only
    private Date    modified;          // read-only
    private Date    accessed;          // read-only
    private String  notes;
    private long    size;              // read-only
    
    //---------------------------------------------
    
    private final static int ALTERABLE   =  1;
    private final static int DELETEABLE  =  2;
    private final static int DUPLICABLE  =  3;
    private final static int EXECUTABLE  =  4;
    private final static int HIDDEN      =  5;
    private final static int MODIFIABLE  =  6;
    private final static int PUBLIC      =  7;
    private final static int READABLE    =  8;
    private final static int LOCKED      =  9;
    private final static int IN_TRASHCAN = 10;
    
    private String  sFailedToChangeAttributeReason;
            
    //------------------------------------------------------------------------//    
    
    /** 
     * Class constructor (this class is a DTO).
     * 
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    FileDescriptor( FileEntity _file )
    {
        this.idParent      = _file.getFileEntityPK().getIdParent();
        this.name          = _file.getFileEntityPK().getName();
        this.isDir         = _file.getFileEntityPK().getIsDir() != 0;
        
        this.idFile        = _file.getIdFile();
        this.idOriginal    = _file.getIdOriginal();
        this.absolutePath  = _file.getFullPath();
        this.account       = _file.getAccount();
        this.owner         = _file.getOwner();
        this.lockedBy      = _file.getLockedBy();
        
        this.isHidden      = _file.getIsHidden()     != 0;
        this.isPublic      = _file.getIsPublic()     != 0;
        this.isReadable    = _file.getIsReadable()   != 0;
        this.isModifiable  = _file.getIsModifiable() != 0;
        this.isDeleteable  = _file.getIsDeleteable() != 0;
        this.isExecutable  = _file.getIsExecutable() != 0;
        this.isDuplicable  = _file.getIsDuplicable() != 0;
        this.isSystem      = _file.getIsSystem()     != 0;
        this.isAlterable   = _file.getIsAlterable()  != 0;
        this.isInTrashcan  = _file.getIsInTrashcan() != 0;
        
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
        _file.setIdFile( getId() );
        // The PK
        _file.getFileEntityPK().setIdParent( getIdParent() );
        _file.getFileEntityPK().setName( getName() );
        _file.getFileEntityPK().setIsDir( (short)(isDirectory() ? 1 : 0) );
        //--------------------------------------------------------
        _file.setIdOriginal(  this.idOriginal   );
        _file.setFullPath(    getAbsolutePath() );
        _file.setAccount(     this.account      );
        _file.setOwner(       getOwner()        );
        _file.setLockedBy(    lockedBy          );
        
        _file.setIsHidden(     (short)(isHidden()     ? 1 : 0) );
        _file.setIsPublic(     (short)(isPublic()     ? 1 : 0) );
        _file.setIsReadable(   (short)(isReadable()   ? 1 : 0) );
        _file.setIsModifiable( (short)(isModifiable() ? 1 : 0) );
        _file.setIsDeleteable( (short)(isDeleteable() ? 1 : 0) );
        _file.setIsExecutable( (short)(isExecutable() ? 1 : 0) );
        _file.setIsDuplicable( (short)(isDuplicable() ? 1 : 0) );
        _file.setIsSystem(     (short)(isSystem()     ? 1 : 0) );
        _file.setIsAlterable(  (short)(isAlterable()  ? 1 : 0) );
        _file.setIsInTrashcan( (short)(isInTrashcan() ? 1 : 0) );
        
        _file.setCreated(  getCreated()  );
        _file.setModified( getModified() );
        _file.setAccessed( new Date()    );
        _file.setNotes(    getNotes()    );
    }
    
    public int getId()
    {
        return this.idFile;
    }
    
    public int getIdParent()
    {
        return this.idParent;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public void setName( String sNewName )
    {
        this.name = sNewName;
    }

    public String getOwner()
    {
        return owner;
    }
    
    public boolean isDirectory()
    {
        return isDir;
    }
    
    public String getAbsolutePath()
    {
        // TODO: posiblemente aquí haya que tener en cuenta cosas como si está 
        //       en la Trashcan o si es un Link a otro fichero
        return absolutePath;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public boolean setHidden(boolean isHidden)
    {
        return changeBooleanAttribute( HIDDEN, isHidden );
    }

    public boolean isReadable()
    {
        return isReadable;
    }

    public boolean setReadable(boolean isReadable)
    {
        return changeBooleanAttribute( READABLE, isReadable );
    }
    
    public boolean isModifiable()
    {
        return isModifiable;
    }

    public boolean setModifiable(boolean isModifiable)
    {
        return changeBooleanAttribute( MODIFIABLE, isModifiable );
    }

    public boolean isDeleteable()
    {
        return isDeleteable;
    }

    public boolean setDeleteable(boolean isDeleteable)
    {
        return changeBooleanAttribute( DELETEABLE, isDeleteable );
    }

    public boolean isExecutable()
    {
        return isExecutable;
    }

    public boolean setExecutable(boolean isExecutable)
    {
        return changeBooleanAttribute( EXECUTABLE, isExecutable );
    }

    public boolean isDuplicable()
    {
        return isDuplicable;
    }

    public boolean setDuplicable(boolean isDuplicable)
    {
        return changeBooleanAttribute( DUPLICABLE, isDuplicable );
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public boolean setPublic(boolean isPublic)
    {
        return changeBooleanAttribute( PUBLIC, isPublic );
    }

    public boolean isAlterable()
    {
        return this.isAlterable;
    }
    
    public boolean setAlterable(boolean isAlterable)
    {
        return changeBooleanAttribute( ALTERABLE, isAlterable );
    }
    
    public boolean isInTrashcan()
    {
        return isInTrashcan;
    }

    public boolean setInTrashcan(boolean isInTrashcan)
    {
        return changeBooleanAttribute( IN_TRASHCAN, isInTrashcan );
    }

    public boolean isLocked()
    {
        return lockedBy != null;
    }
    
    public boolean setLocked(boolean lock)
    {
        return changeBooleanAttribute( LOCKED, lock );
    }
    
    public boolean ownsLock()
    {
        return account.equals( lockedBy );
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

    public boolean setNotes( String notes )
    {
        boolean bSuccess = false;
        
        if( account.equals( owner ) )   // TODO: el account tiene que estar en el modo
                                        //       <name>@<server> o algo así para poder compararlos
        {
            this.notes = notes;
            bSuccess = true;
        }
        
        return bSuccess;
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
    
    public String getFailedToChangeAttributeReason()
    {
        return sFailedToChangeAttributeReason;
    }
    
    //------------------------------------------------------------------------//
        
    /**
     * Chages file size.
     * If file is a directory, the new size is ignored.
     * @param size New file size.
     */
    void setSize( long size )
    {
        if( ! isDirectory() )
            this.size = size;
    }
    
    //------------------------------------------------------------------------//
    
    private boolean changeBooleanAttribute( int nWhich, boolean b)
    {
        boolean bSuccess = false;
        
        sFailedToChangeAttributeReason = null;
        
        if( isAlterable()  ||
            account.equals( owner ) )   // TODO: el account tiene que estar en el modo
                                        //       <name>@<server> o algo así para poder compararlos
        {
            // TODO: hacerlo
            switch( nWhich )
            {
                case ALTERABLE:
                break;
                     
                case DELETEABLE:
                break;
                     
                case DUPLICABLE:
                break;
                     
                case EXECUTABLE:
                     if( ! isDirectory() )
                     {
                         isExecutable = b;
                         bSuccess = true;
                     }
                     else
                     {
                         sFailedToChangeAttributeReason = "Directories can not be executables.";
                     }
                break;
                     
                case HIDDEN:
                break;
                     
                case MODIFIABLE:
                break;
                     
                case PUBLIC:
                break;
                     
                case READABLE:
                break;
                     
                case LOCKED:
                     if( b )
                     {
                         if( ! isLocked() || ownsLock() )
                         {
                             lockedBy = account;
                             bSuccess = true;
                         }
                         else
                         {
                             sFailedToChangeAttributeReason = "File is already locked by another user.";
                         }
                     }
                     else
                     {
                         if( ownsLock() )
                         {
                             lockedBy = null;
                             bSuccess = true;
                         }
                         else
                         {
                             sFailedToChangeAttributeReason = "You do not own the lock.";
                         }
                     }
                break;
                     
                case IN_TRASHCAN:
                    isInTrashcan = b;
                    bSuccess = true;
                break;
            }
        }
        else
        {
            sFailedToChangeAttributeReason = "You have to be the owner of the file.";
        }
        
        return bSuccess;
    }
}