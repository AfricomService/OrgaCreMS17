package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.FormeJuridique} entity.
 */
public class FormeJuridiqueDTO implements Serializable {

    private Long id;

    private String abreviation;

    private String nom;

    private ZonedDateTime dateCreation;

    private String etat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormeJuridiqueDTO)) {
            return false;
        }

        FormeJuridiqueDTO formeJuridiqueDTO = (FormeJuridiqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formeJuridiqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormeJuridiqueDTO{" +
            "id=" + getId() +
            ", abreviation='" + getAbreviation() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
