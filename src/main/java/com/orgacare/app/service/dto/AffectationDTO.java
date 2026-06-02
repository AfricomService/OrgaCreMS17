package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
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

    private Long personneId;

    private Long departementId;

    private Long groupeId;
    private List<Map<String, Object>> totalHierarchy;

    private Long societeId;

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

    public Long getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    public Long getDepartementId() {
        return departementId;
    }

    public void setDepartementId(Long departementId) {
        this.departementId = departementId;
    }

    public Long getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public List<Map<String, Object>> getTotalHierarchy() {
        return totalHierarchy;
    }

    public void setTotalHierarchy(List<Map<String, Object>> totalHierarchy) {
        this.totalHierarchy = totalHierarchy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationDTO)) {
            return false;
        }

        return id != null && id.equals(((AffectationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
            ", personneId=" + getPersonneId() +
            ", departementId=" + getDepartementId() +
            ", groupeId=" + getGroupeId() +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
