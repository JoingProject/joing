/*
 * UserBridge.java
 *
 * Created on 18 de junio de 2007, 15:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.joing.common.runtime;

import java.util.List;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;

/**
 *
 * @author fmorero
 */
public interface UserBridge
{
    User getUser();
    
    User updateUser( User user );
    
    List<Local> getAvailableLocales();
}