/*
 * ApplicationEntityPK.java
 * 
 * Created on 09-jul-2007, 21:23:11
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fmorero
 */
@Embeddable
public class ApplicationEntityPK implements Serializable {
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "VERSION", nullable = false)
    private String version;

    public ApplicationEntityPK() {
    }

    public ApplicationEntityPK(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        hash += (version != null ? version.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
if (!(object instanceof ApplicationEntityPK)) {
            return false;
        }
        ApplicationEntityPK other = (ApplicationEntityPK) object;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.app.ApplicationEntityPK[name=" + name + ", version=" + version + "]";
    }

}
