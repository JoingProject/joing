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

package org.joing.kernel.jvmm;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class JarSearchResult {

    private String entryName;
    private byte[] bytes;
    private boolean found = false;

    JarSearchResult(String name, byte[] bytes, boolean found) {
        this.entryName = name;
        this.bytes = bytes;
        this.found = found;
    }

    // <editor-fold defaultstate="collapsed" desc="getters">
    public byte[] getBytes() {
        return bytes;
    }

    public String getEntryName() {
        return entryName;
    }

    public boolean isFound() {
        return found;
    }
    // </editor-fold>
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("[").append(entryName).append(", ");
        if (found) sb.append("found");
        else sb.append("not found");
        sb.append("]");
        
        return sb.toString();
    }
}
