package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Affectation} entity.
 */
public class AffectationDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeAffectation type;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateAction;

    private ZonedDateTime dateFin;

    @NotNull
    private Etat etat;

    private DepartementDTO departement;

    private GroupeDTO groupe;

    private SocieteDTO societe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeAffectation getType() {
        return type;
    }

    public void setType(TypeAffectation type) {
        this.type = type;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
    }

    public GroupeDTO getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeDTO groupe) {
        this.groupe = groupe;
    }

    public SocieteDTO getSociete() {
        return societe;
    }

    public void setSociete(SocieteDTO societe) {
        this.societe = societe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationDTO)) {
            return false;
        }

        AffectationDTO affectationDTO = (AffectationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affectationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", etat='" + getEtat() + "'" +
            ", departement=" + getDepartement() +
            ", groupe=" + getGroupe() +
            ", societe=" + getSociete() +
            "}";
    }
}
