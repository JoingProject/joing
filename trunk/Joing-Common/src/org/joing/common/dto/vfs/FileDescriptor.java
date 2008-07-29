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

package org.joing.common.dto.vfs;

import java.io.Serializable;
import java.util.Date;
// FIXME: reparsar todos los permisos viendo cuándo se puede o no cambiar algo.
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
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    // PK --------------------
    private String  account;           // read-only
    private String  name;              // read-only
    private String  path;              // read-only (path from root except the file name)
    //------------------------
    private int     idFile;            // read-only
    private int     idOriginal;        // TODO: no creo que esto vaya así: tendría que estar aquí el original y sobrarían el resto de los campos
    private boolean isDir;             // read-only
    private String  owner;             // read-only
    private String  lockedBy;
    
    private boolean isHidden;
    private boolean isPublic;
    private boolean isReadable;
    private boolean isModifiable;
    private boolean isDeleteable;
    private boolean isExecutable;
    private boolean isDuplicable;
    private boolean isAlterable;
    private boolean isInTrashcan;
    
    private Date    created;           // read-only
    private Date    modified;          // read-only
    private Date    accessed;          // read-only
    private String  notes;
    private long    size;              // read-only
    
    //---------------------------------------------//
    
    private String  sFailedToChangeAttributeReason = null;
            
    //------------------------------------------------------------------------//
    
    public int getId()
    {
        return this.idFile;
    }
    
    public void setId( int idFile )
    {
        this.idFile = idFile;
    }
    
    public int getIdOriginal()
    {
        return this.idOriginal;
    }
    
    public String getAccount()
    {
        return account;
    }
    /**
     * Return all except the file name.
     * 
     * @return All except the file name.
     */
    public String getParent()
    {
        return path;
    }
    
    /**
     * Just the name of the file or directory (it does not include the path).
     * 
     * @return Just the name of the file or directory.
     */
    public String getName()
    {
        return name;
    }
    
    public boolean setName( String name )
    {
        if( canChange() )
        {
            this.name = name.trim();
            return true;
        }
        
        return false;
    }

    public String getOwner()
    {
        return owner;
    }
    
    public boolean isDirectory()
    {
        return isDir;
    }
    
    /**
     * Return the path from root, including file name.
     * 
     * @return Path from root, including file name.
     */
    public String getAbsolutePath()
    {
        // TODO: posiblemente aquí haya que tener en cuenta cosas como si está 
        //       en la Trashcan o si es un Link a otro fichero
        
        String sName = getName();   // Can't be null and is already trimmed
        String sPath = null;
        
        if( sName.equals( "/" ) )
        {
            sPath = sName;
        }
        else
        {
            StringBuffer sb = new StringBuffer( 256 );
                         sb.append( path  );

            if( ! path.endsWith( "/" ) )
                sb.append( '/' );

            sb.append( sName );
            sPath = sb.toString();
        }
        
        return sPath;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public boolean setHidden(boolean isHidden)
    {
        if( canChange() )
        {
            this.isHidden = isHidden;
            return true;
        }
        
        return false;
    }

    public boolean isReadable()
    {
        return isReadable;
    }

    public boolean setReadable(boolean isReadable)
    {
        if( canChange() )
        {
            this.isReadable = isReadable;
            return true;
        }
        
        return false;
    }
    
    public boolean isModifiable()
    {
        return isModifiable;
    }

    public boolean setModifiable(boolean isModifiable)
    {
        if( canChange() )
        {
            this.isModifiable = isModifiable;
            return true;
        }
        
        return false;
    }

    public boolean isDeleteable()
    {
        return isDeleteable;
    }

    public boolean setDeleteable(boolean isDeleteable)
    {
        if( canChange() )
        {
            this.isDeleteable = isDeleteable;
            return true;
        }
        
        return false;
    }

    public boolean isExecutable()
    {
        return isExecutable;
    }

    public boolean setExecutable(boolean isExecutable)
    {
        boolean bSuccess = canChange();
        
        if( bSuccess )
        {
            if( ! isDirectory() )
                this.isExecutable = isExecutable;
            else
                sFailedToChangeAttributeReason = "Directories can not be executables.";
        }
        
        return bSuccess;
    }

    public boolean isDuplicable()
    {
        return isDuplicable;
    }

    public boolean setDuplicable(boolean isDuplicable)
    {
        if( canChange() )
        {
            this.isDuplicable = isDuplicable;
            return true;
        }
        
        return false;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public boolean setPublic(boolean isPublic)
    {
        if( canChange() )
        {
            this.isPublic = isPublic;
            return true;
        }
        
        return false;
    }

    public boolean isAlterable()
    {
        return this.isAlterable;
    }
    
    public boolean setAlterable(boolean isAlterable)
    {
        if( account.equals( owner ) )
        {
            this.isAlterable = isAlterable;
            return true;
        }
        
        return false;
    }
    
    public boolean isInTrashcan()
    {
        return isInTrashcan;
    }

    public boolean setInTrashcan(boolean isInTrashcan)
    {
        if( canChange() )
        {
            this.isInTrashcan = isInTrashcan;
            return true;
        }
        
        return false;
    }

    public boolean isLocked()
    {
        return lockedBy != null;
    }
    
    public String getLockedBy()
    {
        return lockedBy;
    }
    
    public boolean setLocked( boolean bLock )
    {
        boolean bSuccess = canChange();

        if( bSuccess )
        {
            if( bLock )
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
        }

        return bSuccess;
    }
    
    /**
     * Just a short-cut
     */
    public boolean ownsLock()
    {
        return account.equals( lockedBy );
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
        if( canChange() )
        {
            this.notes = ((notes == null) ? "" : notes);
            return true;
        }
        
        return false;
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
        return getClass().getName() +"[isDir="+ isDirectory() +", name="+ getAbsolutePath() +"]";
    }
    
    public String getFailedToChangeAttributeReason()
    {
        return sFailedToChangeAttributeReason;
    }
    
    //------------------------------------------------------------------------//
    // NEXT: Los siguientes métodos debieran ser package (o al menos protected)
    
    /** 
     * Class constructor (this class is a DTO).
     * 
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    public FileDescriptor()
    {
    }
   
    public void setAccount( String account )
    {
        this.account = account;
    }
    
    public void setIdFile( int nIdFile )
    {
        this.idFile = nIdFile;
    }
    
    public void setIdOriginal( int nIdOriginal )
    {
        this.idOriginal = nIdOriginal;
    }
    
    public void setDirectory( boolean bIsDir )
    {
        this.isDir = bIsDir;
    }
    
    public void setPath( String sPath )
    {
        this.path = ((sPath == null) ? "" : sPath.trim());
    }
    
    public void setOwner( String sOwner )
    {
        this.owner = sOwner;
    }
    
    public void setLockedBy( String sLockedBy )
    {
        this.lockedBy = sLockedBy;
    }
    
    public void setCreated( Date date )
    {
        this.created = ((date == null) ? new Date( 0 ) : date);
    }
    
    public void setModified( Date date )
    {
        this.modified = ((date == null) ? new Date( 0 ) : date);
    }
    
    public void setAccessed( Date date )
    {
        this.accessed = ((date == null) ? new Date( 0 ) : date);
    }
    
    /**
     * Chages file size.
     * If file is a directory, the new size is ignored.
     * @param size New file size.
     */
    public void setSize( long size )
    {
        if( ! isDirectory() )
            this.size = ((size < 0) ? 0 : size);
    }
    
    //------------------------------------------------------------------------//
    
    private boolean canChange()
    {
        boolean      bSuccess = true;
        StringBuffer sbReason = new StringBuffer( 512 );
        
        if( ! account.equals( owner ) )
        {
            sbReason.append( "You have to be the owner of the file" );
            bSuccess = false;
        }
        
        if( ! isAlterable() )
        {
            if( sbReason.length() > 0 )
                sbReason.append( "\nand file" );
            else
                sbReason.append( "File" );
            
            sbReason.append( " is under 'protected status' against changes." );
            
            bSuccess = false;
        }
        
        sFailedToChangeAttributeReason = ((sbReason.length() == 0) ? null : sbReason.toString());
        
        return true;// FIXME: Cambiarlo por --> bSuccess;
    }
}