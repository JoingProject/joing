/*
 * UserDTOs.java
 *
 * Created on 12 de octubre de 2007, 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.user;

/**
 *
 * @author Francisco Morero Peyrona
 */
class UserDTOs
{
    static Local createLocal( LocaleEntity localeEntity )
    {
        Local local = new Local();
        
        local.setIdLocale( localeEntity.getIdLocale() );
        local.setLanguage( localeEntity.getIdiom() );
        local.setCountry(  localeEntity.getCountry() );
        
        return local;
    }
    
    static User createUser( UserEntity userEntity )
    {
        User user = new User();
        
        transfer( userEntity, user );
        
        return user;
    }
    
    static UserEntity createUserEntity( User user )
    {
        UserEntity userEntity = new UserEntity();
        
        transfer( user, userEntity );
        
        return userEntity;
    }
    
    static void transfer( User fromUser, UserEntity toUserEntity )
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
    
    static void transfer( UserEntity fromUserEntity, User toUser )
    {
        toUser.setAccount( fromUserEntity.getAccount() );
        toUser.setEmail( fromUserEntity.getEmail() );
        toUser.setFirstName( fromUserEntity.getFirstName() );
        toUser.setSecondName( fromUserEntity.getSecondName() );
        toUser.setMale( fromUserEntity.getIsMale() != 0 );
        toUser.setIdLocale( fromUserEntity.getIdLocale().getIdLocale() );
        toUser.setTotalSpace( (long) fromUserEntity.getQuota() );
    }
}