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

package org.joing.common.dto.vfs;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * This is class is instatiated and used only at Server side, at Client side
 * VFSFile (a subclass of this one) is used.
 * <p>
 * Just to make things more clear, I decided to divide VFSFile into two classes:
 * This class has extra variables (non available in java.io.File) and its 
 * related methods.
 * 
 * @author Francisco Morero Peyrona
 */
public class VFSFileBase extends File implements Serializable
{
    /**
     * The system-dependent default name-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>file.separator</code>. Join'g uses same as UNIX systems, 
     * the value of this field is <code>'/'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char separatorChar = '/';

    /**
     * The system-dependent default name-separator character, represented as a
     * string for convenience.  This string contains a single character, namely
     * <code>{@link #separatorChar}</code>.
     */
    public static final String separator = "" + separatorChar;

    /**
     * The system-dependent path-separator character.  This field is
     * initialized to contain the first character of the value of the system
     * property <code>path.separator</code>.  This character is used to
     * separate filenames in a sequence of files given as a <em>path list</em>.
     * Join'g uses same as UNIX systems, this character is <code>':'</code>.
     *
     * @see     java.lang.System#getProperty(java.lang.String)
     */
    public static final char pathSeparatorChar = ':';

    /**
     * The system-dependent path-separator character, represented as a string
     * for convenience.  This string contains a single character, namely
     * <code>{@link #pathSeparatorChar}</code>.
     */
    public static final String pathSeparator = "" + pathSeparatorChar;
    
    //---------------------------------------------//
    
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    // PK --------------------
    private String  account      = null;    // read-only
    private String  name         = null;    // read-only
    private String  path         = null;    // read-only (path from root except the file name)
    //------------------------
    private int     idFile       = -1;      // read-only
    private int     idOriginal   = -1;      // TODO: no creo que esto vaya así: tendría que estar aquí el original y sobrarían el resto de los campos
    private boolean isDir        = false;   // read-only
    private String  owner        = null;    // read-only
    private String  lockedBy     = null;
    
    private boolean isHidden     = false;
    private boolean isPublic     = false;
    private boolean isReadable   = true;
    private boolean isModifiable = true;
    private boolean isDeleteable = true;
    private boolean isExecutable = false;
    private boolean isDuplicable = true;
    private boolean isAlterable  = true;
    private boolean isInTrashcan = false;
    
    private long    created      = -1L;    // read-only     // Even if this vars are dates as longs:
    private long    modified     = -1L;    // read-only     // it is it's better to manage them faster
    private long    accessed     = -1L;    // read-only     // and smaller to be seralized (transported)
    private long    size         = -1L;    // read-only
    private String  notes        = null;
    
    //---------------------------------------------//
    
    private String  sFailedToChangeAttributeReason = null;
    
    //------------------------------------------------------------------------//
    
    public VFSFileBase( String sPathAndName )
    {
        super( sPathAndName );
        
        String[] as = { null, null };
        
        sPathAndName = sPathAndName.trim();
        
        if( separator.equals( sPathAndName ) )
        {
            as[0] = null;          // Not needed, just for clarity
            as[1] = sPathAndName;
        }
        else
        {
            int nIndex = sPathAndName.lastIndexOf( separatorChar );
            as[0] = sPathAndName.substring( 0, nIndex - 1 );
            as[1] = sPathAndName.substring( 0, nIndex + 1 );
        }
        
        path = as[0];
        name = as[1];
    }
    
    public VFSFileBase( String sPath, String sName )
    {
        super( buildFullPath( sPath, sName ) );
        name = sName.trim();    // Can't use setName( sName ) because invokes canChange()
        setPath( sPath );
    }
    
    //------------------------------------------------------------------------//
    
