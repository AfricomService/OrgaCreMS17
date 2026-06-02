package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Organigramme} entity.
 */
public class OrganigrammeDTO implements Serializable {

    private Long id;

    private String code;

    private String nom;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateAction;

    private ZonedDateTime dateExpiration;

    @NotNull
    private Etat etat;

    private Long societeId;

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

    public ZonedDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public ZonedDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(ZonedDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganigrammeDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganigrammeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganigrammeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateAction='" + getDateAction() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", etat='" + getEtat() + "'" +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
