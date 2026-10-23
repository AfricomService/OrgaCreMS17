package com.orgacare.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PieceJointe.
 */
@Entity
@Table(name = "piece_jointe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PieceJointe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_fichier")
    private String nomFichier;

    @Column(name = "type")
    private String type;

    @Column(name = "fichier_url")
    private String fichierURL;

    @Column(name = "date_upload")
    private ZonedDateTime dateUpload;

    @Column(name = "formateur_id")
    private Long formateurId;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "formation_id")
    private Long formationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PieceJointe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return this.nomFichier;
    }

    public PieceJointe nomFichier(String nomFichier) {
        this.setNomFichier(nomFichier);
        return this;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getType() {
        return this.type;
    }

    public PieceJointe type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFichierURL() {
        return this.fichierURL;
    }

    public PieceJointe fichierURL(String fichierURL) {
        this.setFichierURL(fichierURL);
        return this;
    }

    public void setFichierURL(String fichierURL) {
        this.fichierURL = fichierURL;
    }

    public ZonedDateTime getDateUpload() {
        return this.dateUpload;
    }

    public PieceJointe dateUpload(ZonedDateTime dateUpload) {
        this.setDateUpload(dateUpload);
        return this;
    }

    public void setDateUpload(ZonedDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Long getFormateurId() {
        return this.formateurId;
    }

    public PieceJointe formateurId(Long formateurId) {
        this.setFormateurId(formateurId);
        return this;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public Long getSessionId() {
        return this.sessionId;
    }

    public PieceJointe sessionId(Long sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getFormationId() {
        return this.formationId;
    }

    public PieceJointe formationId(Long formationId) {
        this.setFormationId(formationId);
        return this;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PieceJointe)) {
            return false;
        }
        return id != null && id.equals(((PieceJointe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointe{" +
            "id=" + getId() +
            ", nomFichier='" + getNomFichier() + "'" +
            ", type='" + getType() + "'" +
            ", fichierURL='" + getFichierURL() + "'" +
            ", dateUpload='" + getDateUpload() + "'" +
            ", formateurId=" + getFormateurId() +
            ", sessionId=" + getSessionId() +
            ", formationId=" + getFormationId() +
            "}";
    }
}
