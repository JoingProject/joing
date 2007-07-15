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
import ejb.session.SessionManagerLocal;
import ejb.user.UserEntity;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

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
       implements ApplicationManagerLocal, ApplicationManagerRemote, Serializable  // TODO hacer el serializable
{
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
    
    public List<AppsByGroup> getAvailableForUser( String sSessionId )
    {   
        return getApplications( sSessionId, APPS_ALL );
    }
    
    public List<AppsByGroup> getNotInstalledForUser( String sSessionId )
    {   
        return getApplications( sSessionId, APPS_NOT_INSTALLED );
    }
    
    public List<AppsByGroup> getInstalledForUser( String sSessionId )
    {
        return getApplications( sSessionId, APPS_INSTALLED );
    }
    
    public boolean install( String sSessionId, Application app )
    {
        return install( sSessionId, app, true );
    }
    
    public boolean uninstall( String sSessionId, Application app )
    {
        return install( sSessionId, app, false );
    }
    
    public Application getPreferredForType( String sSessionId, String sFileExtension )
    {
        if( sFileExtension == null )
            return null;
        
        String      sAccount    = sessionManagerBean.getUserAccount( sSessionId );
        Application application = null;
        
        if( sAccount != null )
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
                
                // Creates the Application instance based on the ApplicationEntity
                application = new Application( _app );
            }
            catch( NoResultException exc )
            {
                // Nothing to do: app is already equals to null
            }
            catch( Exception exc )
            {
javax.swing.JOptionPane.showMessageDialog( null, "exc = "+ exc );
                Constant.getLogger().throwing( getClass().getName(), "getPreferredForType(...)", exc );
            }
        }
        
