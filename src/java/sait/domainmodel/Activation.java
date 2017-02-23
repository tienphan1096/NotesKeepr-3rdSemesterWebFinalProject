/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.domainmodel;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tien Phan
 */
@Entity
@Table(name = "activation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activation.findAll", query = "SELECT a FROM Activation a"),
    @NamedQuery(name = "Activation.findByUsername", query = "SELECT a FROM Activation a WHERE a.username = :username"),
    @NamedQuery(name = "Activation.findByUuid", query = "SELECT a FROM Activation a WHERE a.uuid = :uuid")})
public class Activation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "username")
    private String username;
    @Size(max = 50)
    @Column(name = "uuid")
    private String uuid;
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    public Activation() {
    }

    public Activation(String username) {
        this.username = username;
    }
    
    public Activation(String username, String uuid){
        this.username=username;
        this.uuid=uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activation)) {
            return false;
        }
        Activation other = (Activation) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sait.domainmodel.Activation[ username=" + username + " ]";
    }
    
}
