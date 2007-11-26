/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */

package org.joing.pde.desktop.container;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.joing.common.desktopAPI.pane.DeskDialog;
import org.joing.common.desktopAPI.pane.DeskFrame;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDialog extends PDEFrame implements DeskDialog
{ // FIXME: hay que mejorar esta clase: estudiar la JOptionPane
    
    private Container owner;
    private DeskDialog.ExitValue exitValue = DeskDialog.ExitValue.CANCELED;
    
    //------------------------------------------------------------------------//

    public PDEDialog()
    {
    }
    
    public PDEDialog( DeskFrame owner )
    {
        this.owner = (PDEFrame) owner;
    }
    
    public PDEDialog( DeskDialog owner )
    {
        this.owner = (PDEDialog) owner;
    }
    
    public boolean isModal()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public void setModal( boolean b )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    public DeskDialog.ExitValue getExitValue()
    {
        return exitValue;
    }
}