/*
 * ListManagerBean.java
 *
 * Created on 19 de abril de 2007, 14:09
 *
 * To change this template, choose VFSTools | Template Manager
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
       implements ListManagerLocal, Serializable
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
            // NEXT: mejorarlo buscando en otras comunidades donde esté el user
            FileEntity _file = VFSTools.path2File( em, sAccount, "/" );
            
            roots = new ArrayList<FileDescriptor>();
            roots.add( FileDTOs.createFileDescriptor( _file ) );
        }
        
        return roots;
    }
    
    public List<FileDescriptor> getChilds( String sSessionId, int nFileDirId )
           throws JoingServerVFSException
    {
        return getChilds( sSessionId, (Object) new Integer( nFileDirId ) );
    }
    
    public List<FileDescriptor> getChilds( String sSessionId, String sDirPath )
           throws JoingServerVFSException
    {
        return getChilds( sSessionId, (Object) sDirPath );
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
                                 .append( " WHERE f.fileEntityPK.account = '" ).append( sAccount ).append( '\'' )
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
            try
            {
                Query query = em.createNamedQuery( "FileEntity.findInTrashcan" );
                      query.setParameter( "account", sAccount );
                      
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
    
    private List<FileDescriptor> fromEntity2DTO( List<FileEntity> fes )
    {
        List<FileDescriptor> files = new ArrayList<FileDescriptor>( fes.size() );
        
        for( FileEntity fe : fes )
            files.add( FileDTOs.createFileDescriptor( fe )  );
        
        return files;
    }
    
    private List<FileDescriptor> getChilds( String sSessionId, Object param )
           throws JoingServerVFSException
    {
        List<FileDescriptor> files    = null;
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            FileEntity _file = null;
            
            if( param instanceof String )
                _file = VFSTools.path2File( em, sAccount, (String) param );
            else
                _file = em.find( FileEntity.class, (Integer) param );
            
            if( _file != null )
            {
                if( _file.getAccount().equals( sAccount ) )   
                    files = fromEntity2DTO( VFSTools.getChilds( em, sAccount, _file ) );
                else
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );   // File is not in user files space
            }
            else
            {
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );       // File does not exists
            }
        }
        
        return files;
    }
}