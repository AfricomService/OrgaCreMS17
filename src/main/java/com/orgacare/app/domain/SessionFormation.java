package com.orgacare.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionFormation.
 */
@Entity
@Table(name = "session_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SessionFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date_planification_du")
    private ZonedDateTime datePlanificationDu;

    @Column(name = "date_planification_au")
    private ZonedDateTime datePlanificationAu;

    @Column(name = "date_execution_du")
    private ZonedDateTime dateExecutionDu;

    @Column(name = "date_execution_au")
    private ZonedDateTime dateExecutionAu;

    @Column(name = "reglementaire")
    private Boolean reglementaire;

    @Column(name = "type_formation")
    private String typeFormation;

    @Column(name = "budget")
    private String budget;

    @Column(name = "cout_session")
    private Double coutSession;

    @Column(name = "statut")
    private String statut;

    @Column(name = "additional_informations")
    private String additionalInformations;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "formateur_id")
    private Long formateurId;

    @Column(name = "societe_id")
    private Long societeId;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionFormation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SessionFormation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getDatePlanificationDu() {
        return this.datePlanificationDu;
    }

    public SessionFormation datePlanificationDu(ZonedDateTime datePlanificationDu) {
        this.setDatePlanificationDu(datePlanificationDu);
        return this;
    }

    public void setDatePlanificationDu(ZonedDateTime datePlanificationDu) {
        this.datePlanificationDu = datePlanificationDu;
    }

    public ZonedDateTime getDatePlanificationAu() {
        return this.datePlanificationAu;
    }

    public SessionFormation datePlanificationAu(ZonedDateTime datePlanificationAu) {
        this.setDatePlanificationAu(datePlanificationAu);
        return this;
    }

    public void setDatePlanificationAu(ZonedDateTime datePlanificationAu) {
        this.datePlanificationAu = datePlanificationAu;
    }

    public ZonedDateTime getDateExecutionDu() {
        return this.dateExecutionDu;
    }

    public SessionFormation dateExecutionDu(ZonedDateTime dateExecutionDu) {
        this.setDateExecutionDu(dateExecutionDu);
        return this;
    }

    public void setDateExecutionDu(ZonedDateTime dateExecutionDu) {
        this.dateExecutionDu = dateExecutionDu;
    }

    public ZonedDateTime getDateExecutionAu() {
        return this.dateExecutionAu;
    }

    public SessionFormation dateExecutionAu(ZonedDateTime dateExecutionAu) {
        this.setDateExecutionAu(dateExecutionAu);
        return this;
    }

    public void setDateExecutionAu(ZonedDateTime dateExecutionAu) {
        this.dateExecutionAu = dateExecutionAu;
    }

    public Boolean getReglementaire() {
        return this.reglementaire;
    }

    public SessionFormation reglementaire(Boolean reglementaire) {
        this.setReglementaire(reglementaire);
        return this;
    }

    public void setReglementaire(Boolean reglementaire) {
        this.reglementaire = reglementaire;
    }

    public String getTypeFormation() {
        return this.typeFormation;
    }

    public SessionFormation typeFormation(String typeFormation) {
        this.setTypeFormation(typeFormation);
        return this;
    }

    public void setTypeFormation(String typeFormation) {
        this.typeFormation = typeFormation;
    }

    public String getBudget() {
        return this.budget;
    }

    public SessionFormation budget(String budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public Double getCoutSession() {
        return this.coutSession;
    }

    public SessionFormation coutSession(Double coutSession) {
        this.setCoutSession(coutSession);
        return this;
    }

    public void setCoutSession(Double coutSession) {
        this.coutSession = coutSession;
    }

    public String getStatut() {
        return this.statut;
    }

    public SessionFormation statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdditionalInformations() {
        return this.additionalInformations;
    }

    public SessionFormation additionalInformations(String additionalInformations) {
        this.setAdditionalInformations(additionalInformations);
        return this;
    }

    public void setAdditionalInformations(String additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public SessionFormation dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getThemeId() {
        return this.themeId;
    }

    public SessionFormation themeId(Long themeId) {
        this.setThemeId(themeId);
        return this;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getFormateurId() {
        return this.formateurId;
    }

    public SessionFormation formateurId(Long formateurId) {
        this.setFormateurId(formateurId);
        return this;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public Long getSocieteId() {
        return this.societeId;
    }

    public SessionFormation societeId(Long societeId) {
        this.setSocieteId(societeId);
        return this;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public String getDescription() {
        return this.description;
    }

    public SessionFormation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormation)) {
            return false;
        }
        return id != null && id.equals(((SessionFormation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormation{" +
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
