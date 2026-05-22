package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.EtatContractuelle;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "nom_prenom")
    private String nomPrenom;

    @Column(name = "email")
    private String email;

    @Column(name = "num_telephone")
    private String numTelephone;

    @Column(name = "genre")
    private String genre;

    @Column(name = "cin")
    private String cin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Etat etat;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_contractuelle", nullable = false)
    private EtatContractuelle etatContractuelle;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "date_debut_contrat")
    private ZonedDateTime dateDebutContrat;

    @Column(name = "id_contrat_actif")
    private Long idContratActif;

    @Column(name = "id_type_contrat_actif")
    private Long idTypeContratActif;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "societe", "typeContrat", "personne" }, allowSetters = true)
    private Set<Contrat> contrats = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "departement", "groupe", "societe" }, allowSetters = true)
    private Affectation affectation;

    @ManyToOne
    private Grade grade;

    @ManyToOne
    private Fonction fonction;

    @ManyToMany(mappedBy = "personnes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organigramme", "site", "departementParent", "personnes" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Personne matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public Personne nomPrenom(String nomPrenom) {
        this.setNomPrenom(nomPrenom);
        return this;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Personne email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTelephone() {
        return this.numTelephone;
    }

    public Personne numTelephone(String numTelephone) {
        this.setNumTelephone(numTelephone);
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getGenre() {
        return this.genre;
    }

    public Personne genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCin() {
        return this.cin;
    }

    public Personne cin(String cin) {
        this.setCin(cin);
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Personne etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public EtatContractuelle getEtatContractuelle() {
        return this.etatContractuelle;
    }

    public Personne etatContractuelle(EtatContractuelle etatContractuelle) {
        this.setEtatContractuelle(etatContractuelle);
        return this;
    }

    public void setEtatContractuelle(EtatContractuelle etatContractuelle) {
        this.etatContractuelle = etatContractuelle;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Personne dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateDebutContrat() {
        return this.dateDebutContrat;
    }

    public Personne dateDebutContrat(ZonedDateTime dateDebutContrat) {
        this.setDateDebutContrat(dateDebutContrat);
        return this;
    }

    public void setDateDebutContrat(ZonedDateTime dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public Long getIdContratActif() {
        return this.idContratActif;
    }

    public Personne idContratActif(Long idContratActif) {
        this.setIdContratActif(idContratActif);
        return this;
    }

    public void setIdContratActif(Long idContratActif) {
        this.idContratActif = idContratActif;
    }

    public Long getIdTypeContratActif() {
        return this.idTypeContratActif;
    }

    public Personne idTypeContratActif(Long idTypeContratActif) {
        this.setIdTypeContratActif(idTypeContratActif);
        return this;
    }

    public void setIdTypeContratActif(Long idTypeContratActif) {
        this.idTypeContratActif = idTypeContratActif;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Personne userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Contrat> getContrats() {
        return this.contrats;
    }

    public void setContrats(Set<Contrat> contrats) {
        if (this.contrats != null) {
            this.contrats.forEach(i -> i.setPersonne(null));
        }
        if (contrats != null) {
            contrats.forEach(i -> i.setPersonne(this));
        }
        this.contrats = contrats;
    }

    public Personne contrats(Set<Contrat> contrats) {
        this.setContrats(contrats);
        return this;
    }

    public Personne addContrat(Contrat contrat) {
        this.contrats.add(contrat);
        contrat.setPersonne(this);
        return this;
    }

    public Personne removeContrat(Contrat contrat) {
        this.contrats.remove(contrat);
        contrat.setPersonne(null);
        return this;
    }

    public Affectation getAffectation() {
        return this.affectation;
    }

    public void setAffectation(Affectation affectation) {
        this.affectation = affectation;
    }

    public Personne affectation(Affectation affectation) {
        this.setAffectation(affectation);
        return this;
    }

    public Grade getGrade() {
        return this.grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Personne grade(Grade grade) {
        this.setGrade(grade);
        return this;
    }

    public Fonction getFonction() {
        return this.fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Personne fonction(Fonction fonction) {
        this.setFonction(fonction);
        return this;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        if (this.departements != null) {
            this.departements.forEach(i -> i.removePersonne(this));
        }
        if (departements != null) {
            departements.forEach(i -> i.addPersonne(this));
        }
        this.departements = departements;
    }

    public Personne departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public Personne addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.getPersonnes().add(this);
        return this;
    }

    public Personne removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.getPersonnes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return id != null && id.equals(((Personne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nomPrenom='" + getNomPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTelephone='" + getNumTelephone() + "'" +
            ", genre='" + getGenre() + "'" +
            ", cin='" + getCin() + "'" +
            ", etat='" + getEtat() + "'" +
            ", etatContractuelle='" + getEtatContractuelle() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateDebutContrat='" + getDateDebutContrat() + "'" +
            ", idContratActif=" + getIdContratActif() +
            ", idTypeContratActif=" + getIdTypeContratActif() +
            ", userId=" + getUserId() +
            "}";
    }
}
