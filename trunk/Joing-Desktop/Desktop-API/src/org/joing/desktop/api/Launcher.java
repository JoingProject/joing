/*
 * Launcher.java
 *
 * Created on 17 de junio de 2007, 03:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.desktop.api; 

/**
 *
 * @author mario
 */
public interface Launcher 
{
    public String  getName();
    public void    setName(String name);
    public String  getCommand();
    public void    setCommand(String command);
    public String  getArguments();
    public void    setArguments(String args);
    public String  getIconResource();    
    public void    setIconResource(String resource);
    public boolean execute();
}