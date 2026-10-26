package com.orgacare.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionFormationChefProjet.
 */
@Entity
@Table(name = "session_formation_chef_projet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SessionFormationChefProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "chef_projet_id")
    private Long chefProjetId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionFormationChefProjet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return this.sessionId;
    }

    public SessionFormationChefProjet sessionId(Long sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getChefProjetId() {
        return this.chefProjetId;
    }

    public SessionFormationChefProjet chefProjetId(Long chefProjetId) {
        this.setChefProjetId(chefProjetId);
        return this;
    }

    public void setChefProjetId(Long chefProjetId) {
        this.chefProjetId = chefProjetId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormationChefProjet)) {
            return false;
        }
        return id != null && id.equals(((SessionFormationChefProjet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormationChefProjet{" +
            "id=" + getId() +
            ", sessionId=" + getSessionId() +
            ", chefProjetId=" + getChefProjetId() +
            "}";
    }
}