    public int getHandler()
    {
        return this.idFile;
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
     * <p>
     * Note: By contract with java.io.Parent, if file is root, it has to 
     * return null.
     * 
     * @return All except the file name. If file is root, return null.
     * @see java.io.File#getParent()
     */
    @Override
    public String getParent()
    {
        return path;
    }
    
    /**
     * Just the name of the file or directory (it does not include the path).
     * 
     * @return Just the name of the file or directory.
     * @see java.io.File#getName()
     */
    @Override
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
    
    /**
     * @see java.io.File#isDirectory()
     */
    @Override
    public boolean isDirectory()
    {
        return isDir;
    }
    
    /**
     * Return the path from root, including file name.
     * 
     * @return Path from root, including file name.
     * @see java.io.File#getAbsolutePath()
     */
    @Override
    public String getAbsolutePath()
    {
        // TODO: posiblemente aquí haya que tener en cuenta cosas como si está 
        //       en la Trashcan o si es un Link a otro fichero
        
        String sName = getName();   // Can't be null and is already trimmed
        String sPath = null;
        
        if( sName.equals( separator ) )
        {
            sPath = sName;
        }
        else
        {
            StringBuffer sb = new StringBuffer( 256 );
                         sb.append( path );

            if( ! path.endsWith( separator ) )
                sb.append( separatorChar );

            sb.append( sName );
            sPath = sb.toString();
        }
        
        return sPath;
    }
    
    /**
     * @see java.io.File#isHidden()
     */
    @Override
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

    /**
     * Returns true if file can be read.
     * <p>
     * It is equivalent to java.io.File::canRead(), but I create this new 
     * method because canRead() is not a Java apropriate name.
     * 
     * @return Returns true if file can be read.
     * @see java.io.File#canRead()
     */
    public boolean isReadable()
    {
        return isReadable;
    }

    /**
     * @see java.io.File#setReadable( boolean readable )
     * @param isReadable
     * @return
     */
    @Override
    public boolean setReadable(boolean isReadable)
    {
        if( canChange() )
        {
            this.isReadable = isReadable;
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if file can be can written.
     * <p>
     * It is equivalent to java.io.File::canWrite(), but I create this new 
     * method because canWrite() is not a Java apropriate name.
     * 
     * @return Returns true if file can be written.
     * @see java.io.File#canWrite()
     */
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

    /**
     * Returns true if file can be can executed.
     * <p>
     * It is equivalent to java.io.File::canExecute(), but I create this new 
     * method because canExecute() is not a Java apropriate name.
     * 
     * @return Returns true if file can be executed.
     * @see java.io.File#canExecute()
     */
    public boolean isExecutable()
    {
        return isExecutable;
    }

    /**
     * @see java.io.File#setExecutable( boolean executable )
     */
    @Override
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
    
    public long getCreated()
    {
        return created;
    }

    /**
     * Returns last time the file was modified as a long.
     * <p>
     * It is equivalent to java.io.File::lastModified(), but I create this new 
     * method because lastModified() is not a Java apropriate name.
     * 
     * @return Last time the file was modified as a long.
     * @see java.io.File#lastModified()
     */
    public long getModified()
    {
        return modified;
    }

    public long getAccessed()
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
    
    /**
     * Returns file size.
     * <p>
     * It is equivalent to java.io.File::length(), but I create this new method
     * because length() is not a Java apropriate name.
     * 
     * @return File size.
     * @see java.io.File#length()
     */
    public Long getSize()
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
    // Following methods should be only known by Server or at least  protected.
    
    public void setAccount( String account )
    {
        this.account = account;
    }
    
    public void setHandler( int nIdFile )
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
        if( sPath != null )
        {
            sPath = sPath.trim();
            
            if( sPath.length() == 0 )
                sPath = null;
        }
        
        this.path = sPath;
    }
    
    public void setOwner( String sOwner )
    {
        this.owner = sOwner;
    }
    
    public void setLockedBy( String sLockedBy )
    {
        this.lockedBy = sLockedBy;
    }
    
    public void setCreated( long nWhen )
    {
        this.created = ((nWhen < 0) ? 0 : nWhen);
    }
    
    public void setModified( long nWhen )
    {
        this.modified = ((nWhen < 0) ? 0 : nWhen);
    }
    
    public void setAccessed( long nWhen )
    {
        this.accessed = ((nWhen < 0) ? 0 : nWhen);
    }
    
    /**
     * Chages file size.
     * If file is a directory, the new size is ignored.
     * @param size New file size.
     */
    public void setSize( long size )
    {
        if( isDirectory() )
            this.size = 0L;
        else
            this.size = ((size < 0) ? 0 : size);
    }
    
    //------------------------------------------------------------------------//
    
    protected void updateFrom( VFSFileBase fBase )
    {
        // Don't use methods because some of them (v.g. setName(...)) include restrictions
        idFile       = fBase.getHandler();
        account      = fBase.getAccount();
        name         = fBase.getName();
        path         = fBase.getParent();
        idOriginal   = fBase.getIdOriginal();
        owner        = fBase.getOwner();
        lockedBy     = fBase.getLockedBy();
        
        isDir        = fBase.isDirectory();
        isHidden     = fBase.isHidden();
        isPublic     = fBase.isPublic();
        isReadable   = fBase.isReadable();
        isModifiable = fBase.isModifiable();
        isDeleteable = fBase.isDeleteable();
        isExecutable = fBase.isExecutable();
        isDuplicable = fBase.isDuplicable();
        isAlterable  = fBase.isAlterable();
        isInTrashcan = fBase.isInTrashcan();
        
        created      = fBase.getCreated();
        modified     = fBase.getModified();
        accessed     = fBase.getAccessed();
        notes        = fBase.getNotes();
        size         = fBase.length();
    }
    
    private boolean canChange()
    {// FIXME: Faltan un montón de comprobaciones (repasar todos los permisos)
        boolean      bSuccess = true;
        StringBuffer sbReason = new StringBuffer( 512 );
        
        if( account != null && ! account.equals( owner ) )
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
        
        return true;
    }
    
    /**
     * Constructs a file-path based on parent and child passed strings.
     * 
     * @param sParent
     * @param sChild
     * @return The full path composed by passed parent and child
     */
    private static String buildFullPath( String sParent, String sChild )
    {
        StringBuffer sb = new StringBuffer( 256 );
        
        // Parent part: must exists and start on root ('/')
        if( sParent == null || sParent.trim().length() == 0 )
        {
            sb.append( separatorChar );
        }
        else
        {
            sParent = sParent.trim();
            
            if( sParent.charAt( 0 ) != '/' )
                sb.append( '/' );
                        
            sb.append( sParent );
        }
        
        // Child part: can't start with root and can exists or not
        if( sChild != null && sChild.trim().length() > 0 )
        {
            if( sb.charAt( sb.length() - 1 ) != separatorChar )  // If not ends with sep, append it
                sb.append( separatorChar );
             
            sChild = sChild.trim();
            
            if( ! separator.equals( sChild ) )
            {
                if( sChild.charAt( 0 ) == separatorChar )
                    sChild = sChild.substring( 1 );                           // Removes first '/'
                
                if( sChild.charAt( sChild.length() - 1 ) == separatorChar )   // Removes last '/'
                    sChild = sChild.substring( 0, sChild.length() - 1 );
                
                sb.append( sChild );
            }
        }
        
        return sb.toString();
    }
}