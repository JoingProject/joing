/*
 * AppDTOs.java
 *
 * Created on 10 de octubre de 2007, 19:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.app;

import org.joing.common.JoingManifestEntry;
import ejb.Constant;
import ejb.vfs.NativeFileSystemTools;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.joing.common.dto.app.AppDescriptor;
import org.joing.common.dto.app.Application;
import org.joing.common.exception.JoingServerAppException;
import org.joing.common.exception.JoingServerException;

/**
 * Moves information from Application Entity to Aplication DTO classes.
 * <br>
 * In this way, DTO classes does not need a reference to the Entity class and 
 * the size of the package needed by the Cliente side will be smaller.
 * <p>
 * Note: The opposite method does not exists because the Client can't change 
 * Application's properties.
 * <p>
 * Obviously this class has package scope.
 *
 * @author Francisco Morero Peyrona
 */
// NEXT: Esta clase habría que hacerla con constructor en lugar de como métodos static para poder crear múltiples instancias y que el servidor escale mejor
class AppDTOs
{
    static synchronized AppDescriptor createAppDescriptor( ApplicationEntity appEntity )
    {
        AppDescriptor appDTO = new AppDescriptor();
        
        transfer( appEntity, appDTO );
        
        return appDTO;
    }
    
    static synchronized Application createApplication( ApplicationEntity appEntity )
    {
        Application app = new Application();
        
        transfer( appEntity, app );
        
        if( app.getExecutable() != null )
        {
            java.io.File fJAR = createFileForJAR( appEntity.getExtraPath(), appEntity.getExecutable() );
            app.setContents( getContents( fJAR ) );
        }
        
        return app;
    }
    
    /* This method does not exists because the Client can't change the App properties:
     * static ApplicationEntity createApplicationEntity( AppDescriptor appDTO )
     */
    
    //------------------------------------------------------------------------//
    
    private static void transfer( ApplicationEntity appEntity, AppDescriptor appDTO )
    {
        String sExecutable = appEntity.getExecutable();
        
        if( sExecutable.toUpperCase().endsWith( ".JAR" ) )
        {
            JarFile jar = null;
            
            try
            {
                java.io.File       fJAR = createFileForJAR( appEntity.getExtraPath(), appEntity.getExecutable() );
                                   jar  = new JarFile( fJAR );
                JoingManifestEntry jm   = new JoingManifestEntry( jar.getManifest() );
                
                String sAppName = jm.getAppName();
                
                if( sAppName == null )     // Can't be null
                {
                    sAppName = fJAR.getName();
                
                    if( sAppName.indexOf( '.' ) > -1 )
                        sAppName = sAppName.substring( 0, sAppName.indexOf( '.' ) );
                }
                
                appDTO.setName( sAppName );
                appDTO.setVersion( jm.getVersion() );
                appDTO.setIconPixel( readBinaryFileFromJAR( jar, jm.getIconPixel() ) );
                appDTO.setIconVector( readBinaryFileFromJAR( jar, jm.getIconVector() ) );
                appDTO.setDescription( jm.getDescription() );
                appDTO.setArguments( jm.getArguments() );
                appDTO.setFileTypes( jm.getFileTypes() );
                appDTO.setAuthor( jm.getAuthor() );
                appDTO.setVendor( jm.getVendor() );
                appDTO.setLicense( jm.getLicense() );
            }
            catch( IOException exc )
            {
                Constant.getLogger().throwing( AppDTOs.class.getName(), "transfer(...)", exc );
                throw new JoingServerAppException( JoingServerException.ACCESS_NFS, exc );
            }
            finally
            {
                if( jar != null )
                    try{ jar.close(); }catch( IOException e ) { /* Nothing to do */ }
            }
        }
        
        appDTO.setId( appEntity.getIdApplication() );
        appDTO.setExecutable( appEntity.getExecutable() );
    }
    
    private static java.io.File createFileForJAR( String sExtraPath, String sExec )
    {
        if( sExtraPath != null && sExtraPath.length() > 0 )
        {
            // Removes leading and trailing "/"
            if( sExtraPath.charAt( 0 ) == '/' )
                sExtraPath = sExtraPath.substring( 1 );
            
            if( sExtraPath.charAt( sExtraPath.length() - 1 ) == '/' )
                sExtraPath = sExtraPath.substring( 0, sExtraPath.length() - 1 );
            
            sExec = sExtraPath +'/'+ sExec;
        }
        
        return NativeFileSystemTools.getApplication( sExec );
    }
    
    // Application contents is sat by this method
    private static byte[] getContents( java.io.File fJAR )
    {
        byte[] btContent = new byte[ (int) fJAR.length() ];
        
        try
        {
            FileInputStream fis  = new FileInputStream( fJAR );
                            fis.read( btContent, 0, btContent.length );
        }
        catch( RuntimeException exc )
        {
            if( ! (exc instanceof JoingServerException) )
            {
                Constant.getLogger().throwing( AppDTOs.class.getName(), "setContents()", exc );
                exc = new JoingServerAppException( JoingServerException.ACCESS_DB, exc );
            }

            throw exc;
        }
        catch( IOException exc )
        {
            Constant.getLogger().throwing( AppDTOs.class.getName(), "setContents()", exc );
            throw new JoingServerAppException( JoingServerException.ACCESS_NFS, exc );
        }
        
        return btContent;
    }
    
    private static byte[] readBinaryFileFromJAR( JarFile jar, String sEntry )
    {
        byte[] anRet = null;
        
        if( sEntry != null )
        {
            try
            {
                JarEntry              entry  = jar.getJarEntry( sEntry );
                InputStream           input  = jar.getInputStream( entry );
                ByteArrayOutputStream output = new ByteArrayOutputStream( 1024*8 );
                
                byte[] buffer = new byte[1024];
                int    bytes;
                
                while( (bytes = input.read( buffer )) > 0 )
                    output.write( buffer, 0, bytes );
                
                input.close();
                anRet = output.toByteArray();
            }
            catch( IOException exc )
            {
                // Nothing to do: returning null
            }
        }
        
        return anRet;
    }
}