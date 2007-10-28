/*
 * ApplicationManagerBean.java
 *
 * Created on 18 de mayo de 2007, 18:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.app;

import ejb.Constant;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.AppEnvironment;
import org.joing.common.dto.app.Application;
import org.joing.common.dto.app.AppGroup;
import org.joing.common.exception.JoingServerAppException;
import ejb.session.SessionManagerLocal;
import ejb.user.UserEntity;
import java.io.Serializable;
import java.sql.Connection;
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
       implements ApplicationManagerRemote, ApplicationManagerLocal, Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private static final int APPS_ALL           = 0;
    private static final int APPS_INSTALLED     = 1;
    private static final int APPS_NOT_INSTALLED = 2;
    
    @Resource(name = "joing_db")
    private DataSource joing_db;
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private SessionManagerLocal sessionManagerBean;
    
    //------------------------------------------------------------------------//
    
    public List<AppGroup> getAvailableForUser( String sSessionId, int nEnviron, int nGroup )
           throws JoingServerAppException
    {   
        return getApplicationDescriptors( sSessionId, APPS_ALL, nEnviron, nGroup );
    }
    
    public List<AppGroup> getNotInstalledForUser( String sSessionId, int nEnviron, int nGroup )
           throws JoingServerAppException
    {
        return getApplicationDescriptors( sSessionId, APPS_NOT_INSTALLED, nEnviron, nGroup );
    }
    
    public List<AppGroup> getInstalledForUser( String sSessionId, int nEnviron, int nGroup )
           throws JoingServerAppException
    {
        return getApplicationDescriptors( sSessionId, APPS_INSTALLED, nEnviron, nGroup );
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
        String sAccount = sessionManagerBean.getUserAccount( sSessionId );
        AppDescriptor appDescriptor = null;
        
        if( sAccount != null && sFileExtension != null )
        {            
            sFileExtension = sFileExtension.trim().toLowerCase();

            if( sFileExtension.charAt( 0 ) == '.' )
                sFileExtension = sFileExtension.substring( 1 );
            
            try
            {
                Query query;
                
                // Finds the AppPreferredEntity instance associated with the file extension
                query = em.createNamedQuery( "AppPreferredEntity.findByFileExtension" );
                query.setParameter( "fileExtension", sFileExtension );
                
                AppPreferredEntity _appPref = (AppPreferredEntity) query.getSingleResult();
                
                // Finds the ApplicationEntity assoacited with the AppPreferredEntity
                query = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                query.setParameter( "idApplication", _appPref.getIdApplication() );
                
                ApplicationEntity _app = (ApplicationEntity) query.getSingleResult();
                
                // Creates the Application Descriptor instance based on the ApplicationEntity
                appDescriptor = AppDTOs.createAppDescriptor( _app );
            }
            catch( NoResultException exc )
            {
                // Nothing to do: app is already equals to null
            }
            catch( RuntimeException exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getPreferredForType(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }
        }
        
        return appDescriptor;
    }
    
    public Application getApplication( String sSessionId, int nAppId ) 
           throws JoingServerAppException
    {
        Application app      = null;
        String      sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            if( ! hasAccess( sAccount, nAppId ) )
                throw new JoingServerAppException( JoingServerAppException.INVALID_OWNER );
            
            try
            {
                Query q = em.createNamedQuery("ApplicationEntity.findByIdApplication");
                      q.setParameter("idApplication", nAppId);
                ApplicationEntity _app = (ApplicationEntity) q.getSingleResult();
                
                if( _app != null )
                    app = AppDTOs.createApplication( _app );
                else
                    throw new JoingServerAppException( JoingServerAppException.APP_NOT_EXISTS );
            }
            catch( RuntimeException exc )
            {
                if( ! (exc instanceof JoingServerException) )
                {
                    Constant.getLogger().throwing( getClass().getName(), "getApplication(...)", exc );
                    exc = new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
                }
                
                throw exc;
            }
        }
        
        return app;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATES
    
    private List<AppGroup> getApplicationDescriptors( String sSessionId, int nInstallMode,
                                                      int nEnviron, int nGroup )
            throws JoingServerAppException
    {
        String         sAccount  = sessionManagerBean.getUserAccount( sSessionId );
        List<AppGroup> lstGroups = new ArrayList<AppGroup>();
        
        if( sAccount != null )
        {
            Connection conn = null; 
            Statement  stmt = null;
            ResultSet  rs   = null;
            
            try
            {
                UserEntity _user         = em.find( UserEntity.class, sAccount );
                int        nCurrentGroup = AppGroup.UNKNOWN;
                
                conn = joing_db.getConnection(); 
                stmt = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY );
                rs   = stmt.executeQuery( getQuery( sAccount, _user.getIdLocale().getIdLocale(),
                                                    nInstallMode, nEnviron, nGroup ) );
                
                while( rs.next() )
                {
                    if( nCurrentGroup != rs.getInt( "GROUP_ID" ) )
                    {
                        nCurrentGroup = rs.getInt( "GROUP_ID" );
                        
                        AppGroup group = new AppGroup( nCurrentGroup );
                                 group.setName( rs.getString( "GROUP_NAME" ) );
                                 group.setDescription( rs.getString( "GROUP_DESC" ) );
                                 // TODO: añadirle los iconos PNG y SVG
                                 
                        lstGroups.add( group );   
                    }
                    
                    Query query = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                          query.setParameter( "idApplication", rs.getInt( "APP_ID" ) );
                          
                    AppDescriptor app = AppDTOs.createAppDescriptor( (ApplicationEntity) query.getSingleResult() );
                                  app.setDescription( rs.getString( "APP_DESC" ) );
                    
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
                try{ if(rs   != null) rs.close();   } catch( SQLException x ){ /* */ }
                try{ if(stmt != null) stmt.close(); } catch( SQLException x ){ /* */ }
                try{ if(conn != null) conn.close(); } catch( SQLException x ){ /* */ }
            }
        }
        
        return lstGroups;
    }
    
    private String getQuery( String sAccount, int nLocaleId, int nInstallMode, int nEnviron, int nGroup )
    { // NEXT: Añadir el "AllowRemoteExecution" ver tabla "USERS_WITH_APPS"
        StringBuilder sbQuery = new StringBuilder( 1024 ).append(
            "SELECT APP_GROUP_DESCRIPTIONS.APP_GROUP_ID GROUP_ID,"                 ).append(
            "       APP_GROUP_DESCRIPTIONS.GROUP_NAME   GROUP_NAME,"               ).append(
            "       APP_GROUP_DESCRIPTIONS.DESCRIPTION  GROUP_DESC,"               ).append(
            "       APPLICATIONS.ID_APPLICATION         APP_ID,"                   ).append(
            "       APPLICATIONS.APPLICATION            APP_NAME,"                 ).append(
            "       APP_DESCRIPTIONS.DESCRIPTION        APP_DESC"                  ).append(
            "  FROM USERS_WITH_APPS, APPLICATIONS, APP_DESCRIPTIONS,"              ).append(
            "       APPS_WITH_GROUPS, APP_GROUPS, APP_GROUP_DESCRIPTIONS, LOCALES" ).append(
            " WHERE LOCALES.ID_LOCALE = "+ nLocaleId                               ).append(
            "   AND USERS_WITH_APPS.ACCOUNT = '"+ sAccount +"'"                    ).append(
            "   AND APPLICATIONS.ID_APPLICATION = USERS_WITH_APPS.ID_APPLICATION"  ).append(
            "   AND APP_DESCRIPTIONS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION" ).append(
            "   AND APP_DESCRIPTIONS.ID_LOCALE = LOCALES.ID_LOCALE"                ).append(
            "   AND APPS_WITH_GROUPS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION" ).append(
            "   AND APP_GROUPS.ID_APP_GROUP = APPS_WITH_GROUPS.ID_APP_GROUP"       ).append(
            "   AND APP_GROUP_DESCRIPTIONS.ID_APP_GROUP = APP_GROUPS.ID_APP_GROUP" ).append(
            "   AND APP_GROUP_DESCRIPTIONS.ID_LOCALE = LOCALES.ID_LOCALE" );
        
        if( nEnviron != AppEnvironment.JAVA_ALL )
            sbQuery.append( " AND APPLICATIONS.ENVIRONMENT = "+ nEnviron );
        
        if( nGroup != AppGroup.ALL )
            sbQuery.append( " AND APP_GROUPS.APP_GROUP_ID = "+ nGroup );
                    
        switch( nInstallMode )
        {
            case APPS_INSTALLED    : sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 1" ); break;
            case APPS_NOT_INSTALLED: sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 0" ); break;
            case APPS_ALL          : break;
        }
        
        sbQuery.append( " ORDER BY GROUP_ID, APP_NAME" );
        
        return sbQuery.toString();
    }
    
    // To install and uninstall apps
    private boolean install( String sSessionId, AppDescriptor app, boolean bInstall )
            throws JoingServerAppException
    {
        boolean bSuccess = false;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            Connection conn = null;
            Statement  stmt = null;
            
            try
            {
                Query query = em.createQuery( "SELECT a FROM Users_with_Apps a"+
                                              " WHERE a.name = :name AND a.version = :version" );
                      query.setParameter( "name"   , app.getName()    );
                      query.setParameter( "version", app.getVersion() );
                      
                ApplicationEntity _app = (ApplicationEntity) query.getSingleResult();
                
                conn = joing_db.getConnection();
                stmt = conn.createStatement();
                stmt.execute( "UPDATE USERS_WITH_APPS"+
                              "   SET IS_INSTALLED = "+ (bInstall ? 1 : 0) +
                              " WHERE ACCOUNT = "+ sAccount +
                              "   AND ID_APPLICATION = "+ _app.getIdApplication() );
                
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
                try{ if(stmt != null) stmt.close(); }catch( SQLException x ){ /* */ }
                try{ if(conn != null) conn.close(); }catch( SQLException x ){ /* */ }
            }
        }
        
        return bSuccess;
    }
    
     // Checks that the user is the owner of the file (a security measure)
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
}