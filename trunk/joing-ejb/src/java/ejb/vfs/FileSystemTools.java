/*
 * FileSystemTools.java
 *
 * Created on 3 de junio de 2007, 21:50
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

import ejb.Constant;
import java.io.IOException;

/**
 * A class to perform all needed operations with the O.S. File System.
 * This class does not perform any security checking: it assumes that
 * is activity was done by those classes invoking this one.
 *
 *
 * TODO: Añadirle a esta clas el soporte para Windows.
 *       (Actualmente sólo funciona con S.O. *nix.)
 *
 * @author Francisco Morero Peyrona
 */
public class FileSystemTools
{
    /**
     * Cretes all files and dirs related with an account.
     * 
     * @param sAccount Account to be created.
     * @return <code>true</code> if everything goes fine.
     */
    public static boolean createAccount( String sAccount )
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
    public static boolean removeAccount( String sAccount )
    {
        return deleteDirectory( getUserHome( sAccount ) );
    }
    
    /**
     * Creates an empty file.
     *
     * @param sAccount 
     * @param sFileName
     */
    public static void createFile( String sAccount, String sFileName ) 
           throws IOException
    {
        java.io.File fNew = getFile( sAccount, sFileName );
                     
        if( ! fNew.createNewFile() )
            throw new IOException( "Error creating file ["+ sFileName +"]\n"+
                                   "for account = ["+ sAccount +"]" );
    }
    
    /**
     * Returns file handler to a physical file.
     *
     * @param sAccount 
     * @param sFileName
     */
    public static java.io.File getFile( String sAccount, String sFileName )
    {
        return (new java.io.File( getUserHome( sAccount ), sFileName ));
    }
    
    /**
     * 
     * 
     */
    public static java.io.File getApplication( String sAppName )
    {
        return (new java.io.File( getAppDir(), sAppName ));
    }
    
    /**
     * Deletes a file from the FS
     *
     * @param sAccount An user account
     * @param nFileId  A File Id
     */
    public static boolean deleteFile( String sAccount, int nFileId )
    {
        return getAbsoluteFile( sAccount, nFileId ).delete();
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
    public static long getFileSize( String sAccount, int nFileId )
    {
        return getAbsoluteFile( sAccount, nFileId ).length();
    }
    
    /**
     * Returns free disk space
     *
     * @return Free disk space
     */
    public static long getDiskFreeSpace()
    {              
        return Constant.getBaseDir().getFreeSpace();
    }
    
    /**
     * Calculates the sum of all files size owned by passed account.
     *
     * @param sAccount An user account
     * @return The sum of all files size owned by passed account.
     */
    public static long getUsedSpace( String sAccount )
    {
        long nAmount = 0;
        java.io.File[] aFile = getUserHome( sAccount ).listFiles();
        
        for( int n = 0; n < aFile.length; n++ )
            nAmount += aFile[n].length();
            
        return nAmount;
    }
    
    //------------------------------------------------------------------------//
    
    // Retrieves the user home directory
    private static java.io.File getUserHome( String sAccount )
    {
        return (new java.io.File( Constant.getUserDir(), sAccount ));
    }
    
    private static java.io.File getAppDir()
    {
        return (new java.io.File( Constant.getBaseDir(), "applications" ) );
    }
    
    // Nota: no pongo éste método en la clase vfs.File porque no quiero que 
    //       el Cliente se lleve la referencia a la clase ejb.Constant
    private static String getAbsolutePath( String sAccount, int nFileId )
    {
        return Constant.getBaseDir() +"vfs/"+ sAccount +"/"+ nFileId;
    }
    
    // Nota: no pongo éste método en la clase vfs.File porque no quiero que 
    //       el Cliente se lleve la referencia a la clase ejb.Constant   
    private static java.io.File getAbsoluteFile( String sAccount, int nFileId )
    {
        return (new java.io.File( getAbsolutePath( sAccount, nFileId ) ));
    }
    
    // Recursively deletes a directory an all its files
    private static boolean deleteDirectory( java.io.File path )
    {
        if( path.exists() )
        {
            java.io.File[] files = path.listFiles();
            
            for( int n = 0; n < files.length; n++ )
            {
                if( files[n].isDirectory() )
                    deleteDirectory( files[n] );
                else
                    files[n].delete();
            }
        }
        
        return( path.delete() );
    }
}