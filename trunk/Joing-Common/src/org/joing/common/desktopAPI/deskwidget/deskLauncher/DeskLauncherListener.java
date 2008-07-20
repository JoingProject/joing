/*
 * DeskLauncherListener.java
 * 
 * Created on 11-sep-2007, 14:33:03
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.deskwidget.deskLauncher;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface DeskLauncherListener
{
    /** For both: selection and de-selection */
    public void selection( DeskLauncher l );
    /** For both: selection and de-selection */
    public void selectionIncremental( DeskLauncher l );
    /** Informs that launch action was done (the responsability of launching relays on the launcher) */
    public void launched( DeskLauncher l );
}