package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.SessionFormation} entity.
 */
public class SessionFormationDTO implements Serializable {

    private Long id;

    private String title;

    private ZonedDateTime datePlanificationDu;

    private ZonedDateTime datePlanificationAu;

    private ZonedDateTime dateExecutionDu;

    private ZonedDateTime dateExecutionAu;

    private Boolean reglementaire;

    private String typeFormation;

    private String budget;

    private Double coutSession;

    private String statut;

    private String additionalInformations;

    private ZonedDateTime dateCreation;

    private Long themeId;

    private Long formateurId;

    private Long societeId;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getDatePlanificationDu() {
        return datePlanificationDu;
    }

    public void setDatePlanificationDu(ZonedDateTime datePlanificationDu) {
        this.datePlanificationDu = datePlanificationDu;
    }

    public ZonedDateTime getDatePlanificationAu() {
        return datePlanificationAu;
    }

    public void setDatePlanificationAu(ZonedDateTime datePlanificationAu) {
        this.datePlanificationAu = datePlanificationAu;
    }

    public ZonedDateTime getDateExecutionDu() {
        return dateExecutionDu;
    }

    public void setDateExecutionDu(ZonedDateTime dateExecutionDu) {
        this.dateExecutionDu = dateExecutionDu;
    }

    public ZonedDateTime getDateExecutionAu() {
        return dateExecutionAu;
    }

    public void setDateExecutionAu(ZonedDateTime dateExecutionAu) {
        this.dateExecutionAu = dateExecutionAu;
    }

    public Boolean getReglementaire() {
        return reglementaire;
    }

    public void setReglementaire(Boolean reglementaire) {
        this.reglementaire = reglementaire;
    }

    public String getTypeFormation() {
        return typeFormation;
    }

    public void setTypeFormation(String typeFormation) {
        this.typeFormation = typeFormation;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public Double getCoutSession() {
        return coutSession;
    }

    public void setCoutSession(Double coutSession) {
        this.coutSession = coutSession;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdditionalInformations() {
        return additionalInformations;
    }

    public void setAdditionalInformations(String additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormationDTO)) {
            return false;
        }

        SessionFormationDTO sessionFormationDTO = (SessionFormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionFormationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", datePlanificationDu='" + getDatePlanificationDu() + "'" +
            ", datePlanificationAu='" + getDatePlanificationAu() + "'" +
            ", dateExecutionDu='" + getDateExecutionDu() + "'" +
            ", dateExecutionAu='" + getDateExecutionAu() + "'" +
            ", reglementaire='" + getReglementaire() + "'" +
            ", typeFormation='" + getTypeFormation() + "'" +
            ", budget='" + getBudget() + "'" +
            ", coutSession=" + getCoutSession() +
            ", statut='" + getStatut() + "'" +
            ", additionalInformations='" + getAdditionalInformations() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", themeId=" + getThemeId() +
            ", formateurId=" + getFormateurId() +
            ", societeId=" + getSocieteId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
