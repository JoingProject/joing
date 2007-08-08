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
import ejb.JoingServerException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Common functionality used by other classes in this module.
 * <code>FileManagerBean</code> and <code>ListManagerBean</code> among
 * others.
 * 
 * @author Francisco Morero Peyrona
 */
class Tools
{
    @PersistenceContext
    private static EntityManager em;
    
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
    static FileEntity path2File( String sAccount, String sPath )
           throws JoingServerVFSException
    {
        FileEntity _file = null;
                           // Path must start from root
        if( sPath != null && sPath.trim().charAt( 0 ) == '/' )
        {
            sPath = sPath.trim();

            try
            {
                Query query = em.createQuery( "SELECT f FROM FileEntity f"+
                                              " WHERE f.account = :account"+
                                              "   AND f.fullPath = :fullPath" );

                // Assuming that it is a file (it is not a dir)
                if( ! sPath.endsWith( "/" ) )
                {
                    int    nIndex   = sPath.lastIndexOf( '/' );
                    String sFile    = sPath.substring( nIndex + 1 );
                    String sPathTmp = sPath.substring( 0, nIndex );    // Can't modify sPath because is used later

                    query.setParameter( "account" , sAccount );
                    query.setParameter( "fullPath", sPathTmp );

                    try
                    {
                        FileEntity _dir = (FileEntity) query.getSingleResult();

                        String s = "SELECT f FROM FileEntity f"+
                                   " WHERE f.account = '"+ sAccount +"'"+
                                   "   AND f.fileEntityPK.idParent = "+ _dir.getFileEntityPK().getIdParent() +
                                   "   AND f.name = '"+ sFile +"'";

                        Query q = em.createQuery( s );

                        _file = (FileEntity) q.getSingleResult();
                    }
                    catch( NoResultException exc )
                    {
                        // Nothing to do
                    }
                }

                // Assuming that it is a dir (it is not a file)
                if( _file == null )
                {
                    if( ! sPath.endsWith( "/" ) )
                        sPath += "/";

                    query.setParameter( "account" , sAccount );
                    query.setParameter( "fullPath", sPath );

                    try
                    {
                        _file = (FileEntity) query.getSingleResult();
                    }
                    catch( NoResultException exc )
                    {
                        // Nothing to do
                    }
                }
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( Tools.class.getName(), "path2File(...)", exc );
                    exc = new JoingServerVFSException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
        }
        
        return _file;
    }
}
