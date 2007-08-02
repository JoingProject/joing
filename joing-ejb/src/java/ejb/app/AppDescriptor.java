/*
 * Application.java
 *
 * Created on 18 de mayo de 2007, 17:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This calss is the DTO to represent instances of ApplicationEntity
 * 
 * Note: obviously, this class is "read-only", because applications' 
 * information can not be changed by users. 
 * In other words, it does not have a <code>update( Application _app )</code> 
 * method.
 * 
 * @author Francisco Morero Peyrona
 */
public class AppDescriptor implements Serializable
{
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private int          id;
    private String       name;        // These two fields form the PK
    private String       version;     // These two fields form the PK
    
    private String       executable;
    private byte[]       iconPNG;
    private byte[]       iconSVG;
    private List<String> fileTypes;
    private String       description;
    
    private boolean allowRemote;      // Used by ApplicationManagerBean class
    
    /**
     * Creates a new instance of Application
     */
    AppDescriptor( ApplicationEntity _app )
    {
        id         = _app.getIdApplication();
        name       = _app.getApplicationEntityPK().getName();
        version    = _app.getApplicationEntityPK().getVersion();
        executable = _app.getExecutable();
        iconPNG    = _app.getIconPng();
        iconSVG    = _app.getIconSvg();
        fileTypes  = new ArrayList<String>();  // Good practice: empty list instead of null
        
        // Prepare file types
        if( _app.getFileTypes() != null )
        {
            StringTokenizer st = new StringTokenizer( _app.getFileTypes(), ";" );
            
            while( st.hasMoreTokens() )
                fileTypes.add( st.nextToken() );
        }
    }

    /**
     * Return the id of the application.
     *
     * @return The id of the application.
     */
    public int getId()
    {
        return id;
    }    
    
    /**
     * Return the name of the application.
     *
     * @return The name of the application.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return the version of the application.
     *
     * @return The version of the application.
     */
    public String getVersion()
    {
        return version;
    }
    
    /**
     * Return the name of the executable file for this application.
     *
     * @return The name of the executable file.
     */
    public String getExecutable()
    {
        return executable;
    }
    
    /**
     * Return the 24x24 pixels PNG icon for this application.
     * 
     * @return The PNG icon for this application.
     */
    // Nota: prefiero mandar los bytes tal cual y que el cliente decida 
    // qué tipo de icono desea crear con ellos.
    // Además esto es más rápido porque no hay que serializar la clase
    // ImageIcon u otra similar.
    // En este caso no sería una buena práctica devolver un array vacío si el 
    // dato es null, porque null significa que no hay imagen, además, el array
    // no se utiliza para recorerlo, sino como una estructura.
    public byte[] getPNGIcon()
    {
        if( iconPNG == null )
        {
            return new byte[0];
        }
        else
        {   // Defensive copy
            byte[] ret = new byte[ iconPNG.length ];
            System.arraycopy( iconPNG, 0, ret, 0, iconPNG.length );
            return ret;
        }

    }
    
    /**
     * Return the SVG icon for this application.
     * 
     * @return The SVG icon for this application.
     */
    public byte[] getSVGIcon()
    {
        if( iconSVG == null )
        {
            return new byte[0];
        }
        else
        {   // Defensive copy
            byte[] ret = new byte[ iconSVG.length ];
            System.arraycopy( iconSVG, 0, ret, 0, iconSVG.length );
            return ret;
        }
    }
    
    public String[] getFileTypes()
    {
        return fileTypes.toArray( new String[ fileTypes.size() ] );   // Defensive copy
    }
    
    public String getDescription()
    {
        return ((description == null) ? "" : description);
    }
    
    public boolean isRemoteExecutionAllowed()
    {
        return allowRemote;
    }
    
    //------------------------------------------------------------------------//
    // PACKAGE SCOPE
    
    /**
     * Set if this application can or can not be executed remotely (in the 
     * WebPC Server).
     * <p>
     * Note: this field is package scope -> client apps can't modify it
     * @param description The description for this application.
     */
    void setRemoteExecutionAllowed( boolean allowRemote )
    {
        allowRemote = allowRemote;
    }
    
    /**
     * Set a short description for this application.
     * It is mainly used to show tooltips on the Clien side. 
     * <p>
     * Note: this field is package scope -> client apps can't modify it
     *
     * @param description A short description for this application.
     */
    void setDescription( String description )
    {
        description = description;
    }
}