/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.silesiajug.website.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author user
 */
@Entity
@Table(name = "registrationKey")
@NamedQueries({
    @NamedQuery(name = "RegistrationKey.findAll", query = "SELECT r FROM RegistrationKey r"),
    @NamedQuery(name = "RegistrationKey.findById", query = "SELECT r FROM RegistrationKey r WHERE r.id = :id"),
    @NamedQuery(name = "RegistrationKey.findByUserKey", query = "SELECT r FROM RegistrationKey r WHERE r.userKey = :userKey"),
    @NamedQuery(name = "RegistrationKey.findByRequestDate", query = "SELECT r FROM RegistrationKey r WHERE r.requestDate = :requestDate")})
public class RegistrationKey implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "userKey")
    private String userKey;
    @Column(name = "requestDate")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RegistrationKey() {
    }

    public RegistrationKey(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistrationKey)) {
            return false;
        }
        RegistrationKey other = (RegistrationKey) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.silesiajug.model.RegistrationKey[id=" + id + "]";
    }
}
