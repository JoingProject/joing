/*
 * Copyright (C) Francisco Morero Peyrona. All rights reserved.
 *
 * This software is published under the terms of Open Source
 * License version 1.1, a copy of which has been included with this
 * distribution in the License.txt file.
 */

package org.joing.pde.desktop.container;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.joing.api.desktop.DeskDialog;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class PDEDialog 
       extends JOptionPane
       implements DeskDialog
{ // FIXME: hay que mejorar esta clase: estudiar la JOptionPane
    
    private String    sTitle;
    private Container owner;
    
    //------------------------------------------------------------------------//

    public PDEDialog()
    {
    }
    
    public PDEDialog( PDEFrame owner )
    {
        this.owner = owner;
    }
    
    public PDEDialog( PDEDialog owner )
    {
        this.owner = owner;
    }

    public String getTitle()
    {
        return this.sTitle;
    }

    public void setTitle( String sTitle )
    {
        this.sTitle = sTitle;
    }
}