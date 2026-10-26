package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.PieceJointe} entity.
 */
public class PieceJointeDTO implements Serializable {

    private Long id;

    private String nomFichier;

    private String type;

    private String fichierURL;

    private ZonedDateTime dateUpload;

    private Long formateurId;

    private Long sessionId;

    private Long formationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFichierURL() {
        return fichierURL;
    }

    public void setFichierURL(String fichierURL) {
        this.fichierURL = fichierURL;
    }

    public ZonedDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(ZonedDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PieceJointeDTO)) {
            return false;
        }

        PieceJointeDTO pieceJointeDTO = (PieceJointeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pieceJointeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointeDTO{" +
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
