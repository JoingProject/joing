/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.desktopAPI;

/**
 * Standar images: common to all desktop implementations.
 * Every implementation can define its own images set, but the names will be 
 * always the same to allow applications protability between desktops.
 * 
 * @author Francisco Morero Peyrona
 */
public enum StandardImage
{ 
    ALERT       ,
    COPY        ,
    CUT         ,
    DELETE      ,
    DESKTOP     ,   // Desktop logo
    FOLDER      , 
    FILE        ,
    INFO        ,
    JOING       ,
    LAUNCHER    ,
    LOCK        ,
    MOVE        ,
    NEW         ,
    NO_IMAGE    ,
    NOTIFICATION,
    PASTE       ,
    PRINT       ,
    PROPERTIES  ,
    REMOVE      ,
    SEARCH      ,
    TRASHCAN    ,
    USER_FEMALE ,
    USER_MALE   ;
}