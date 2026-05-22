package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.TypeContrat} entity.
 */
public class TypeContratDTO implements Serializable {

    private Long id;

    private String nom;

    private String abreviation;

    private LocalDate dateCreation;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeContratDTO)) {
            return false;
        }

        TypeContratDTO typeContratDTO = (TypeContratDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeContratDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeContratDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", abreviation='" + getAbreviation() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
