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
    private static final long serialVersionUID = 1L;    // TODO: cambiarlo por un nº apropiado
    
    private int          id;
    private String       name;
    private String       version;
    private String       executable;
    private String[]     arguments;
    private byte[]       iconPNG;
    private byte[]       iconSVG;
    private List<String> fileTypes;
    private String       description;
    private boolean      allowRemote;  // Used by ApplicationManagerBean class
    
    private int          environment;  // Defined in org.joing.common.dto.app.AppEnvironment
    private int          environ_ver;  // Minimum Environment Version to run the application
    
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
     * Return the arguments that will be passed to the appllication.
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
    public byte[] getPNGIcon()   // FIXME: Hay que renombrarlo a getIconPNG
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
    public byte[] getSVGIcon()   // FIXME: Hay que renombrarlo a getIconSVG
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
    
    /**
     * Informs about the file types this application can work with.
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
    
    /**
     * The kind of Java environment this application needs.
     *
     * @return The kind of Java environment this application needs.
     * @see org.joing.common.dto.app.AppEnvironment
     */
    public int getEnvironment()
    {
        return environment;
    }

    /**
     * The minimum environment version needed to run this application.
     *
     * @return Minimum environment version needed to run this application.
     */ 
    public int getEnvironVersion()
    {
        return environ_ver;
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
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public void setExecutable(String executable)
    {
        this.executable = executable;
    }
    
    public void setArguments(String[] arguments)
    {
        this.arguments = arguments;
    }

    public void setIconPNG(byte[] iconPNG)
    {
        this.iconPNG = iconPNG;
    }
    
    public void setIconSVG(byte[] iconSVG)
    {
        this.iconSVG = iconSVG;
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
        this.description = description;
    }

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public void setEnvironment(int environment)
    {
        this.environment = environment;
    }

    public void setEnvironVersion(int environ_ver)
    {
        this.environ_ver = environ_ver;
    }
}