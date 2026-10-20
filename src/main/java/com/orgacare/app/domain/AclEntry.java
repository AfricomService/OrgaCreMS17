package com.orgacare.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclEntry.
 */
@Entity
@Table(name = "acl_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "sid_id")
    private Long sidId;

    @Column(name = "can_read")
    private Boolean canRead;

    @Column(name = "can_write")
    private Boolean canWrite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AclEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public AclEntry objectType(String objectType) {
        this.setObjectType(objectType);
        return this;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public AclEntry objectId(Long objectId) {
        this.setObjectId(objectId);
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getSidId() {
        return this.sidId;
    }

    public AclEntry sidId(Long sidId) {
        this.setSidId(sidId);
        return this;
    }

    public void setSidId(Long sidId) {
        this.sidId = sidId;
    }

    public Boolean getCanRead() {
        return this.canRead;
    }

    public AclEntry canRead(Boolean canRead) {
        this.setCanRead(canRead);
        return this;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanWrite() {
        return this.canWrite;
    }

    public AclEntry canWrite(Boolean canWrite) {
        this.setCanWrite(canWrite);
        return this;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclEntry)) {
            return false;
        }
        return id != null && id.equals(((AclEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclEntry{" +
            "id=" + getId() +
            ", objectType='" + getObjectType() + "'" +
            ", objectId=" + getObjectId() +
            ", sidId=" + getSidId() +
            ", canRead='" + getCanRead() + "'" +
            ", canWrite='" + getCanWrite() + "'" +
            "}";
    }
}
