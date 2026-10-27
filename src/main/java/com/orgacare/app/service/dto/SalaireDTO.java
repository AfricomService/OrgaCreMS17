package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Salaire} entity.
 */
public class SalaireDTO implements Serializable {

    private Long id;

    @NotNull
    private Double valeur;

    private ZonedDateTime date;

    private Long personneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaireDTO)) {
            return false;
        }

        SalaireDTO salaireDTO = (SalaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaireDTO{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", date='" + getDate() + "'" +
            ", personneId=" + getPersonneId() +
            "}";
    }
}
