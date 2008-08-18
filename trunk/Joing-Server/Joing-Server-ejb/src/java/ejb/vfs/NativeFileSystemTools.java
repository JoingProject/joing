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
package ejb.vfs;

import ejb.Constant;
import java.io.IOException;

/**
 * A class to perform all needed operations with the O.S. File System.
 * This class does not perform any security checking: it assumes that
 * this activity was done by those classes invoking this one.
 *
 * NEXT: Añadirle a esta clas el soporte para Windows.
 *       (Actualmente sólo funciona con S.O. *nix.)
 *
 * @author Francisco Morero Peyrona
 */
// TODO: Esta clase habría que hacerla con constructor en lugar de como métodos static para poder crear múltiples instancias y que el servidor escale mejor
public class NativeFileSystemTools
{
    /**
     * Cretes all files and dirs related with an account.
     * 
     * @param sAccount Account to be created.
     * @return <code>true</code> if everything goes fine.
     */
    public static synchronized boolean createAccount( String sAccount )
           throws IOException
    {
        boolean bSuccess = getUserHome( sAccount ).mkdirs();
        
        if( ! bSuccess )
            throw new IOException( "Can't create user home directory." );
        
        return bSuccess;
    }
    
    /**
     * Deletes all files and dirs related with an account.
     * 
     * @param sAccount Account to be deleted.
     * @return <code>true</code> if everything goes fine.
     */
    public static synchronized boolean removeAccount( String sAccount )
    {
        return deepDelete( getUserHome( sAccount ) );
    }
    
    /**
     * Creates an empty file.
     *
     * @param sAccount A valid user account
     * @param nFileId  The file Id (it is used as file name) 
     */
    public static synchronized void createFile( String sAccount, int nFileId  ) 
           throws IOException
    {
        java.io.File fNew = _getUserFile( sAccount, nFileId  );
        
        if( ! fNew.createNewFile() )
            throw new IOException( "Error creating file\n"+
                                   "   File name = '"+ fNew.getName() +"'\n"+
                                   "   Account   = '"+ sAccount +"'" );
    }
    
    /**
     * Returns file handler to a physical file.
     *
     * @param sAccount      A valid user account
     * @param nFileId       The file Id (it is used as file name) 
     */
    public static synchronized java.io.File getFile( String sAccount, int nFileId  )
    {        
        return _getUserFile( sAccount, nFileId  );
    }
    
    /**
     * 
     * 
     * @param sAppName Full app name including path (if any) from Apps base dir.
     */
    public static synchronized java.io.File getApplication( String sAppName )
    {
        return new java.io.File( Constant.getAppDir(), sAppName );
    }
    
    /**
     * Deletes a file from the FS
     *
     * @param sAccount An user account
     * @param nFileId  A File Id
     */
    public static synchronized boolean deleteFile( String sAccount, int nFileId )
    {
        return _getUserFile( sAccount, nFileId ).delete();
    }
    
    /**
     * Returns the length of the file denoted by passed parameters.
     * <p>
     * The return value is unspecified if this nFileID denotes a 
     * directory.
     *
     * @param sAccount An user account
     * @param nFileId  A File Id
     * @return The length, in bytes, or 0L if the file does not exist.
     */
    public static synchronized long getFileSize( String sAccount, int nFileId )
    {
        return _getUserFile( sAccount, nFileId ).length();
    }
    
    /**
     * Calculates the sum of all files size owned by passed account.
     *
     * @param sAccount An user account
     * @return The sum of all files size owned by passed account.
     */
    public static synchronized long getUsedSpace( String sAccount )
    {
        long nAmount = 0;
        // For some reason we're having a null pointer exception here,
        // let's do some magical handling.
        java.io.File userHomeFile = getUserHome(sAccount);
        java.io.File[] aFile = (userHomeFile == null) ? new java.io.File[] {} :
                ((userHomeFile.listFiles() == null) ? new java.io.File[] {} : 
                    userHomeFile.listFiles());
        
        for( int n = 0; n < aFile.length; n++ )
            nAmount += aFile[n].length();
            
        // TODO: Proposal: In case of an error, like a user account incorrectly
        // configuted, return -1.
        return nAmount;
    }
    
    //------------------------------------------------------------------------//
    
    private static java.io.File _getUserFile( String sAccount, int nFileId )
    {
        return new java.io.File( getUserHome( sAccount ), Integer.toString( nFileId ) );
    }
    
    // Retrieves the user home directory.
    private static java.io.File getUserHome( String sAccount )
    {
        // sAccount is the form <user_name>@<provider_name>.<domain>
        // and the user real (physical) root directory is the <user_name>
        sAccount = sAccount.substring( 0, sAccount.indexOf( '@' ) );
        
        return new java.io.File( Constant.getUserDir(), sAccount );
    }
    
    // If passed file is a directory, recursively deletes it an all its files.
    // If passed file is just a file, deletes it.
    private static boolean deepDelete( java.io.File f )
    {
        boolean bSuccess = false;
        
        if( f.exists() )
        {
            bSuccess = true;
            java.io.File[] files = f.listFiles();
            
            if( files != null )
            {
                for( int n = 0; n < files.length; n++ )
                {
                    if( files[n].isDirectory() )
                        bSuccess = bSuccess && deepDelete( files[n] );
                    else
                        bSuccess = bSuccess && files[n].delete();
                }
            }
            
            bSuccess = bSuccess && f.delete();
        }
        
        return bSuccess;
    }
}