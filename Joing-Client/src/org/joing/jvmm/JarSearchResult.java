/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.jvmm;

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
