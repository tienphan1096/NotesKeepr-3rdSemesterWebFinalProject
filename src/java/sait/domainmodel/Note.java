/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.domainmodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Tien Phan
 */
@Entity
@Table(name = "notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Note.findAll", query = "SELECT n FROM Note n"),
    @NamedQuery(name = "Note.findByNoteid", query = "SELECT n FROM Note n WHERE n.noteid = :noteid"),
    @NamedQuery(name = "Note.findByDatecreated", query = "SELECT n FROM Note n WHERE n.datecreated = :datecreated"),
    @NamedQuery(name = "Note.findByContents", query = "SELECT n FROM Note n WHERE n.contents = :contents")})
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "noteid")
    private Integer noteid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Size(max = 10000)
    @Column(name = "contents")
    private String contents;
    @JoinTable(name = "notecollaborators", joinColumns = {
        @JoinColumn(name = "noteid", referencedColumnName = "noteid")}, inverseJoinColumns = {
        @JoinColumn(name = "username", referencedColumnName = "username")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> userList;
    @JoinColumn(name = "owner", referencedColumnName = "username")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User owner;

    public Note() {
    }

    public Note(Integer noteid) {
        this.noteid = noteid;
    }

    public Note(Integer noteid, Date datecreated) {
        this.noteid = noteid;
        this.datecreated = datecreated;
    }
    
    public Note(Integer noteid, Date datecreated, String contents) {
        this.noteid = noteid;
        this.datecreated = datecreated;
        this.contents=contents;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noteid != null ? noteid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Note)) {
            return false;
        }
        Note other = (Note) object;
        if ((this.noteid == null && other.noteid != null) || (this.noteid != null && !this.noteid.equals(other.noteid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sait.domainmodel.Note[ noteid=" + noteid + " ]";
    }
    
}
