/*
 * UserBridge.java
 *
 * Created on 18 de junio de 2007, 15:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.runtime.bridge2server;

import ejb.user.Local;
import ejb.user.User;
import java.util.List;

/**
 *
 * @author fmorero
 */
public interface UserBridge
{
    User getUser();
    
    void updateUser( User user );
    
    List<Local> getAvailableLocales();
}