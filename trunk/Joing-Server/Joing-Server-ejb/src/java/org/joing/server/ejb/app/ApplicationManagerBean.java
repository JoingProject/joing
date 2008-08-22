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
package org.joing.server.ejb.app;

import org.joing.server.ejb.Constant;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.server.ejb.session.SessionManagerLocal;
import org.joing.server.ejb.user.UserEntity;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import org.joing.common.dto.app.AppGroupKey;
import org.joing.common.exception.JoingServerAppException;
import org.joing.common.exception.JoingServerException;

/**
 * This class implements operations related with the applications that can be
 * ran by the users.
 * <p>
 * For the API javadoc, refer to the 'remote' and 'local' interfaces .
 *
 * @author Francisco Morero Peyrona
 */
@Stateless
public class ApplicationManagerBean
        implements ApplicationManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    private static final int APPS_ALL = 0;
    private static final int APPS_INSTALLED = 1;
    private static final int APPS_NOT_INSTALLED = 2;
    
    @Resource(name = "joing_db")
    private DataSource joing_db;
    @PersistenceContext
    private EntityManager em;
    @EJB
    private SessionManagerLocal sessionManagerBean;

    //------------------------------------------------------------------------//
    
    public List<AppGroup> getAvailableForUser( String sSessionId, AppGroupKey groupKey )
            throws JoingServerAppException
    {
        return getApplicationDescriptors( sSessionId, APPS_ALL, groupKey );
    }

    public List<AppGroup> getNotInstalledForUser( String sSessionId, AppGroupKey groupKey )
            throws JoingServerAppException
    {
        return getApplicationDescriptors( sSessionId, APPS_NOT_INSTALLED, groupKey );
    }

    public List<AppGroup> getInstalledForUser( String sSessionId, AppGroupKey groupKey )
            throws JoingServerAppException
    {
        return getApplicationDescriptors( sSessionId, APPS_INSTALLED, groupKey );
    }

    public boolean install( String sSessionId, AppDescriptor app )
            throws JoingServerAppException
    {
        return install( sSessionId, app, true );
    }

    public boolean uninstall( String sSessionId, AppDescriptor app )
            throws JoingServerAppException
    {
        return install( sSessionId, app, false );
    }

    public AppDescriptor getPreferredForType( String sSessionId, String sFileExtension )
            throws JoingServerAppException
    {
        String        sAccount      = sessionManagerBean.getUserAccount( sSessionId );
        AppDescriptor appDescriptor = null;

        if( sAccount != null && sFileExtension != null )
        {
            sFileExtension = sFileExtension.trim().toLowerCase();

            if( sFileExtension.charAt( 0 ) == '.' )
                sFileExtension = sFileExtension.substring( 1 );

            // TODO: Pensar cómo hacerlo (en la DB, en user_preferences, etc) e implementarlo
        }
        
        return appDescriptor;
    }

    public Application getApplication( String sSessionId, int nAppId )
            throws JoingServerAppException
    {
        Application app = null;
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            if( ! hasAccess( sAccount, nAppId ) )
            {
                throw new JoingServerAppException( JoingServerAppException.INVALID_OWNER );
            }

            try
            {
                Query q = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                      q.setParameter( "idApplication", nAppId );
                ApplicationEntity _app = (ApplicationEntity) q.getSingleResult();

                if( _app != null )
                {
                    app = AppDTOs.createApplication( _app );
                }
                else
                {
                    throw new JoingServerAppException( JoingServerAppException.APP_NOT_EXISTS );
                }
            }
            catch( RuntimeException exc )
            {
                if( !(exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "getApplication(...)", exc );
                    exc = new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
                }

                throw exc;
            }
        }

        return app;
    }

    public Application getApplicationByName( String sessionId, String executableName )
            throws JoingServerAppException
    {
        Application app = null;
        String account = sessionManagerBean.getUserAccount( sessionId );

        if( account != null )
        {
            Query q = null;

            try
            {
                q = em.createNamedQuery( "ApplicationEntity.findByExecutable" );
                q.setParameter( "executable", executableName );
                ApplicationEntity appEnt = (ApplicationEntity) q.getSingleResult();

                app = AppDTOs.createApplication( appEnt );
            }
            catch( NoResultException nre )
            {
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, nre );
            }

            // Para poder determinar el acceso necesito primero obtener el
            // id de app., y para ello ocupo obtener la app.
            if( ! hasAccess( account, app.getId() ) )
            {
                throw new JoingServerAppException( JoingServerAppException.INVALID_OWNER );
            }
        }
        
        return app;
    }

    //------------------------------------------------------------------------//
    // PRIVATES
    
    private List<AppGroup> getApplicationDescriptors( String sSessionId, int nInstallMode, AppGroupKey groupKey )
            throws JoingServerAppException
    {
        // TODO: Validate the case when this method is invoked with null 
        // sessionId.
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        List<AppGroup> lstGroups = new ArrayList<AppGroup>();

        if( sAccount != null )
        {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try
            {
                UserEntity _user = em.find( UserEntity.class, sAccount );
                AppGroupKey currentGroupKey = AppGroupKey.UNKNOWN;

                conn = joing_db.getConnection();
                stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY );
                rs = stmt.executeQuery( getQuery( sAccount, _user.getIdLocale().getIdLocale(), nInstallMode, groupKey ) );

                while( rs.next() )
                {
                    AppGroupKey theKeyInRS = AppGroupKey.inverse( rs.getInt( "GROUP_ID" ) );

                    if( currentGroupKey != theKeyInRS )
                    {
                        currentGroupKey = theKeyInRS;

                        AppGroup group = new AppGroup( currentGroupKey );
                                 group.setName( rs.getString( "GROUP_NAME" ) );
                                 group.setDescription( rs.getString( "GROUP_DESC" ) );
                        // TODO: añadirle los iconos Pixel y Vector al grupo

                        lstGroups.add( group );
                    }

                    Query query = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                          query.setParameter( "idApplication", rs.getInt( "APP_ID" ) );

                    AppDescriptor app = AppDTOs.createAppDescriptor( (ApplicationEntity) query.getSingleResult() );

                    lstGroups.get( lstGroups.size() - 1 ).addApplication( app );
                }
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getApplications(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }
            catch( SQLException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getApplications(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }
            finally
            {
                if( rs != null )
                {
                    try{ rs.close(); } catch( SQLException x ) { /* */ }
                }
                
                if( stmt != null )
                {
                    try{ stmt.close(); } catch( SQLException x ) { /* */ }
                }
                
                if( conn != null )
                {
                    try{ conn.close(); } catch( SQLException x ) { /* */ }
                }
            }
        }

        return lstGroups;
    }

    private String getQuery( String sAccount, int nLocaleId, int nInstallMode, AppGroupKey groupKey )
    {
        // NEXT: Añadir el "AllowRemoteExecution" ver tabla "USERS_WITH_APPS"        
        StringBuilder sbQuery = new StringBuilder( 1024 ).append(
                "SELECT APP_GROUP_DESCRIPTIONS.ID_APP_GROUP GROUP_ID," ).append(
                "       APP_GROUP_DESCRIPTIONS.GROUP_NAME   GROUP_NAME," ).append(
                "       APP_GROUP_DESCRIPTIONS.DESCRIPTION  GROUP_DESC," ).append(
                "       APPLICATIONS.ID_APPLICATION         APP_ID" ).append(
                "  FROM USERS_WITH_APPS, APPLICATIONS, APPS_WITH_GROUPS," ).append(
                "       APP_GROUPS, APP_GROUP_DESCRIPTIONS, LOCALES" ).append(
                " WHERE LOCALES.ID_LOCALE = " + nLocaleId ).append(
                "   AND USERS_WITH_APPS.ACCOUNT = '" + sAccount + "'" ).append(
                "   AND APPLICATIONS.ID_APPLICATION = USERS_WITH_APPS.ID_APPLICATION" ).append(
                "   AND APPS_WITH_GROUPS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION" ).append(
                "   AND APP_GROUPS.ID_APP_GROUP = APPS_WITH_GROUPS.ID_APP_GROUP" ).append(
                "   AND APP_GROUP_DESCRIPTIONS.ID_APP_GROUP = APP_GROUPS.ID_APP_GROUP" ).append(
                "   AND APP_GROUP_DESCRIPTIONS.ID_LOCALE = LOCALES.ID_LOCALE" );
                
        if( groupKey != AppGroupKey.ALL )
        {
            sbQuery.append( " AND APP_GROUPS.ID_APP_GROUP = " + groupKey.getIndex() );
        }

        /* TODO: Mirar cómo hacer esto, porque he quitado el campo IS_INSTALLED porque
         * debiera haber un modo más simple de hacerlo
        switch( nInstallMode )
        {
            case APPS_INSTALLED    : sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 1" ); break;
            case APPS_NOT_INSTALLED: sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 0" ); break;
            case APPS_ALL          : break;
        }*/
        
        sbQuery.append( " ORDER BY GROUP_ID" );
        
        return sbQuery.toString();
    }

    // To install and uninstall apps
    private boolean install( String sSessionId, AppDescriptor app, boolean bInstall )
            throws JoingServerAppException
    {
        boolean bSuccess = false;
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );

        if( sAccount != null )
        {
            /* TODO: Hacerlo: lo he quitado por que no tengo claro cómo hacerlo. Ver this::getQuery(...)
            Connection conn = null;
            Statement  stmt = null;

            try
            {
                Query query = em.createQuery( "SELECT a FROM Users_with_Apps a" +
                                              " WHERE a.name = :name AND a.version = :version" );
                query.setParameter( "name", app.getName() );
                query.setParameter( "version", app.getVersion() );

                ApplicationEntity _app = (ApplicationEntity) query.getSingleResult();

                conn = joing_db.getConnection();
                stmt = conn.createStatement();
                stmt.execute( "UPDATE USERS_WITH_APPS" +
                              "   SET IS_INSTALLED = " + (bInstall ? 1 : 0) +
                              " WHERE ACCOUNT = " + sAccount +
                              "   AND ID_APPLICATION = " + _app.getIdApplication() );

                bSuccess = true;
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "install(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }
            catch( SQLException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "install(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }
            finally
            {
                if( stmt != null )
                {
                    try{ stmt.close(); } catch( SQLException x ) {  }
                }
                
                if( conn != null )
                {
                    try{ conn.close(); } catch( SQLException x ) {  }
                }
            }*/
        }

        return bSuccess;
    }

    // Checks that user is the owner of the file (a security measure)
    private boolean hasAccess( String sAccount, int nIdApp )
            throws JoingServerAppException
    {
        // TODO: terminar este método
        /*boolean bHasAccess = false;
        try
        {
        Query query = em.createNativeQuery( "SELECT * FROM USERS_WITH_APPS "+
        " WHERE ACCOUNT = '"+ sAccount +"'"+
        "   AND ID_APPLICATION = "+ nIdApp );
        query.getSingleResult();
        }
        catch( NoResultException exc )    // Impossible to retun more than one result
        {
        // Nothing to do, bHasAccess is already false
        }
        catch( RuntimeException exc )
        {
        Constant.getLogger().throwing( getClass().getName(), "isOwnerOfFile(...)", exc );
        throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );    
        }
        return bHasAccess;*/
        return true;
    }

    public List<AppDescriptor> getAvailableDesktops()
            throws JoingServerAppException
    {
        // TODO: Arreglar esta marranada... :-(
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
//            Query q = 
//                    em.createNamedQuery("findByIdAppGroupAppGroupEntity.findByIdAppGroup");
//            q.setParameter("idAppGroup", AppGroupKey.DESKTOP);
//            AppGroupEntity appGrp = (AppGroupEntity)q.getSingleResult();
//
            List<AppDescriptor> apps = new ArrayList<AppDescriptor>();
//            
//            for (ApplicationEntity appEntity : appGrp.getApplications()) {
//                Application app = AppDTOs.createApplication(appEntity);
//                apps.add(app);
//            }
//            
//

            conn = this.joing_db.getConnection();
            // este query sql esta mal
            StringBuilder sql = new StringBuilder();
            sql.append( "SELECT APPLICATIONS.ID_APPLICATION AS idApp " );
            sql.append( "FROM APPLICATIONS,APPS_WITH_GROUPS " );
            sql.append( "WHERE APPS_WITH_GROUPS.ID_APP_GROUP = ? AND " );
            sql.append( "APPLICATIONS.ID_APPLICATION = APPS_WITH_GROUPS.ID_APPLICATION " );
            ps = conn.prepareStatement( sql.toString() );
            ps.setInt( 1, AppGroupKey.DESKTOP.getIndex() );

            rs = ps.executeQuery();

            while( rs.next() )
            {
                Integer appId = rs.getInt( "idApp" );
                Query q = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                q.setParameter( "idApplication", appId );
                ApplicationEntity entity = (ApplicationEntity) q.getSingleResult();
                AppDescriptor appdesc = AppDTOs.createAppDescriptor( entity );
                apps.add( appdesc );
            }

            return apps;

        }
        catch( Exception e )
        {
            throw new JoingServerAppException( e.getMessage(), e );
        }
        finally
        {
            if( rs != null )
            {
                try{ rs.close(); } catch( SQLException x ) { /* */ }
            }

            if( ps != null )
            {
                try{ ps.close(); } catch( SQLException x ) { /* */ }
            }

            if( conn != null )
            {
                try{ conn.close(); } catch( SQLException x ) { /* */ }
            }
        }
    }
}