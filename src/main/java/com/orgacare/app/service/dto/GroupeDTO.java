package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Groupe} entity.
 */
public class GroupeDTO implements Serializable {

    private Long id;

    private String code;

    private String nom;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateActivation;

    @NotNull
    private Etat etat;

    private OrganigrammeDTO organigramme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public OrganigrammeDTO getOrganigramme() {
        return organigramme;
    }

    public void setOrganigramme(OrganigrammeDTO organigramme) {
        this.organigramme = organigramme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupeDTO)) {
            return false;
        }

        GroupeDTO groupeDTO = (GroupeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", etat='" + getEtat() + "'" +
            ", organigramme=" + getOrganigramme() +
            "}";
    }
}
