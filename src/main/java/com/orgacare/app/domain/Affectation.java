package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Affectation.
 */
@Entity
@Table(name = "affectation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeAffectation type;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "date_action")
    private ZonedDateTime dateAction;

    @Column(name = "date_fin")
    private ZonedDateTime dateFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Etat etat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organigramme", "site", "departementParent", "personnes" }, allowSetters = true)
    private Departement departement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organigramme" }, allowSetters = true)
    private Groupe groupe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organigrammes", "sites", "contrats", "formeJuridiquee" }, allowSetters = true)
    private Societe societe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Affectation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeAffectation getType() {
        return this.type;
    }

    public Affectation type(TypeAffectation type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeAffectation type) {
        this.type = type;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Affectation dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateAction() {
        return this.dateAction;
    }

    public Affectation dateAction(ZonedDateTime dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public Affectation dateFin(ZonedDateTime dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Affectation etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Affectation departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public Groupe getGroupe() {
        return this.groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Affectation groupe(Groupe groupe) {
        this.setGroupe(groupe);
        return this;
    }

    public Societe getSociete() {
        return this.societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Affectation societe(Societe societe) {
        this.setSociete(societe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affectation)) {
            return false;
        }
        return id != null && id.equals(((Affectation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affectation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
