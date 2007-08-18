/*
 * Constant.java
 *
 * Created on 18 de mayo de 2007, 8:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class defines a set of constants used by other classes.
 * <p>
 * Values are loaded from a properties file.
 *
 * @author Francisco Morero Peyrona
 */
public class Constant
{
    private static String sVersion;
    private static String sSysName;         // System name (decided by Join'g provider)
    private static File   fBaseDir;         // Joing base directory
    private static File   fAppDir;          // Dir for all apps for all users
    private static File   fUserDir;         // Dir for all users home dirs
    private static URL    emailServer;
    private static long   nSessionTimeOut;
    
    //-----------------------------
    private static final String sSYSTEM_NAME     = "system_name";  // The provider, vg. "joing.sun.com"
    private static final String sBASE_DIR        = "base_dir";
    private static final String sEMAIL_SRV       = "email_server";
    private static final String sSESSION_TIMEOUT = "session_timeout";
    
    //------------------------------------------------------------------------//
    
    static
    {
        init();
    }
    
    //------------------------------------------------------------------------//

    public static String getVersion()
    {
        return sVersion;
    }
    
    public static String getSystemName()
    {
        return sSysName;
    }
    
    public static File getBaseDir()
    {
        return fBaseDir;
    }
    
    public static File getUserDir()
    {
        return fUserDir;
    }
    
    public static File getAppDir()
    {
        return fAppDir;
    }
    
    public static URL getEmailServer()
    {
        return emailServer;
    }
    
    /**
     * Maximum inactivity time for a session before delete it
     * @return Maximum inactivity time in milliseconds
     */
    public static long getSessionTimeOut()
    {
        return nSessionTimeOut;
    }
    
    public static Logger getLogger()
    {
        return Logger.getLogger( "joing" );
    }
    
    //------------------------------------------------------------------------//
    
    private static void init()
    {
        long nTimeOut = 12 * 60 * 60 * 1000;    // Default == 12 hrs
        
        // Loading values from properties file
        Properties props = new Properties();
        
        try
        {
            FileInputStream in = new FileInputStream( "system.properties" );    
            props.load( in );
            in.close();
        }
        catch( Exception exc )
        {
            // Initialise properties instance with default values
            props.setProperty( sSYSTEM_NAME    , "joing.peyrona.com" );
            props.setProperty( sBASE_DIR       , "/home/fmorero/proyectos/Joing/base_dir" );
            props.setProperty( sEMAIL_SRV      , "localhost" );
            props.setProperty( sSESSION_TIMEOUT, Long.toString( nTimeOut ) );
        }
        
        sVersion = "0.0";      // It's better to hardcode this property than to store it in a file
        sSysName = props.getProperty( sSYSTEM_NAME );
        fBaseDir = new File( props.getProperty( sBASE_DIR ) );
        fUserDir = new File( fBaseDir, "users" );
        fAppDir  = new File( fBaseDir, "apps"  );
        
        if( ! fBaseDir.exists() )
            fBaseDir.mkdirs();
        
        if( ! fAppDir.exists() )
            fAppDir.mkdirs();
        
        if( ! fUserDir.exists() )
            fUserDir.mkdirs();
        
        try
        { 
            emailServer = new URL( props.getProperty( sEMAIL_SRV ) );
        }
        catch( MalformedURLException exc ) 
        {
            emailServer = null;
        }
        
        try
        {
            nSessionTimeOut = Long.parseLong( props.getProperty( sSESSION_TIMEOUT ) ) * 1000;   // in milliseconds
        }
        catch( NumberFormatException exc ) 
        {
            nSessionTimeOut = nTimeOut;
        }   
    }
}