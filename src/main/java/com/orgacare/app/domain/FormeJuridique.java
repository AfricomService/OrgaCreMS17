package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormeJuridique.
 */
@Entity
@Table(name = "forme_juridique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FormeJuridique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "abreviation")
    private String abreviation;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "etat")
    private String etat;

    @OneToMany(mappedBy = "formeJuridiquee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organigrammes", "sites", "contrats", "formeJuridiquee" }, allowSetters = true)
    private Set<Societe> societes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormeJuridique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviation() {
        return this.abreviation;
    }

    public FormeJuridique abreviation(String abreviation) {
        this.setAbreviation(abreviation);
        return this;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getNom() {
        return this.nom;
    }

    public FormeJuridique nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public FormeJuridique dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getEtat() {
        return this.etat;
    }

    public FormeJuridique etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<Societe> getSocietes() {
        return this.societes;
    }

    public void setSocietes(Set<Societe> societes) {
        if (this.societes != null) {
            this.societes.forEach(i -> i.setFormeJuridiquee(null));
        }
        if (societes != null) {
            societes.forEach(i -> i.setFormeJuridiquee(this));
        }
        this.societes = societes;
    }

    public FormeJuridique societes(Set<Societe> societes) {
        this.setSocietes(societes);
        return this;
    }

    public FormeJuridique addSociete(Societe societe) {
        this.societes.add(societe);
        societe.setFormeJuridiquee(this);
        return this;
    }

    public FormeJuridique removeSociete(Societe societe) {
        this.societes.remove(societe);
        societe.setFormeJuridiquee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormeJuridique)) {
            return false;
        }
        return id != null && id.equals(((FormeJuridique) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormeJuridique{" +
            "id=" + getId() +
            ", abreviation='" + getAbreviation() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
