/*
 * WDesktopComponentChangeEvent.java
 *
 * Created on 10 de febrero de 2007, 19:24
 *
 * (c) 2006 - Francisco Morero Peyrona
 *
 * License: {license}
 */

package org.joing.pde.desktop.workarea.items;

import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Events that can occur over a DeskComponent.
 * 
 * Sólo clases de este paquete pueden construir instancias de esta clase.  
 * 
 * @author Francisco Morero
 */
public class WorkAreaItemChangeEvent extends EventObject
{
    public final static int MOVED_INFORM             = 0;  // Informa q el componente ya se ha movido
    public final static int SELECTED_INFORM          = 2;  // Informa q el componente fue seleccionado 
    public final static int DESELECTED_INFORM        = 3;  // Informa q el componente fue deseleccionado
    public final static int SELECTION_REQUEST        = 4;  // Solicitud de ser seleccionado
  //public final static int DESELECTED_REQUEST       = 5;  // Ver nota más abajo
    public final static int SELECTION_INCR_REQUEST   = 6;  // Solicitud de selección incremental
    public final static int DESELECTION_INCR_REQUEST = 7;  // Solicitud de deselección incremental 
    
    private int nType;
    private MouseEvent me;    // Si lo originó un evento de ratón, su referencia
    
    // DESELECTED_REQUEST
    // Nota: No se usa, ya q un componente se deselecciona cuando se hace clic sobre otro,
    //       y él solo puede saber si se ha hecho clic sobre él, no sabe nada de los otros.
    //       Sí existe la deselección incremental: si hay varios componentes seleccionados
    //       y se pulsa Ctrl-clic sobre uno de ellos, este se deselecciona.
    //       De todos modos igual se me ocurre algo de utilidad en el futuro.
    
    /**
     * Default class constructor
     */
    public WorkAreaItemChangeEvent( Object source, MouseEvent me )
    {
        super( source );
        this.me = me;
    }

    public int getType()
    {
        return this.nType;
    }
    
    public void setType( int type )
    {
        this.nType = type;
    }
    
    /** 
     * El evento de ratón q lo originó o <code>null</code> si no fue un evento de ratón
     * 
     * @return El evento de ratón q lo originó o <code>null</code> si no fue un evento de ratón
     */ 
    public MouseEvent getMouseEvent()
    {
        return this.me;
    }
}