javax.swing.JOptionPane.showMessageDialog( null, "App = "+ application );
        
        return application;
    }
    
    //------------------------------------------------------------------------//
    // PRIVATES
    
    private List<AppsByGroup> getApplications( String sSessionId, int nWhich )
    {
        List<AppsByGroup> apps     = null;
        String            sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                UserEntity _user = em.find( UserEntity.class, sAccount );
                Connection conn  = joing_db.getConnection(); 
                Statement  stmt  = conn.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY );
                ResultSet  rs    = stmt.executeQuery( getQuery( sAccount, 
                                                                _user.getIdLocale().getIdLocale(),
                                                                nWhich ) );
                String sGroup    = "";
                
                while( rs.next() )
                {
                    if( ! sGroup.equals( rs.getString( "GROUP_DESC" ) ) )
                    {
                        sGroup = rs.getString( "GROUP_DESC" );
                        apps.add( new AppsByGroup( sGroup ) );
                    }
                    
                    Query query = em.createNamedQuery( "ApplicationEntity.findByIdApplication" );
                          query.setParameter( "idApplication", rs.getInt( "ID_APPLICATION" ) );
                          
                    Application app = new Application( (ApplicationEntity) query.getSingleResult() );
                                app.setDescription( rs.getString( "APP_DESC" ) );
                        
                    apps.get( apps.size() - 1 ).addApplication( app );
                }
                
                rs.close();
                stmt.close();
                conn.close();
            }
            catch( Exception exc )
            {
                Constant.getLogger().throwing( getClass().getName(), "getApplications(...)", exc );
            }
        }
        
        return apps;
    }
    
    private String getQuery( String sAccount, int nLocaleId, int nWhich )
    {
        StringBuilder sbQuery = new StringBuilder( 
            "SELECT APP_GROUP_DESCRIPTIONS.DESCRIPTION GROUP_DESC," ).append(
            "       APPLICATIONS.ID_APPLICATION," ).append(
            "       APP_DESCRIPTIONS.DESCRIPTION APP_DESC" ).append(
            "  FROM USERS_WITH_APPS, APPLICATIONS, APP_DESCRIPTIONS," ).append(
            "       APPS_WITH_GROUPS, APP_GROUPS, APP_GROUP_DESCRIPTIONS, LOCALE" ).append(
            " WHERE LOCALE.ID_LOCALE = "+ nLocaleId  ).append(
            "   AND USERS_WITH_APPS.ACCOUNT = '"+ sAccount +"'" ).append(
            "   AND APPLICATIONS.ID_APPLICATION = USERS_WITH_APPS.ID_APPLICATION" ).append(
            "   AND APP_DESCRIPTIONS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION" ).append(
            "   AND APP_DESCRIPTIONS.ID_LOCALE = LOCALE.ID_LOCALE" ).append(
            "   AND APPS_WITH_GROUPS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION" ).append(
            "   AND APP_GROUPS.ID_APP_GROUP = APPS_WITH_GROUPS.ID_APP_GROUP" ).append(
            "   AND APP_GROUP_DESCRIPTIONS.ID_APP_GROUP = APP_GROUPS.ID_APP_GROUP" ).append(
            "   AND APP_GROUP_DESCRIPTIONS.ID_LOCALE = LOCALE.ID_LOCALE" );
        
        switch( nWhich )
        {
            case APPS_INSTALLED    : sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 1" ); break;
            case APPS_NOT_INSTALLED: sbQuery.append( " AND USERS_WITH_APPS.IS_INSTALLED = 0" ); break;
            case APPS_ALL          : break;
        }
        
        sbQuery.append( " ORDER BY APP_GROUP_DESCRIPTIONS.DESCRIPTION," ).append(
                        "          APP_DESCRIPTIONS.DESCRIPTION" );
        
        return sbQuery.toString();
    }
    
    // To install and uninstall apps
    private boolean install( String sSessionId, Application app, boolean bInstall )
    {
        boolean bSuccess = true;
        String  sAccount = sessionManagerBean.getUserAccount( sSessionId );
        
        if( sAccount != null )
        {
            try
            {
                Query query = em.createQuery( "SELECT a FROM Users_with_Apps a"+
                                                   " WHERE a.name = :name AND a.version = :version" );
                      query.setParameter( "name"   , app.getName()    );
                      query.setParameter( "version", app.getVersion() );
                      
                ApplicationEntity _app = (ApplicationEntity) query.getSingleResult();
                
                Connection conn = joing_db.getConnection();
                Statement  stmt = conn.createStatement();
                           stmt.execute( "UPDATE USERS_WITH_APPS"+
                                         "   SET IS_INSTALLED = "+ (bInstall ? 1 : 0) +
                                         " WHERE ACCOUNT = "+ sAccount +
                                         "   AND ID_APPLICATION = "+ _app.getIdApplication() );
            }
            catch( Exception exc )
            {
                bSuccess = false;
                Constant.getLogger().throwing( getClass().getName(), "executeUpdate(...)", exc );
            }
        }
        
        return bSuccess;
    }
    
//SELECT
//       APP_GROUP_DESCRIPTIONS.DESCRIPTION GROUP_DESC, APPLICATIONS.ID_APPLICATION, 
//       APP_DESCRIPTIONS.DESCRIPTION APP_DESC
//  FROM
//       USERS_WITH_APPS, APPLICATIONS, APP_DESCRIPTIONS, APPS_WITH_GROUPS, 
//       APP_GROUPS, APP_GROUP_DESCRIPTIONS, LOCALES
//
// WHERE
//       LOCALES.ID_LOCALE = 2
//   AND USERS_WITH_APPS.ACCOUNT = 'peyrona'
//   AND USERS_WITH_APPS.IS_INSTALLED = 1
//   AND APPLICATIONS.ID_APPLICATION = USERS_WITH_APPS.ID_APPLICATION
//   AND APP_DESCRIPTIONS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION
//   AND APP_DESCRIPTIONS.ID_LOCALE = LOCALES.ID_LOCALE
//   AND APPS_WITH_GROUPS.ID_APPLICATION = APPLICATIONS.ID_APPLICATION
//   AND APP_GROUPS.ID_APP_GROUP = APPS_WITH_GROUPS.ID_APP_GROUP
//   AND APP_GROUP_DESCRIPTIONS.ID_APP_GROUP = APP_GROUPS.ID_APP_GROUP
//   AND APP_GROUP_DESCRIPTIONS.ID_LOCALE = LOCALES.ID_LOCALE
//
//ORDER BY APP_GROUP_DESCRIPTIONS.DESCRIPTION, APP_DESCRIPTIONS.DESCRIPTION
}