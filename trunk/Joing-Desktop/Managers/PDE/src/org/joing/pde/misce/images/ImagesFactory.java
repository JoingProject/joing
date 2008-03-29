/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.pde.misce.images;

/**
 * Just a tricky class to access cleanly common icons.
 * 
 * @author Francisco Morero Peyrona
 */
public class ImagesFactory
{
    public enum Icon 
    { 
        COPY       ( "copy"        ),
        CUT        ( "cut"         ),
        DELETE     ( "delete"      ), 
        FOLDER     ( "folder"      ), 
        INFO       ( "info"        ),
        LAUNCHER   ( "launcher"    ), 
        LOCK       ( "lock"        ),
        MOVE       ( "move"        ), 
        NO_IMAGE   ( "no_image"    ),
        PASTE      ( "paste"       ),
        PROPERTIES ( "properties"  ),
        REMOVE     ( "remove"      ),
        CONN_SERVER( "conn_server" ),
        TRASHCAN   ( "trashcan"    ), 
        USER_FEMALE( "user_female" ),
        USER_MALE  ( "user_male"   ); 
      
        private final String sName;      // Don't use var name "name"
        
        Icon( String name )      { this.sName = name; }
        public String getName()  { return sName; }
    }
    
    public static Icon getIcon( String sName )
    {
        for( Icon icon : Icon.values() )
        {
            if( icon.getName().equals( sName ) )
                return icon;
        }
        
        return null;
    }
}