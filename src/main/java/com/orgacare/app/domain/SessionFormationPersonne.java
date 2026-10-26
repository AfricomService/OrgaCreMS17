package com.orgacare.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionFormationPersonne.
 */
@Entity
@Table(name = "session_formation_personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SessionFormationPersonne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "personne_id")
    private Long personneId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionFormationPersonne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return this.sessionId;
    }

    public SessionFormationPersonne sessionId(Long sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPersonneId() {
        return this.personneId;
    }

    public SessionFormationPersonne personneId(Long personneId) {
        this.setPersonneId(personneId);
        return this;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormationPersonne)) {
            return false;
        }
        return id != null && id.equals(((SessionFormationPersonne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormationPersonne{" +
            "id=" + getId() +
            ", sessionId=" + getSessionId() +
            ", personneId=" + getPersonneId() +
            "}";
    }
}
