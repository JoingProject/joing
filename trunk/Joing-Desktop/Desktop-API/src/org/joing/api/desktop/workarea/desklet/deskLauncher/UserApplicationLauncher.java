/*
 * UserApplicationLauncher.java
 * 
 * Created on 11-sep-2007, 1:35:19
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea.desklet.deskLauncher;

import org.joing.api.desktop.workarea.*;
import org.joing.api.desktop.workarea.desklet.*;

/**
 *
 * @author fmorero
 */
public interface UserApplicationLauncher extends Launcher
{
    public String   getCommand();
    public void     setCommand( String command );
    public String[] getArguments();
    public void     setArguments( String[] asArgument );
}
