/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joing.common.pkt.app;

/**
 *
 * @author antoniovl
 */
public class ApplicationReply implements java.io.Serializable {
    private String account;
    private boolean ok = true;
    // any additional info?
    private Object reply;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Object getReply() {
        return reply;
    }

    public void setReply(Object reply) {
        this.reply = reply;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
