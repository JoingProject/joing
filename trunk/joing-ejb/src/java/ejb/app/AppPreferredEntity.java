/*
 * AppPreferredEntity.java
 * 
 * Created on 20-ago-2007, 14:28:45
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author fmorero
 */
@Entity
@Table(name = "APP_PREFERRED")
@NamedQueries({@NamedQuery(name = "AppPreferredEntity.findByFileExtension", query = "SELECT a FROM AppPreferredEntity a WHERE a.fileExtension = :fileExtension")})
public class AppPreferredEntity implements Serializable {
    @Id
    @Column(name = "FILE_EXTENSION", nullable = false)
    private String fileExtension;

    @Column(name = "ID_APPLICATION", nullable = false)
    private int idApplication;

    public AppPreferredEntity()
    {
    }
    
    public AppPreferredEntity( String fileExtension )
    {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension( )
    {
        return fileExtension;
    }

    protected void setFileExtension( String fileExtension )
    {
        this.fileExtension = fileExtension;
    }

    public int getIdApplication()
    {
        return this.idApplication;
    }
    
    public void setIdApplication( int idApplication )
    {
        this.idApplication = idApplication;
    }
    
    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (fileExtension != null ? fileExtension.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof AppPreferredEntity) )
        {
            return false;
        }
        AppPreferredEntity other = (AppPreferredEntity) object;
        if( (this.fileExtension == null && other.fileExtension != null) || (this.fileExtension != null && !this.fileExtension.equals( other.fileExtension )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.app.AppPreferredEntity[fileExtension=" + fileExtension + "]";
    }

}
