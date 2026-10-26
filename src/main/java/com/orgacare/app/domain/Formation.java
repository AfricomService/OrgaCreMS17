package com.orgacare.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "statut")
    private String statut;

    @Column(name = "etat")
    private String etat;

    @Column(name = "note")
    private Double note;

    @Column(name = "date_obtention_certificat")
    private ZonedDateTime dateObtentionCertificat;

    @Column(name = "date_expiration_certificat")
    private ZonedDateTime dateExpirationCertificat;

    @Column(name = "notification_envoyee")
    private Boolean notificationEnvoyee;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "personne_id")
    private Long personneId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return this.statut;
    }

    public Formation statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEtat() {
        return this.etat;
    }

    public Formation etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Double getNote() {
        return this.note;
    }

    public Formation note(Double note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public ZonedDateTime getDateObtentionCertificat() {
        return this.dateObtentionCertificat;
    }

    public Formation dateObtentionCertificat(ZonedDateTime dateObtentionCertificat) {
        this.setDateObtentionCertificat(dateObtentionCertificat);
        return this;
    }

    public void setDateObtentionCertificat(ZonedDateTime dateObtentionCertificat) {
        this.dateObtentionCertificat = dateObtentionCertificat;
    }

    public ZonedDateTime getDateExpirationCertificat() {
        return this.dateExpirationCertificat;
    }

    public Formation dateExpirationCertificat(ZonedDateTime dateExpirationCertificat) {
        this.setDateExpirationCertificat(dateExpirationCertificat);
        return this;
    }

    public void setDateExpirationCertificat(ZonedDateTime dateExpirationCertificat) {
        this.dateExpirationCertificat = dateExpirationCertificat;
    }

    public Boolean getNotificationEnvoyee() {
        return this.notificationEnvoyee;
    }

    public Formation notificationEnvoyee(Boolean notificationEnvoyee) {
        this.setNotificationEnvoyee(notificationEnvoyee);
        return this;
    }

    public void setNotificationEnvoyee(Boolean notificationEnvoyee) {
        this.notificationEnvoyee = notificationEnvoyee;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Formation commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getSessionId() {
        return this.sessionId;
    }

    public Formation sessionId(Long sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPersonneId() {
        return this.personneId;
    }

    public Formation personneId(Long personneId) {
        this.setPersonneId(personneId);
        return this;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation)) {
            return false;
        }
        return id != null && id.equals(((Formation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formation{" +
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
