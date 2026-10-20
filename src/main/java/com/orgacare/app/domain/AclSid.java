package com.orgacare.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclSid.
 */
@Entity
@Table(name = "acl_sid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclSid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sid_type")
    private String sidType;

    @Column(name = "sid_value")
    private String sidValue;

    @Column(name = "sid_key")
    private String sidKey;

    @Column(name = "nom_descriptif")
    private String nomDescriptif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AclSid id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSidType() {
        return this.sidType;
    }

    public AclSid sidType(String sidType) {
        this.setSidType(sidType);
        return this;
    }

    public void setSidType(String sidType) {
        this.sidType = sidType;
    }

    public String getSidValue() {
        return this.sidValue;
    }

    public AclSid sidValue(String sidValue) {
        this.setSidValue(sidValue);
        return this;
    }

    public void setSidValue(String sidValue) {
        this.sidValue = sidValue;
    }

    public String getSidKey() {
        return this.sidKey;
    }

    public AclSid sidKey(String sidKey) {
        this.setSidKey(sidKey);
        return this;
    }

    public void setSidKey(String sidKey) {
        this.sidKey = sidKey;
    }

    public String getNomDescriptif() {
        return this.nomDescriptif;
    }

    public AclSid nomDescriptif(String nomDescriptif) {
        this.setNomDescriptif(nomDescriptif);
        return this;
    }

    public void setNomDescriptif(String nomDescriptif) {
        this.nomDescriptif = nomDescriptif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclSid)) {
            return false;
        }
        return id != null && id.equals(((AclSid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclSid{" +
            "id=" + getId() +
            ", sidType='" + getSidType() + "'" +
            ", sidValue='" + getSidValue() + "'" +
            ", sidKey='" + getSidKey() + "'" +
            ", nomDescriptif='" + getNomDescriptif() + "'" +
            "}";
    }
}
