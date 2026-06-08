package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;

public class AffecterPersonneRequest implements Serializable {

    @NotNull
    private Long personneId;

    @NotNull
    private Long departementId;

    private Long societeId;

    @NotNull
    private TypeAffectation type;

    private ZonedDateTime dateAction;

    private ZonedDateTime dateFin;

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

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public TypeAffectation getType() {
        return type;
    }

    public void setType(TypeAffectation type) {
        this.type = type;
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
}
