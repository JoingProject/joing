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
       implements ListManagerRemote, ListManagerLocal, Serializable
{
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public List<FileDescriptor> getChilds( String sSessionId, int nFileDirId )
           throws JoingServerVFSException
    {
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<FileDescriptor> files    = null;
            
        if( sAccount != null )
        {
            if( nFileDirId == 0 )    // Invoking is asking for root ("/")
            {
                // Makes nFileDirId to have the value of root
                Query query = em.createQuery( "SELECT f FROM FileEntity f"+
                                              " WHERE f.name = '/'"+
                                              "   AND f.account = '"+ sAccount +"'" );
                try
                {
                   nFileDirId = ((FileEntity) query.getSingleResult()).getIdFile();
                }
                catch( Exception exc )
                {
                    Constant.getLogger().throwing( getClass().getName(), "getChilds(String,int)", exc );
                    throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
                }
            }
            
            if( nFileDirId > 0 )
                files = _getChilds( sAccount, nFileDirId );
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
            
            FileEntity _file = (new FileManagerBean()).path2File( sAccount, sDirPath );

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
        StringBuilder sbQuery = new StringBuilder( 1024 );
                          sbQuery.append( "SELECT f FROM FileEntity f" )
                                 .append( " WHERE f.fileEntityPK.idParent = " ).append( nFileDirId )
                                 .append( "   AND f.account = '" ).append( sAccount ).append( '\'' )    // It is not necessary but enforces security
                                 .append( "   AND f.is_in_trashcan = 0" );
        try
        {
            Query query = em.createQuery( sbQuery.toString() );

            return fromEntity2DTO( (List<FileEntity>) query.getResultList() );
        }
        catch( RuntimeException exc )
        {
            /*                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "updateFile(...)", exc );
                    exc = new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
                }
                
                throw exc;*/
            Constant.getLogger().throwing( getClass().getName(), "_getChilds(...)", exc );
            throw new JoingServerVFSException( JoingServerVFSException.ACCESS_DB, exc );
        }
    }
    
    private List<FileDescriptor> fromEntity2DTO( List<FileEntity> fes )
    {
        List<FileDescriptor> files = new ArrayList<FileDescriptor>( fes.size() );
        
        for( FileEntity fe : fes )
            files.add( new FileDescriptor( fe )  );
        
        return files;
    }
}