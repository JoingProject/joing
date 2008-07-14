/*
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
import java.util.List;
import org.joing.common.exception.JoingServerVFSException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Common functionality used by other classes in this module.
 * <code>FileManagerBean</code> and <code>ListManagerBean</code> among
 * others.
 * 
 * @author Francisco Morero Peyrona
 */
class VFSTools
{
    private final static String sROOT = "/";     // 4 speed
    
    // Note: a file exists even if it is in the trashcan ('cause it can be moved back)
    static boolean existsName( EntityManager em, String sAccount, String sPath, String sName )
    {
        String sFullName = sPath + (sPath.equals( sROOT ) ? "" : sROOT) + sName;
        
        return( path2File( em, sAccount, sFullName ) != null );
    }
    
    /**
     * Obtains the FileEntity instance that represents the file or directory
     * passed as String.
     *
     * @param sAccount An Account representing the owner of the file ro dir.
     * @param sFullname Full file (or dir) name starting at root ("/") and 
     *        ending with the file (or dir) name.
     * @return An instance of class <code>FileEntity</code> that represents the 
     *         file or directory denoted by passed path or <code>null</code> if 
     *         the path does not corresponds with an existing file.
     * @throws JoingServerVFSException if any prerequisite was not satisfied or 
     *         a wrapped third-party exception if something went wrong.
     */
    static FileEntity path2File( EntityManager em, String sAccount, String sFullName )
           throws JoingServerVFSException
    {
        
        FileEntity _file = null;
        String     sPath = null;
        String     sName = null;
        
        if( sFullName == null )
            throw new NullPointerException( "File name can't be null." );
        
        sFullName = sFullName.trim();
        
        if( sFullName.length() == 0 )
            sFullName = sROOT;
        
        if( sFullName.charAt( 0 ) != '/' )
            throw new IllegalArgumentException( "File name must start from root '/'." );
        
        if( sFullName.equals( sROOT ) )
        {
            sPath = "";
            sName = sFullName;
        }
        else
        {
            int nIndex = sFullName.lastIndexOf( '/' ) + 1;

            if( nIndex > 0 && nIndex < sFullName.length() )
            {
                sPath = sFullName.substring( 0, nIndex );
                sName = sFullName.substring( nIndex );
            }
        }
        
        // If sPath != '/' and ends with '/' then removes last '/'
        if( sPath.length() > 1 && sPath.endsWith( sROOT ) )
            sPath = sPath.substring( 0, sPath.length() - 1 );
        
        if( sName == null )
            throw new JoingServerVFSException( "File name can't be null." );
        
        sName = sName.trim();
        
        if( sName.length() == 0 )
            throw new JoingServerVFSException( "File name can't be empty." );
        
        if( sPath.length() > 0 )    // sName is not root (we are not talking about root)
        {
            // Name can't have '/' (leading or trailing)
            if( sName.startsWith( sROOT ) )
                sName = sName.substring( 1 );
                
            if( sName.endsWith( sROOT ) )
                sName = sName.substring( 0, sName.length() - 2 );
        }
        
        if( sName.length() == 0 )
            throw new JoingServerVFSException( "File name can't be empty." );
        
        Query qryFindFile = em.createNamedQuery( "FileEntity.findByPathAndName" );
              qryFindFile.setParameter( "account", sAccount );
              qryFindFile.setParameter( "path"   , sPath    );
              qryFindFile.setParameter( "name"   , sName    );

        try
        {
            _file = (FileEntity) qryFindFile.getSingleResult();
        }
        catch( NoResultException exc )
        {
            // Nothing to do
        }
        
        return( _file );
    }
    
    /**
     * 
     * @param em
     * @param sAccount 
     * @param nFileDirId
     * @return
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    static List<FileEntity> getChilds( EntityManager em, String sAccount, FileEntity _file )
            throws JoingServerVFSException
    {
        if( _file.getIsDir() == 0 )
            throw new JoingServerVFSException( "Passed file is not a directory." );
        
        try
        {
            Query query = em.createNamedQuery( "FileEntity.findByPath" );
                  query.setParameter( "account", sAccount );
                  query.setParameter( "path"   , getAbsolutePath( _file ) );
                  
            return (List<FileEntity>) query.getResultList();
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( VFSTools.class.getName(), "getChilds(...)", exc );
            throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
        }
    }
    
    /**
     * Return the path from root, including file name.
     * 
     * @return Path from root, including file name.
     */
    static String getAbsolutePath( FileEntity fe )
    {
        // TODO: posiblemente aquí haya que tener en cuenta cosas como si está 
        //       en la Trashcan o si es un Link a otro fichero
        
        String        sName = fe.getFileName();   // Can't be null and is already trimmed
        StringBuilder sb    = new StringBuilder( 256 );
        
        if( sName.equals( sROOT ) )
        {
            sb.append( sName );
        }
        else
        {
            sb.append( fe.getFilePath() );
                          
            if( ! fe.getFilePath().endsWith( sROOT ) )
                sb.append( '/' );

            sb.append( sName );
        }
        
        return sb.toString();
    }
    
    //------------------------------------------------------------------------//
    
    private VFSTools()
    {
    }
}