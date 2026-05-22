package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.EtatContractuelle;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Personne} entity.
 */
public class PersonneDTO implements Serializable {

    private Long id;

    private String matricule;

    private String nomPrenom;

    private String email;

    private String numTelephone;

    private String genre;

    private String cin;

    @NotNull
    private Etat etat;

    @NotNull
    private EtatContractuelle etatContractuelle;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateDebutContrat;

    private Long idContratActif;

    private Long idTypeContratActif;

    private Long userId;

    private AffectationDTO affectation;

    private GradeDTO grade;

    private FonctionDTO fonction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public EtatContractuelle getEtatContractuelle() {
        return etatContractuelle;
    }

    public void setEtatContractuelle(EtatContractuelle etatContractuelle) {
        this.etatContractuelle = etatContractuelle;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateDebutContrat() {
        return dateDebutContrat;
    }

    public void setDateDebutContrat(ZonedDateTime dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public Long getIdContratActif() {
        return idContratActif;
    }

    public void setIdContratActif(Long idContratActif) {
        this.idContratActif = idContratActif;
    }

    public Long getIdTypeContratActif() {
        return idTypeContratActif;
    }

    public void setIdTypeContratActif(Long idTypeContratActif) {
        this.idTypeContratActif = idTypeContratActif;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AffectationDTO getAffectation() {
        return affectation;
    }

    public void setAffectation(AffectationDTO affectation) {
        this.affectation = affectation;
    }

    public GradeDTO getGrade() {
        return grade;
    }

    public void setGrade(GradeDTO grade) {
        this.grade = grade;
    }

    public FonctionDTO getFonction() {
        return fonction;
    }

    public void setFonction(FonctionDTO fonction) {
        this.fonction = fonction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        PersonneDTO personneDTO = (PersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneDTO{" +
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
            ", affectation=" + getAffectation() +
            ", grade=" + getGrade() +
            ", fonction=" + getFonction() +
            "}";
    }
}
