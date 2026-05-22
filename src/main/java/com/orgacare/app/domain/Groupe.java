package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Groupe.
 */
@Entity
@Table(name = "groupe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "date_activation")
    private ZonedDateTime dateActivation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Etat etat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "societe" }, allowSetters = true)
    private Organigramme organigramme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Groupe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Groupe code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Groupe nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Groupe dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return this.dateActivation;
    }

    public Groupe dateActivation(ZonedDateTime dateActivation) {
        this.setDateActivation(dateActivation);
        return this;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Groupe etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Organigramme getOrganigramme() {
        return this.organigramme;
    }

    public void setOrganigramme(Organigramme organigramme) {
        this.organigramme = organigramme;
    }

    public Groupe organigramme(Organigramme organigramme) {
        this.setOrganigramme(organigramme);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groupe)) {
            return false;
        }
        return id != null && id.equals(((Groupe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groupe{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
