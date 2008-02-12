/*
 * FileEntityPK.java
 *
 * Created on 13 de octubre de 2007, 20:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb.vfs;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Primary Key class FileEntityPK for entity class FileEntity
 * 
 * @author Francisco Morero Peyrona
 */
@Embeddable
public class FileEntityPK implements Serializable
{
    @Column(name = "ACCOUNT", nullable = false)
    private String account;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;
    
    /** Creates a new instance of FileEntityPK */
    public FileEntityPK()
    {
    }

    /**
     * Creates a new instance of FileEntityPK with the specified values.
     * @param fileName the fileName of the FileEntityPK
     * @param filePath the filePath of the FileEntityPK
     * @param account the account of the FileEntityPK
     */
    public FileEntityPK(String fileName, String filePath, String account)
    {
        this.fileName = fileName;
        this.filePath = filePath;
        this.account = account;
    }

    /**
     * Gets the account of this FileEntityPK.
     * @return the account
     */
    public String getAccount()
    {
        return this.account;
    }

    /**
     * Sets the account of this FileEntityPK to the specified value.
     * @param account the new account
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    /**
     * Gets the filePath of this FileEntityPK.
     * @return the filePath
     */
    public String getFilePath()
    {
        return this.filePath;
    }

    /**
     * Sets the filePath of this FileEntityPK to the specified value.
     * @param filePath the new filePath
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * Gets the fileName of this FileEntityPK.
     * @return the fileName
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * Sets the fileName of this FileEntityPK to the specified value.
     * @param fileName the new fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.fileName != null ? this.fileName.hashCode() : 0);
        hash += (this.filePath != null ? this.filePath.hashCode() : 0);
        hash += (this.account != null ? this.account.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this FileEntityPK.  The result is 
     * <code>true</code> if and only if the argument is not null and is a FileEntityPK object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileEntityPK)) {
            return false;
        }
        FileEntityPK other = (FileEntityPK)object;
        if (this.fileName != other.fileName && (this.fileName == null || !this.fileName.equals(other.fileName))) return false;
        if (this.filePath != other.filePath && (this.filePath == null || !this.filePath.equals(other.filePath))) return false;
        if (this.account != other.account && (this.account == null || !this.account.equals(other.account))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return "ejb.vfs.FileEntityPK[fileName=" + fileName + ", filePath=" + filePath + ", account=" + account + "]";
    }
    
}
