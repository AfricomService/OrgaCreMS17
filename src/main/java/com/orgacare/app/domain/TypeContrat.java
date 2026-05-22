package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeContrat.
 */
@Entity
@Table(name = "type_contrat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeContrat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "abreviation")
    private String abreviation;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "typeContrat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "societe", "typeContrat", "personne" }, allowSetters = true)
    private Set<Contrat> contrats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeContrat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public TypeContrat nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviation() {
        return this.abreviation;
    }

    public TypeContrat abreviation(String abreviation) {
        this.setAbreviation(abreviation);
        return this;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public TypeContrat dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatus() {
        return this.status;
    }

    public TypeContrat status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Contrat> getContrats() {
        return this.contrats;
    }

    public void setContrats(Set<Contrat> contrats) {
        if (this.contrats != null) {
            this.contrats.forEach(i -> i.setTypeContrat(null));
        }
        if (contrats != null) {
            contrats.forEach(i -> i.setTypeContrat(this));
        }
        this.contrats = contrats;
    }

    public TypeContrat contrats(Set<Contrat> contrats) {
        this.setContrats(contrats);
        return this;
    }

    public TypeContrat addContrat(Contrat contrat) {
        this.contrats.add(contrat);
        contrat.setTypeContrat(this);
        return this;
    }

    public TypeContrat removeContrat(Contrat contrat) {
        this.contrats.remove(contrat);
        contrat.setTypeContrat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeContrat)) {
            return false;
        }
        return id != null && id.equals(((TypeContrat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeContrat{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", abreviation='" + getAbreviation() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
