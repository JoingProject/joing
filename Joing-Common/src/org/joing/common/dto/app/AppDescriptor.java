/*
 * Application.java
 *
 * Created on 18 de mayo de 2007, 17:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.dto.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo usando: serialver -show
    
    private int          id;
    private String       name;
    private String       version;
    private String       executable;
    private String[]     arguments;
    private byte[]       iconPixel;     // PNG like
    private byte[]       iconVector;    // SVG like
    private List<String> fileTypes;
    private String       description;
    private String       author;
    private String       vendor;
    private String       license;
    private boolean      allowRemote;  // Used by ApplicationManagerBean class
    
    //------------------------------------------------------------------------//
    
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
     * Return the arguments that will be passed to the application or an empty
     * String array if none.
     *
     * @return The arguments.
     */
    public String[] getArguments()
    {
        return arguments;
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
    public byte[] getIconPixel()
    {
        byte[] ret;
        
        if( iconPixel == null )
        {
            ret = new byte[0];
        }
        else
        {   // Defensive copy
            ret = new byte[ iconPixel.length ];
            System.arraycopy( iconPixel, 0, ret, 0, iconPixel.length );
        }
        
        return ret;
    }
    
    /**
     * Return the SVG icon for this application.
     * 
     * @return The SVG icon for this application.
     */
    public byte[] getIconVector()
    {
        byte[] ret;
        
        if( iconVector == null )
        {
            ret = new byte[0];
        }
        else
        {   // Defensive copy
            ret = new byte[ iconVector.length ];
            System.arraycopy( iconVector, 0, ret, 0, iconVector.length );
        }
        
        return ret;
    }
    
    /**
     * Informs about the file types this application can work with or an empty
     * list if none.
     *
     * @return An array indicating those file name extensions that this 
     *         application can operate with.
     */
    public String[] getFileTypes()
    {
        return fileTypes.toArray( new String[ fileTypes.size() ] );   // Defensive copy
    }
    
    /**
     * The application description.
     *
     * @return The application description
     */
    public String getDescription()
    {
        return ((description == null) ? "" : description);
    }
    
    /**
     * The application author.
     *
     * @return The application author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * The application license.
     *
     * @return The application license
     */
    public String getLicense()
    {
        return license;
    }

    /**
     * The application vendor.
     *
     * @return The application vendor
     */
    public String getVendor()
    {
        return vendor;
    }
    
    /**
     * Tells about the permission for the user to run the application remotely.
     *
     * @return <code>true</code> if the user is allowed to run this application
     *         remotely (will run inside the server and will be viewed from 
     *         using the client somekind of VNC).
     */
    public boolean isRemoteExecutionAllowed()
    {
        return allowRemote;
    }
    
    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }
    
    //------------------------------------------------------------------------//
    // NEXT: Los siguientes métodos debieran ser package (o al menos protected)
    
    /**
     * Class constructor (this class is a DTO).
     * <p>
     * For security and encapsulation reasons, the constructor has package scope:
     * only the Manager EJB can create them.<br>
     * If any other part of the application would need to create for example an
     * empty instance of this class, then a method can be added to the Manager
     * EJB (this method can return an empty instance).
     */
    public AppDescriptor()
    {
        id          = -1;
        arguments   = new String[0];
        fileTypes   = new ArrayList<String>();
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setName(String name)
    {
        this.name = (name == null ? null : name.trim());
    }
    
    public void setVersion(String version)
    {
        this.version = (version == null ? null : version.trim());
    }
    
    public void setExecutable(String executable)
    {
        this.executable = (executable == null ? null : executable.trim());
    }
    
    public void setArguments(String[] arguments)
    {
        if( arguments == null )
        {
            this.arguments = new String[0];
        }
        else
        {
            for( int n = 0; n < arguments.length; n++ )
            {
                if( arguments[n] != null )
                    arguments[n] = arguments[n].trim();
            }
            
            arguments = new String[ arguments.length ];
            System.arraycopy( arguments, 0, this.arguments, 0, arguments.length );
        }
    }

    public void setIconPixel(byte[] iconPNG)
    {
        this.iconPixel = iconPNG;
    }
    
    public void setIconVector(byte[] iconSVG)
    {
        this.iconVector = iconSVG;
    }
    
    public void setFileTypes(List<String> fileTypes)
    {
        if( fileTypes == null )
            fileTypes = new ArrayList<String>();  // Good practice: empty list instead of null
            
        this.fileTypes = fileTypes;
    }
    
    /**
     * Set if this application can or can not be executed remotely (in the 
     * WebPC Server).
     * <p>
     * Note: this field is package scope -> client apps can't modify it
     * @param description The description for this application.
     */
    public void setRemoteExecutionAllowed( boolean allowRemote )
    {
        this.allowRemote = allowRemote;
    }
    
    /**
     * Set a short description for this application.
     * It is mainly used to show tooltips on the Clien side. 
     * <p>
     * Note: this field is package scope -> client apps can't modify it
     *
     * @param description A short description for this application.
     */
    public void setDescription( String description )
    {
        this.description = (description == null ? null : description.trim());
    }

    public void setAuthor( String author )
    {
        this.author = (author == null ? null : author.trim());
    }

    public void setLicense( String license )
    {
        this.license = (license == null ? null : license.trim());
    }

    public void setVendor( String vendor )
    {
        this.vendor = (vendor == null ? null : vendor.trim());
    }
}