/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * This class reads from an application Manifest file all entries needed by
 * <code>ApplicationDescriptor</code> that are not stored in DB.
 * <p>
 * This class has package scope because has not to be used outside.
 * @author Francisco Morero Peyrona
 */
class JoingManifest
{
    // Join'g Entry Attributes
    private static final String          JOING_ENTRY = "Join'g";
    private static final Attributes.Name APP_NAME    = new Attributes.Name( "AppName"     );
    private static final Attributes.Name VERSION     = new Attributes.Name( "Version"     );
    private static final Attributes.Name ICON_PIXEL  = new Attributes.Name( "IconPixel"   );
    private static final Attributes.Name ICON_VECTOR = new Attributes.Name( "IconVector"  );
    private static final Attributes.Name DESCRIPTION = new Attributes.Name( "Description" );
    private static final Attributes.Name ARGUMENTS   = new Attributes.Name( "Arguments"   ); 
    private static final Attributes.Name FILE_TYPES  = new Attributes.Name( "FileTypes"   );
    private static final Attributes.Name AUTHOR      = new Attributes.Name( "Author"      );
    private static final Attributes.Name VENDOR      = new Attributes.Name( "Vendor"      );
    private static final Attributes.Name LICENSE     = new Attributes.Name( "License"     );
    
    // Join'g Entry Attributes
    private String sAppName     = null;
    private String sVersion     = null;
    private String sIconPixel   = null;
    private String sIconVector  = null;
    private String sDescription = null;
    private String sArguments   = null; 
    private String sFileTypes   = null;
    private String sAuthor      = null;
    private String sVendor      = null;
    private String sLicense     = null;
    
    //------------------------------------------------------------------------//
    
    JoingManifest( JarFile jarfile ) throws IOException
    {
        Manifest   manifest = jarfile.getManifest();
        Attributes attrs    = manifest.getAttributes( JOING_ENTRY );
        
        if( attrs != null )
        {
            sAppName     = attrs.getValue( APP_NAME    );
            sVersion     = attrs.getValue( VERSION     );
            sIconPixel   = attrs.getValue( ICON_PIXEL  );
            sIconVector  = attrs.getValue( ICON_VECTOR );
            sDescription = attrs.getValue( DESCRIPTION );
            sArguments   = attrs.getValue( ARGUMENTS   );
            sFileTypes   = attrs.getValue( FILE_TYPES  );
            sAuthor      = attrs.getValue( AUTHOR      );
            sVendor      = attrs.getValue( VENDOR      );
            sLicense     = attrs.getValue( LICENSE     );

            if( sAppName     != null && sAppName.length() == 0 )     sAppName     = null;
            if( sVersion     != null && sVersion.length() == 0 )     sVersion     = null;
            if( sIconPixel   != null && sIconPixel.length() == 0 )   sIconPixel   = null;
            if( sIconVector  != null && sIconVector.length() == 0 )  sIconVector  = null;
            if( sDescription != null && sDescription.length() == 0 ) sDescription = null;
            if( sArguments   != null && sArguments.length() == 0 )   sArguments   = null;
            if( sFileTypes   != null && sFileTypes.length() == 0 )   sFileTypes   = null;
            if( sAuthor      != null && sAuthor.length() == 0 )      sAuthor      = null;
            if( sVendor      != null && sVendor.length() == 0 )      sVendor      = null;
            if( sLicense     != null && sLicense.length() == 0 )     sLicense     = null;
        }
    }
    
    public String getIconPixel()
    {
        return sIconPixel;
    }

    public String getIconVector()
    {
        return sIconVector;
    }

    public String getAppName()
    {
        return sAppName;
    }

    public String[] getArguments()
    {
        String[] asRet = new String[0];
        
        if( sArguments != null )
        {
            List<String>    lst = new ArrayList<String>();
            StringTokenizer st  = new StringTokenizer( sArguments );
        
            while( st.hasMoreTokens() )
                lst.add( st.nextToken() );
            
            asRet = lst.toArray( asRet );
        }
        
        return asRet;
    }
    
    public String getAuthor()
    {
        return sAuthor;
    }

    public String getDescription()
    {
        return sDescription;
    }

    public List<String> getFileTypes()
    {
        List<String> lst = new ArrayList<String>();
        
        if( sFileTypes != null )
        {
            StringTokenizer st  = new StringTokenizer( sFileTypes, ";" );
            
            while( st.hasMoreTokens() )
                lst.add( st.nextToken() );
        }
        
        return lst;
    }

    public String getLicense()
    {
        return sLicense;
    }

    public String getVendor()
    {
        return sVendor;
    }

    public String getVersion()
    {
        return sVersion;
    }
}