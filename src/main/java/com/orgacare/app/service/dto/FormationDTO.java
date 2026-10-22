package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.Formation} entity.
 */
public class FormationDTO implements Serializable {

    private Long id;

    private String statut;

    private String etat;

    private Double note;

    private ZonedDateTime dateObtentionCertificat;

    private ZonedDateTime dateExpirationCertificat;

    private Boolean notificationEnvoyee;

    private String commentaire;

    private Long sessionId;

    private Long personneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public ZonedDateTime getDateObtentionCertificat() {
        return dateObtentionCertificat;
    }

    public void setDateObtentionCertificat(ZonedDateTime dateObtentionCertificat) {
        this.dateObtentionCertificat = dateObtentionCertificat;
    }

    public ZonedDateTime getDateExpirationCertificat() {
        return dateExpirationCertificat;
    }

    public void setDateExpirationCertificat(ZonedDateTime dateExpirationCertificat) {
        this.dateExpirationCertificat = dateExpirationCertificat;
    }

    public Boolean getNotificationEnvoyee() {
        return notificationEnvoyee;
    }

    public void setNotificationEnvoyee(Boolean notificationEnvoyee) {
        this.notificationEnvoyee = notificationEnvoyee;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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
        if (!(o instanceof FormationDTO)) {
            return false;
        }

        FormationDTO formationDTO = (FormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationDTO{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", etat='" + getEtat() + "'" +
            ", note=" + getNote() +
            ", dateObtentionCertificat='" + getDateObtentionCertificat() + "'" +
            ", dateExpirationCertificat='" + getDateExpirationCertificat() + "'" +
            ", notificationEnvoyee='" + getNotificationEnvoyee() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", sessionId=" + getSessionId() +
            ", personneId=" + getPersonneId() +
            "}";
    }
}
