/*
 * JoingApplicationLauncher.java
 *
 * Created on 25 de septiembre de 2007, 9:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.api.desktop.workarea.desklet.deskLauncher;

/**
 *
 * @author Francisco Morero Peyrona
 */
public interface JoingApplicationLauncher extends Launcher
{
    public String   getExecutable();
    public String[] getArguments();
    public String[] getFileTypes();
    public String   getVersion();
}