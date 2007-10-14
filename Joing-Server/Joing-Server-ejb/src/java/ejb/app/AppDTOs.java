/*
 * AppDTOs.java
 *
 * Created on 10 de octubre de 2007, 19:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.app;

import ejb.Constant;
import ejb.JoingServerException;
import ejb.vfs.FileSystemTools;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Moves information from Application Entity to Aplication DTO class.
 * <br>
 * In this way, the DTO class does not need a reference to the Entity class and 
 * the size of the package needed by the Cliente side will be smaller.
 * <p>
 * Note: The opposite method does not exists because the Client can't change the 
 * Application properties.
 * <p>
 * Obviously this class has package scope.
 *
 * @author Francisco Morero Peyrona
 */
class AppDTOs
{
    static AppDescriptor createAppDescriptor( ApplicationEntity appEntity )
    {
        AppDescriptor appDTO = new AppDescriptor();
     
        transfer( appEntity, appDTO );
        
        return appDTO;
    }
    
    static Application createApplication( ApplicationEntity appEntity )
    {
        Application app = new Application();
        
        transfer( appEntity, app );
        
        if( app.getExecutable() != null )
            app.setContents( getContents( app.getExecutable() ) );
        
        return app;
    }
    
    /* This method does not exists because the Client can't change the App properties:
     * static ApplicationEntity createApplicationEntity( AppDescriptor appDTO )
     */
    
    //------------------------------------------------------------------------//
    
    private static void transfer( ApplicationEntity appEntity, AppDescriptor appDTO )
    {
        appDTO.setId( appEntity.getIdApplication() );
        appDTO.setName( appEntity.getApplication() );
        appDTO.setVersion( appEntity.getVersion() );
        appDTO.setExecutable( appEntity.getExecutable() );
        appDTO.setArguments( null );// FIXME: implementarlo --> appEntity.getArguments();
        appDTO.setIconPNG( appEntity.getIconPng() );
        appDTO.setIconSVG( appEntity.getIconSvg() );
        
        // Prepare file types
        ArrayList<String> lstFileTypes = new ArrayList<String>();        
        
        if( appEntity.getFileTypes() != null )
        {
            StringTokenizer st = new StringTokenizer( appEntity.getFileTypes(), ";" );
            
            while( st.hasMoreTokens() )
                lstFileTypes.add( st.nextToken() );
        }
          
        appDTO.setFileTypes( lstFileTypes );
    }
    
    // Application contents is sat by this method
    private static byte[] getContents( String sExec )
    {
        byte[] btContent = null;
        
        java.io.File fApp = FileSystemTools.getApplication( sExec );
        
        btContent = new byte[ (int) fApp.length() ];
        
        try
        {
            FileInputStream fis  = new FileInputStream( fApp );
                            fis.read( btContent, 0, btContent.length );
        }
        catch( RuntimeException exc )
        {
            if( ! (exc instanceof JoingServerException) )
            {
                Constant.getLogger().throwing( "Application", "setContents()", exc );
                exc = new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }

            throw exc;
        }
        catch( IOException exc )
        {
            Constant.getLogger().throwing( "Application", "setContents()", exc );
            throw new JoingServerAppException( JoingServerException.ACCESS_NFS, exc );
        }
        
        return btContent;
    }
}