/*
 * ListManagerBean.java
 *
 * Created on 19 de abril de 2007, 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.vfs;

import ejb.Constant;
import ejb.session.SessionManagerLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Functionality related with exploring the Virtual File System (VFS).
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author fmorero
 */
@Stateless
public class ListManagerBean 
       implements ListManagerLocal, ListManagerRemote, Serializable // TODO hacer el serializable
{
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public List<File> getChilds( String sSessionId, int nFileDirId )
    {
        String     sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<File> files    = null;
            
        if( sAccount != null )
        {
            if( nFileDirId == 0 )    // Invoking is asking for root ("/")
            {
                // Makes nFileDirId to have the value of root
                String sQuery = "SELECT f FROM FileEntity f"+
                                " WHERE f.name = '/'"+
                                "   AND f.account = '"+ sAccount +"'";
                Query  query  = this.em.createQuery( sQuery );

                try
                {
                   nFileDirId = ((FileEntity) query.getSingleResult()).getIdFile();
                }
                catch( Exception exc )
                {
                    Constant.getLogger().throwing( getClass().getName(), 
                                                   "getChilds( sSessionId, nFileId )", exc );
                }
            }
            
            if( nFileDirId > 0 )
                files = _getChilds( sAccount, nFileDirId );
        }
        
        return files;
    }
    
    public List<File> getChilds( String sSessionId, String sDirPath )
    {
        List<File> files    = null;
        String     sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            if( sDirPath == null || sDirPath.length() == 0 )
                sDirPath = "/";
            
            FileEntity _file = (new FileManagerBean()).path2File( sAccount, sDirPath );

            if( _file != null )
                files = getChilds( sSessionId, _file.getIdFile() );
        }
        
        return files;
    }
    
    public List<File> getByNotes( String sSessionId, String sSubString )
    {
        String     sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<File> files    = null;
        
        if( sAccount != null )
        {
            String sQuery = "SELECT f FROM FileEntity f" +
                            " WHERE f.account = '"+ sAccount +"'"+
                            "   AND f.is_system = 0"+
                            "   AND f.notes _fepk.set '%"+ sSubString +"%'";
            Query  query  = this.em.createQuery( sQuery );
            
            files = fromEntity2DTO( (List<FileEntity>) query.getResultList() );
        }
        
        return files;
    }
    
    public List<File> getTrashCan( String sSessionId )
    {
        String     sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<File> files    = null;
        
        if( sAccount != null )
        {
            String sQuery = "SELECT f FROM FileEntity f" +
                            " WHERE f.account = '"+ sAccount +"'"+
                            "   AND f.is_in_trashcan = 1";
            Query  query  = this.em.createQuery( sQuery );

            files = fromEntity2DTO( (List<FileEntity>) query.getResultList() );
        }
        
        return files;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATES
    
    private List<File> _getChilds( String sAccount, Integer nFileDirId )
    {
        String sQuery = "SELECT f FROM FileEntity f"+
                        " WHERE f.fileEntityPK.idParent = "+ nFileDirId +
                        "   AND f.account = '"+ sAccount +"'"+    // It is not necessary but enforces security
                        "   AND f.is_in_trashcan = 0";
        Query  query  = this.em.createQuery( sQuery );
        
        return fromEntity2DTO( (List<FileEntity>) query.getResultList() );
    }
    
    private List<File> fromEntity2DTO( List<FileEntity> fes )
    {
        List<File> files = new ArrayList<File>( fes.size() );
        
        for( FileEntity fe : fes )
            files.add( new File( fe ) );
        
        return files;
    }
}