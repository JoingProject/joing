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
import org.joing.common.dto.vfs.FileDescriptor;
import org.joing.common.exception.JoingServerVFSException;

/**
 * Functionality related with exploring the Virtual File System (VFS).
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author fmorero
 */
@Stateless
public class ListManagerBean 
       implements ListManagerRemote, ListManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
     
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public List<FileDescriptor> getRoots( String sSessionId )
            throws JoingServerVFSException
    {
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<FileDescriptor> roots    = null;
            
        if( sAccount != null )
        {
            // TODO: mejorarlo buscando en otras comunidades donde esté el user
            FileEntity _file = Tools.path2File( em, sAccount, "/" );
            
            roots = new ArrayList<FileDescriptor>();
            roots.add( FileDTOs.createFileDescriptor( _file ) );
        }
        
        return roots;
    }
    
    public List<FileDescriptor> getChilds( String sSessionId, int nFileDirId )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files    = null;
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                if( nFileDirId == 0 )    // Invoking is asking for root ("/")
                {
                    Query query = em.createQuery( "SELECT f FROM FileEntity f"+
                                                  " WHERE f.name = '/'"+
                                                  "   AND f.account = '"+ sAccount +"'" );

                    nFileDirId = ((FileEntity) query.getSingleResult()).getIdFile();

                    if( nFileDirId > 0 )
                        files = _getChilds( sAccount, nFileDirId );
                }
                else if( nFileDirId > 0 )    // Negative numbers are not accepted
                {
                    files = _getChilds( sAccount, nFileDirId );
                }
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getChilds(String,int)", exc );
                throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
            }
            
        }
        
        return files;
    }
    
    public List<FileDescriptor> getChilds( String sSessionId, String sDirPath )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files    = null;
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            if( sDirPath == null || sDirPath.length() == 0 )
                sDirPath = "/";
            
            FileEntity _file = Tools.path2File( em, sAccount, sDirPath );

            if( _file != null )
                files = getChilds( sSessionId, _file.getIdFile() );
        }
        
        return files;
    }
    
    public List<FileDescriptor> getByNotes( String sSessionId, String sSubString )
           throws JoingServerVFSException
    {
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<FileDescriptor> files    = null;
        
        if( sAccount != null )
        {
            StringBuilder sbQuery = new StringBuilder( 1024 );
                          sbQuery.append( "SELECT f FROM FileEntity f" )
                                 .append( " WHERE f.account = '" ).append( sAccount ).append( '\'' )
                                 .append( "   AND f.is_system = 0" )
                                 .append( "   AND f.notes _fepk.set '%" ).append( sSubString ).append( "%'" );
            try
            {
                Query query = em.createQuery( sbQuery.toString() );
            
                files = fromEntity2DTO( (List<FileEntity>) query.getResultList() );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getTrashCan(...)", exc );
                throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
            }
        }
        
        return files;
    }
    
    public List<FileDescriptor> getTrashCan( String sSessionId )
           throws JoingServerVFSException
    {
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<FileDescriptor> files    = null;
        
        if( sAccount != null )
        {
            StringBuilder sbQuery = new StringBuilder( 512 );
                          sbQuery.append( "SELECT f FROM FileEntity f" )
                                 .append( " WHERE f.account = '" ).append( sAccount ).append( '\'' )
                                 .append( "   AND f.is_in_trashcan = 1" );
            try
            {
                Query query = em.createQuery( sbQuery.toString() );

                files = fromEntity2DTO( (List<FileEntity>) query.getResultList() );
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getTrashCan(...)", exc );
                throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
            }
        }
        
        return files;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATES
    
    private List<FileDescriptor> _getChilds( String sAccount, Integer nFileDirId )
            throws JoingServerVFSException
    {
        try
        {
            Query query = em.createNamedQuery( "FileEntity.findByPath" );
                  query.setParameter( "account", sAccount );
                  query.setParameter( "path"   , ""       );
                  
            return fromEntity2DTO( (List<FileEntity>) query.getResultList() );
        }
        catch( RuntimeException exc )
        {
            Constant.getLogger().throwing( getClass().getName(), "_getChilds(...)", exc );
            throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
        }
    }
    
    private List<FileDescriptor> fromEntity2DTO( List<FileEntity> fes )
    {
        List<FileDescriptor> files = new ArrayList<FileDescriptor>( fes.size() );
        
        for( FileEntity fe : fes )
            files.add( FileDTOs.createFileDescriptor( fe )  );
        
        return files;
    }
}