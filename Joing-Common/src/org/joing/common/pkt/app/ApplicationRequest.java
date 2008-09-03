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

package org.joing.common.pkt.app;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ApplicationRequest implements java.io.Serializable {
    
    public static final int AVAILABLE_DESKTOPS = 5;
    
    private int code;
    private String name;
    private String sessionId;
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApplicationRequest other = (ApplicationRequest) obj;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.sessionId != other.sessionId && (this.sessionId == null || !this.sessionId.equals(other.sessionId))) {
            return false;
        }
        if (this.account != other.account && (this.account == null || !this.account.equals(other.account))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.sessionId != null ? this.sessionId.hashCode() : 0);
        hash = 37 * hash + (this.account != null ? this.account.hashCode() : 0);
        return hash;
    }
}