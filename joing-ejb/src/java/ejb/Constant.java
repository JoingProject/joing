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
    private static File   fBaseDir;         // Joing base directory
    private static File   fAppDir;          // Dir for all apps for all users
    private static File   fUserDir;         // Dir for all users home dirs
    private static URL    emailServer;
    private static long   nSessionTimeOut;
    
    //-----------------------------
    private static final String sBASE_DIR        = "base_dir";
    private static final String sAPP_DIR         = "app_dir";
    private static final String sUSER_DIR        = "user_dir";
    private static final String sEMAIL_SRV       = "email_server";
    private static final String sSESSION_TIMEOUT = "session_timeout";
    
    //------------------------------------------------------------------------//
    
    static
    {
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
            props.setProperty( sBASE_DIR       , "/home/fmorero/Joing_App" );
            props.setProperty( sAPP_DIR        , "apps"                    );
            props.setProperty( sUSER_DIR       , "users"                   );
            props.setProperty( sEMAIL_SRV      , "localhost"               );
            props.setProperty( sSESSION_TIMEOUT, Long.toString( 12*60*60 ) );   // 12 hrs
        }
        
        sVersion = "0.0";   // It is better to hardcode this property than store it in a file
        fBaseDir = new File( props.getProperty( sBASE_DIR ) );
        fUserDir = new File( fBaseDir, props.getProperty( sUSER_DIR ) );
        fAppDir  = new File( fBaseDir, props.getProperty( sUSER_DIR ) );
        
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
        }
        
        try
        {
            nSessionTimeOut = Long.parseLong( props.getProperty( sSESSION_TIMEOUT ) ) * 1000;   // in milliseconds
        }
        catch( NumberFormatException exc ) 
        {
            nSessionTimeOut = 12 * 60 * 60 * 1000; 
        }
    }
    
    //------------------------------------------------------------------------//
    
    public static String getVersion()
    {
        return sVersion;
    }
    
    public static File getBaseDir()
    {
        return fBaseDir;
    }
    
    public static File getUserDir()
    {
        return fUserDir;
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
}