package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Absence} entity.
 */
public class AbsenceDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateCreation;

    @NotNull
    private Etat etat;

    private ZonedDateTime dateDebut;

    private ZonedDateTime dateFin;

    private String motif;

    private PersonneDTO personneAbscent;

    private PersonneDTO personneRemplacant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public PersonneDTO getPersonneAbscent() {
        return personneAbscent;
    }

    public void setPersonneAbscent(PersonneDTO personneAbscent) {
        this.personneAbscent = personneAbscent;
    }

    public PersonneDTO getPersonneRemplacant() {
        return personneRemplacant;
    }

    public void setPersonneRemplacant(PersonneDTO personneRemplacant) {
        this.personneRemplacant = personneRemplacant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbsenceDTO)) {
            return false;
        }

        AbsenceDTO absenceDTO = (AbsenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, absenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbsenceDTO{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", etat='" + getEtat() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", motif='" + getMotif() + "'" +
            ", personneAbscent=" + getPersonneAbscent() +
            ", personneRemplacant=" + getPersonneRemplacant() +
            "}";
    }
}
