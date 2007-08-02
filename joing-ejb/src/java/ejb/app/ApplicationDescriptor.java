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
public class Application implements  Serializable   // TODO hacer el serializable
{
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
    Application( ApplicationEntity _app )
    {
        this.name       = _app.getApplicationEntityPK().getName();
        this.version    = _app.getApplicationEntityPK().getVersion();
        this.executable = _app.getExecutable();
        this.iconPNG    = _app.getIconPng();
        this.iconSVG    = _app.getIconSvg();
        this.fileTypes  = new ArrayList<String>();  // Good practice: empty list instead of null
        
        // Prepare file types
        if( _app.getFileTypes() != null )
        {
            StringTokenizer st = new StringTokenizer( _app.getFileTypes(), ";" );
            
            while( st.hasMoreTokens() )
                this.fileTypes.add( st.nextToken() );
        }
    }

    /**
     * Return the name of the application.
     *
     * @return The name of the application.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Return the version of the application.
     *
     * @return The version of the application.
     */
    public String getVersion()
    {
        return this.version;
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
        return this.iconPNG;
    }
    
    /**
     * Return the SVG icon for this application.
     * 
     * @return The SVG icon for this application.
     */
    public byte[] getSVGIcon()
    {
        return this.iconSVG;
    }
    
    public String[] getFileTypes()
    {
        // Good practice: Defensive copy
        return this.fileTypes.toArray( new String[ this.fileTypes.size() ] );
    }
    
    public String getDescription()
    {
        return ((this.description == null) ? "" : this.description);
    }
    
    public boolean isRemoteExecutionAllowed()
    {
        return this.allowRemote;
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
        this.description = description;
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
        this.allowRemote = allowRemote;
    }
}