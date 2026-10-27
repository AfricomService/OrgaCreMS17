package com.orgacare.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Salaire.
 */
@Entity
@Table(name = "salaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Salaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private Double valeur;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "personne_id")
    private Long personneId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Salaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeur() {
        return this.valeur;
    }

    public Salaire valeur(Double valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Salaire date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getPersonneId() {
        return this.personneId;
    }

    public Salaire personneId(Long personneId) {
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
        if (!(o instanceof Salaire)) {
            return false;
        }
        return id != null && id.equals(((Salaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salaire{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", date='" + getDate() + "'" +
            ", personneId=" + getPersonneId() +
            "}";
    }
}
