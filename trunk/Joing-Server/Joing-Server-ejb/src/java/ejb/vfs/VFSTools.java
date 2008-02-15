/*
 * FileTools.java
 * 
 * Created on 08-ago-2007, 13:05:42
 * 
 * Author: Francisco Morero Peyrona.
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
import org.joing.common.exception.JoingServerException;
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
    /**
     * Obtains the FileEntity instance that represents the file or directory
     * passed as String.
     *
     * @param sAccount An Account representing the owner of the file ro dir.
     * @param sPath Path starting at root ("/") and ending with the file or dir
     *        name.
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
        
        if( sFullName != null )
        {
            sFullName = sFullName.trim();
            
            if( sFullName.length() == 0 )
                throw new JoingServerVFSException( "Path can't be empty." );
            
            if( sFullName.charAt( 0 ) != '/' )
                throw new JoingServerVFSException( "Path must start from root." );
            
            try
            {
                Query  qryFindFile = em.createNamedQuery( "FileEntity.findByPathAndName" );
                String sPath       = null;
                String sName       = null;
                
                if( sFullName.equals( "/" ) )
                {
                    sPath = "";
                    sName = sFullName;
                }
                else
                {
                    int nIndex = sFullName.lastIndexOf( '/' );
                     
                    sName = sFullName.substring( nIndex + 1 );
                    sPath = sPath.substring( 0, nIndex );
                }
                
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
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( VFSTools.class.getName(), "path2File(...)", exc  );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
        }
        
        return _file;
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
            throw new JoingServerVFSException( "It is not a directory." );
        
        try
        {
            String sFullName = _file.getFilePath() +"/"+ _file.getFileName();
            
            Query query = em.createNamedQuery( "FileEntity.findByPath" );
                  query.setParameter( "account", sAccount  );
                  query.setParameter( "path"   , sFullName );
                  
            return (List<FileEntity>) query.getResultList();
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( VFSTools.class.getName(), "getChilds(...)", exc );
            throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
        }
    }
    
    /**
     * Returns parent Entity based on its ID.
     * @param em An instance of EntityManager
     * @param nParentId
     * @return Returns parent Entity based on its ID, or null if it does not exists.
     * @throws org.joing.common.exception.JoingServerVFSException
     */
    static FileEntity getParentEntity( EntityManager em, int nParentId )
            throws JoingServerVFSException
    {
        FileEntity _feParent = null;
        
        return _feParent;
    }
    
    //------------------------------------------------------------------------//
    
    private VFSTools()
    {
    }
}