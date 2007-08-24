/*
 * ApplicationEntityPK.java
 * 
 * Created on 20-ago-2007, 10:49:41
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
    @Column(name = "APPLICATION", nullable = false)
    private String application;
    @Column(name = "VERSION", nullable = false)
    private String version;

    public ApplicationEntityPK( )
    {
    }

    public ApplicationEntityPK( String application, String version )
    {
        this.application = application;
        this.version = version;
    }

    public String getApplication( )
    {
        return application;
    }

    public void setApplication( String application )
    {
        this.application = application;
    }

    public String getVersion( )
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    @Override
    public int hashCode( )
    {
        int hash = 0;
        hash += (application != null ? application.hashCode(  ) : 0);
        hash += (version != null ? version.hashCode(  ) : 0);
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !(object instanceof ApplicationEntityPK) )
        {
            return false;
        }
        ApplicationEntityPK other = (ApplicationEntityPK) object;
        if( (this.application == null && other.application != null) || (this.application != null && !this.application.equals( other.application )) )
            return false;
        if( (this.version == null && other.version != null) || (this.version != null && !this.version.equals( other.version )) )
            return false;
        return true;
    }

    @Override
    public String toString( )
    {
        return "ejb.app.ApplicationEntityPK[application=" + application + ", version=" + version + "]";
    }
}