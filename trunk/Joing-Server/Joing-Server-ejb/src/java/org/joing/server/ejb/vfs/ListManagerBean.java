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
package org.joing.server.ejb.vfs;

import org.joing.server.ejb.Constant;
import org.joing.server.ejb.session.SessionManagerLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joing.common.dto.vfs.VFSFileBase;
import org.joing.common.exception.JoingServerVFSException;

/**
 * Functionality related with exploring the Virtual File System (VFS).
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces.
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class ListManagerBean 
       implements ListManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
     
    //------------------------------------------------------------------------//
    // REMOTE INTERFACE
    
    public List<VFSFileBase> getRoots( String sSessionId )
            throws JoingServerVFSException
    {
        String            sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<VFSFileBase> roots    = null;
            
        if( sAccount != null )
        {
            // NEXT: mejorarlo buscando en otras comunidades donde esté el user
            FileEntity _file = VFSTools.path2File( em, sAccount, "/" );
            
            roots = new ArrayList<VFSFileBase>();
            roots.add( (new FileDTOs( _file )).createVFSFileBase() );
        }
        
        return roots;
    }
    
    public List<VFSFileBase> getChilds( String sSessionId, int nFileDirId )
           throws JoingServerVFSException
    {
        List<VFSFileBase> files    = null;
        String            sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            FileEntity _file = em.find( FileEntity.class, nFileDirId );
            
            if( _file != null )
            {
                if( _file.getAccount().equals( sAccount ) )
                    files = fromEntity2DTO( VFSTools.getChildren( em, sAccount, _file ) );
                else
                    throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );  // File is not in user files space
            }
            else
            {
                throw new JoingServerVFSException( JoingServerVFSException.FILE_NOT_EXISTS );      // File does not exists
            }
        }
        
        return files;
    }
    
    public List<VFSFileBase> getByNotes( String sSessionId, String sSubString, boolean bGlobal )
           throws JoingServerVFSException
    {// TODO: Implemetar el bGlobal (ver javadoc)
        String            sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<VFSFileBase> files    = null;
        
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
    
    public List<VFSFileBase> getTrashCan( String sSessionId )
           throws JoingServerVFSException
    {
        String               sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<VFSFileBase> files    = null;
        
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
    
    private List<VFSFileBase> fromEntity2DTO( List<FileEntity> fes )
    {
        List<VFSFileBase> files = new ArrayList<VFSFileBase>( fes.size() );
        
        for( FileEntity fe : fes )
            files.add( (new FileDTOs( fe )).createVFSFileBase() );
        
        return files;
    }
}