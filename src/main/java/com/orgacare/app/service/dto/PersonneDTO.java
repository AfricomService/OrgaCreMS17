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

    @NotEmpty(message = "{error.NotEmpty}")
    private String nomPrenom;

    private String email;

    private String numTelephone;

    private String genre;

    private String cin;

    private Etat etat;

    private EtatContractuelle etatContractuelle;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateDebutContrat;

    private Long idContratActif;

    private Long idTypeContratActif;

    private Long affectationId;

    private Long gradeId;

    private Long fonctionId;

    private String role;

    private String userId;

    public PersonneDTO(Long id, String nomPrenom, String role) {
        this.id = id;
        this.nomPrenom = nomPrenom;
        this.role = role;
    }

    // Getters et Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public Long getAffectationId() {
        return affectationId;
    }

    public void setAffectationId(Long affectationId) {
        this.affectationId = affectationId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public PersonneDTO() {}

    public PersonneDTO(Long id, String nomPrenom) {
        this.id = id;
        this.nomPrenom = nomPrenom;
    }

    public Long getFonctionId() {
        return fonctionId;
    }

    public void setFonctionId(Long fonctionId) {
        this.fonctionId = fonctionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonneDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
            ", affectationId=" + getAffectationId() +
            ", gradeId=" + getGradeId() +
            ", fonctionId=" + getFonctionId() +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
