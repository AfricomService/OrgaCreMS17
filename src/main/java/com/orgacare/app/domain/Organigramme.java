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
 * A Organigramme.
 */
@Entity
@Table(name = "organigramme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organigramme implements Serializable {

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

    @Column(name = "date_action")
    private ZonedDateTime dateAction;

    @Column(name = "date_expiration")
    private ZonedDateTime dateExpiration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Etat etat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organigrammes", "sites", "contrats", "formeJuridiquee" }, allowSetters = true)
    private Societe societe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organigramme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Organigramme code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Organigramme nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Organigramme dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateAction() {
        return this.dateAction;
    }

    public Organigramme dateAction(ZonedDateTime dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public ZonedDateTime getDateExpiration() {
        return this.dateExpiration;
    }

    public Organigramme dateExpiration(ZonedDateTime dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(ZonedDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Organigramme etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Societe getSociete() {
        return this.societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Organigramme societe(Societe societe) {
        this.setSociete(societe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organigramme)) {
            return false;
        }
        return id != null && id.equals(((Organigramme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organigramme{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
