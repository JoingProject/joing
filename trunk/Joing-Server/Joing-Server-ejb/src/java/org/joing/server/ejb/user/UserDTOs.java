/*
 * Copyright (C) 2007, 2008 Join'g Team Members. All Rights Reserved.
 * Join'g Team Members are listed at project's home page. By the time of 
 * writting this at: https://joing.dev.java.net/servlets/ProjectMemberList.
 *
 * This file is part of Join'g project: www.joing.org
 *
 * GNU Classpath is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the free
 * Software Foundation; either version 3, or (at your option) any later version.
 * 
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GNU Classpath; see the file COPYING.  If not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.joing.server.ejb.user;

import java.util.Locale;
import org.joing.common.dto.user.Local;
import org.joing.common.dto.user.User;

/**
 *
 * @author Francisco Morero Peyrona
 */
// NEXT: Esta clase habría que hacerla con constructor en lugar de como métodos static para poder crear múltiples instancias y que el servidor escale mejor
class UserDTOs
{
    static synchronized Local createLocal( LocaleEntity localeEntity )
    {
        Local local = new Local();
        
        local.setIdLocale( localeEntity.getIdLocale() );
        local.setLanguage( localeEntity.getIdiom()    );
        local.setCountry(  localeEntity.getCountry()  );
        
        return local;
    }
    
    static synchronized User createUser( UserEntity userEntity )
    {
        User user = new User();
        
        transfer( userEntity, user );
        
        return user;
    }
    
    static synchronized UserEntity createUserEntity( User user )
    {
        UserEntity userEntity = new UserEntity();
        
        transfer( user, userEntity );
        
        return userEntity;
    }
    
    static synchronized void transfer( User fromUser, UserEntity toUserEntity )
    {
        LocaleEntity _locale = new LocaleEntity();
                     _locale.setIdLocale( fromUser.getIdLocale() );
                     _locale.setIdiom( fromUser.getLocale().getLanguage() );
                     _locale.setCountry( fromUser.getLocale().getCountry() );
                     
        toUserEntity.setAccount( fromUser.getAccount() );
        toUserEntity.setEmail( fromUser.getEmail() );
        toUserEntity.setFirstName( fromUser.getFirstName() );
        toUserEntity.setSecondName( fromUser.getSecondName() );
        toUserEntity.setIsMale( (short)(fromUser.isMale() ? 1 : 0) );
        toUserEntity.setIdLocale( _locale );
    }
    
    static synchronized void transfer( UserEntity fromUserEntity, User toUser )
    {        
        toUser.setAccount( fromUserEntity.getAccount() );
        toUser.setEmail( fromUserEntity.getEmail() );
        toUser.setFirstName( fromUserEntity.getFirstName() );
        toUser.setSecondName( fromUserEntity.getSecondName() );
        toUser.setMale( fromUserEntity.getIsMale() != 0 );
        toUser.setIdLocale( fromUserEntity.getIdLocale().getIdLocale() );
        // FIXME: Aunqaue la tabla USERS tenga el campo ID_LOCALE, cuando se 
        //        cambia por UserEntity, tendría que tener una instancia de LocaleEntity
        toUser.setLocale( Locale.getDefault() );
        //-----------------------------------------------------------------------------
        toUser.setTotalSpace( (long) fromUserEntity.getQuota() );
    }
}