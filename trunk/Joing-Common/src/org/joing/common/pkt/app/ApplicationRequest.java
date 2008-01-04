/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.pkt.app;

/**
 *
 * @author Antonio Varela Lizardi <antonio@icon.net.mx>
 */
public class ApplicationRequest implements java.io.Serializable {
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
