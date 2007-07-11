/*
 * FileEntityPK.java
 * 
 * Created on 09-jul-2007, 21:37:10
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.vfs;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fmorero
 */
@Embeddable
public class FileEntityPK implements Serializable {
    @Column(name = "ID_PARENT", nullable = false)
    private int idParent;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "IS_DIR", nullable = false)
    private short isDir;

    public FileEntityPK() {
    }

    public FileEntityPK(int idParent, String name, short isDir) {
        this.idParent = idParent;
        this.name = name;
        this.isDir = isDir;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getIsDir() {
        return isDir;
    }

    public void setIsDir(short isDir) {
        this.isDir = isDir;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idParent;
        hash += (name != null ? name.hashCode() : 0);
        hash += (int) isDir;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
if (!(object instanceof FileEntityPK)) {
            return false;
        }
        FileEntityPK other = (FileEntityPK) object;
        if (this.idParent != other.idParent) {
            return false;
        }
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.isDir != other.isDir) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.vfs.FileEntityPK[idParent=" + idParent + ", name=" + name + ", isDir=" + isDir + "]";
    }

}
