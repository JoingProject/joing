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
package ejb;

import java.io.File;
import java.io.InputStream;
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
    private static String sSysName;    // System name (decided by Join'g provider)
    private static File   fBaseDir;    // Joing base directory
    private static File   fAppDir;     // Dir for all apps for all users (relative to base dir)
    private static File   fUserDir;    // Dir for all users home dirs (relative to base dir)
    private static URL    emailServer;
    private static long   nSessionTimeOut;
    
    //-----------------------------
    
    private static final String sSYSTEM_NAME     = "system_name";
    private static final String sBASE_DIR        = "base_dir";
    private static final String sEMAIL_SRV       = "email_server";
    private static final String sSESSION_TIMEOUT = "session_timeout";
    
    //------------------------------------------------------------------------//
    
    static
    {
        init();
    }
    
    //------------------------------------------------------------------------//
    
    /**
     * 
     * @return
     */
    public static String getVersion()
    {
        return sVersion;
    }
    
    /**
     * Return Join'g system name: name given by Join'g provider.
     * For example: joing.peyrona.com
     * 
     * @return Return Join'g system name: name given by Join'g provider.
     */
    public static String getSystemName()
    {
        return sSysName;
    }
    
    /**
     * Return "system@" + getSystemName().
     * 
     * @return "system@" + getSystemName()
     */
    public static String getSystemAccount()
    {
        return "system@" + getSystemName();
    }
    
    /**
     * Base directory for Join'g: users file spaces and applications.
     * 
     * @return Base directory for Join'g.
     */
    public static File getBaseDir()
    {
        return fBaseDir;
    }
    
    /**
     * Users directory inside Join'g base direcotry.
     * 
     * @return Users directory inside Join'g base direcotry.
     */
    public static File getUserDir()
    {
        return fUserDir;
    }
    
    /**
     * Applications directory inside Join'g base direcotry.
     * 
     * @return Applications directory inside Join'g base direcotry.
     */
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
    
    /**
     * Reserved accounts that could be used in the future by the Server
     */
    public static String[] getReservedAccounts()
    {
        String[] asReserved = {"system", "Joing", "Join'g", "admin", "administrator", getSystemName()};
        
        return asReserved;
    }
    
    //------------------------------------------------------------------------//
    
    // Create default Properties
    private static void init()
    {
        long nTimeOut = 12 * 60 * 60 * 1000; // Default == 12 hrs
        
        // Loading values from properties file
        Properties props = new Properties();
        
        try
        {
            ClassLoader classLoader = Constant.class.getClassLoader();
            InputStream is = classLoader.getResourceAsStream("joing-server.properties");
            props.load( is );
            is.close();
        }
        catch( Exception exc )
        {
            String sHome = System.getProperty( "user.home" );
            char   cDir  = File.separatorChar;
            
            if( sHome.charAt( sHome.length() - 1 ) != cDir )
                sHome += cDir;
            
            // Initialise properties instance with default values
            props.setProperty( sSYSTEM_NAME    , "joing.org" );
            props.setProperty( sBASE_DIR       , cDir +"joing" );
            props.setProperty( sEMAIL_SRV      , "localhost" );
            props.setProperty( sSESSION_TIMEOUT, Long.toString(nTimeOut) );
        }
        
        sVersion = "0.1"; // It's better to hardcode this property than to store it in a file
        sSysName = props.getProperty( sSYSTEM_NAME );
        fBaseDir = new File( props.getProperty( sBASE_DIR ) );
        fUserDir = new File( fBaseDir, "users" );
        fAppDir  = new File( fBaseDir, "apps" );
        
        if( ! fBaseDir.exists() )
            fBaseDir.mkdirs();
        
        if( ! fAppDir.exists() )
            fAppDir.mkdirs();
        
        if( ! fUserDir.exists() )
            fUserDir.mkdirs();
        
        try
        {
            emailServer = new URL(props.getProperty(sEMAIL_SRV));
        }
        catch (MalformedURLException exc)
        {
            emailServer = null;
        }
        
        try
        {
            nSessionTimeOut = Long.parseLong(props.getProperty(sSESSION_TIMEOUT)) * 1000; // in milliseconds
        }
        catch (NumberFormatException exc)
        {
            nSessionTimeOut = nTimeOut;
        }
    }
}