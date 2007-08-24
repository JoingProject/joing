/*
 * FileEntityPK.java
 * 
 * Created on 20-ago-2007, 10:57:56
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
    @Column(name = "ACCOUNT", nullable = false)
    private String account;
    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    public FileEntityPK( )
    {
    }

    public FileEntityPK( String account, String filePath, String fileName )
    {
        this.account = account;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getAccount( )
    {
        return account;
    }

    public void setAccount( String account )
    {
        this.account = account;
    }

    public String getFilePath( )
    {
        return filePath;
    }

    public void setFilePath( String filePath )
    {
        this.filePath = filePath;
    }

    public String getFileName( )
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (account != null ? account.hashCode(  ) : 0);
        hash += (filePath != null ? filePath.hashCode(  ) : 0);
        hash += (fileName != null ? fileName.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof FileEntityPK) )
        {
            return false;
        }
        FileEntityPK other = (FileEntityPK) object;
        if( (this.account == null && other.account != null) || (this.account != null && !this.account.equals( other.account )) )
            return false;
        if( (this.filePath == null && other.filePath != null) || (this.filePath != null && !this.filePath.equals( other.filePath )) )
            return false;
        if( (this.fileName == null && other.fileName != null) || (this.fileName != null && !this.fileName.equals( other.fileName )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.vfs.FileEntityPK[account=" + account + ", filePath=" + filePath + ", fileName=" + fileName + "]";
    }

}
