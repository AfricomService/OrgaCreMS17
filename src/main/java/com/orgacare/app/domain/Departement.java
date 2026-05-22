package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Departement implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Etat status;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JsonIgnoreProperties(value = { "societe" }, allowSetters = true)
    private Organigramme organigramme;

    @ManyToOne
    @JsonIgnoreProperties(value = { "societe" }, allowSetters = true)
    private Site site;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organigramme", "site", "departementParent", "personnes" }, allowSetters = true)
    private Departement departementParent;

    @ManyToMany
    @JoinTable(
        name = "rel_departement__personne",
        joinColumns = @JoinColumn(name = "departement_id"),
        inverseJoinColumns = @JoinColumn(name = "personne_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contrats", "affectation", "grade", "fonction", "departements" }, allowSetters = true)
    private Set<Personne> personnes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Departement code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Departement nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Etat getStatus() {
        return this.status;
    }

    public Departement status(Etat status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Etat status) {
        this.status = status;
    }

    public String getEmail() {
        return this.email;
    }

    public Departement email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organigramme getOrganigramme() {
        return this.organigramme;
    }

    public void setOrganigramme(Organigramme organigramme) {
        this.organigramme = organigramme;
    }

    public Departement organigramme(Organigramme organigramme) {
        this.setOrganigramme(organigramme);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Departement site(Site site) {
        this.setSite(site);
        return this;
    }

    public Departement getDepartementParent() {
        return this.departementParent;
    }

    public void setDepartementParent(Departement departement) {
        this.departementParent = departement;
    }

    public Departement departementParent(Departement departement) {
        this.setDepartementParent(departement);
        return this;
    }

    public Set<Personne> getPersonnes() {
        return this.personnes;
    }

    public void setPersonnes(Set<Personne> personnes) {
        this.personnes = personnes;
    }

    public Departement personnes(Set<Personne> personnes) {
        this.setPersonnes(personnes);
        return this;
    }

    public Departement addPersonne(Personne personne) {
        this.personnes.add(personne);
        personne.getDepartements().add(this);
        return this;
    }

    public Departement removePersonne(Personne personne) {
        this.personnes.remove(personne);
        personne.getDepartements().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", status='" + getStatus() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
