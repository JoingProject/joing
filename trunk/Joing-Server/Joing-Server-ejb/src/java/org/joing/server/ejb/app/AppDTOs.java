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

import org.joing.common.JoingManifestEntry;
import org.joing.server.ejb.Constant;
import org.joing.server.ejb.vfs.NativeFileSystemTools;
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
            java.io.File fJAR = NativeFileSystemTools.getApplication( app.getExecutable(), app.getExtraPath() );
            app.setContents( getContents( fJAR ) );
        }
        
        return app;
    }
    
    /* Next line method does not exists because the Client can't change the App properties:
     * static ApplicationEntity createApplicationEntity( AppDescriptor appDTO )
     */
    
    //------------------------------------------------------------------------//
    
    private static void transfer( ApplicationEntity appEntity, AppDescriptor appDTO )
    {
        if( appEntity.getExecutable().toUpperCase().endsWith( ".JAR" ) )
        {
            JarFile jar = null;
            
            try
            {
                java.io.File       fJAR = NativeFileSystemTools.getApplication( appEntity.getExecutable(), appEntity.getExtraPath() );
                                   jar  = new JarFile( fJAR );
                JoingManifestEntry jm   = new JoingManifestEntry( jar.getManifest() );
                
                String sAppName = jm.getAppName();
                
                if( sAppName == null )     // Should not be null
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
        appDTO.setExtraPath( appEntity.getExtraPath() );
        appDTO.setExecutable( appEntity.getExecutable() );
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