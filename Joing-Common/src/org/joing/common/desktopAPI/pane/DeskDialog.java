/*
 * DeskDialog.java
 *
 * Created on 4 de octubre de 2007, 0:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI.pane;

/**
 * Creates a new dialog.
 * <p>
 * A method like this:<br>
 *    <t>public void setModal( boolean b );
 * does not exists because in all dialogs are modal.
 * <p>
 * @author Francisco Morero Peyrona
 */
public interface DeskDialog extends DeskWindow
{
    /**
     * Diffrent implementations can built Dialogs as modal or modaless.
     * 
     * @return Modal value for the dialog.
     */
    boolean isModal();
}