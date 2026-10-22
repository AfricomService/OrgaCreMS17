package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.ThemeFormation} entity.
 */
public class ThemeFormationDTO implements Serializable {

    private Long id;

    @NotNull
    private String theme;

    private Double cout;

    private Integer duree_validite_certificat;

    private Integer notifier_expiration_avant;

    private Integer duree_anticipation_utilisateur;

    private Double cout_formation_individuelle;

    private String status;

    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Double getCout() {
        return cout;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public Integer getDuree_validite_certificat() {
        return duree_validite_certificat;
    }

    public void setDuree_validite_certificat(Integer duree_validite_certificat) {
        this.duree_validite_certificat = duree_validite_certificat;
    }

    public Integer getNotifier_expiration_avant() {
        return notifier_expiration_avant;
    }

    public void setNotifier_expiration_avant(Integer notifier_expiration_avant) {
        this.notifier_expiration_avant = notifier_expiration_avant;
    }

    public Integer getDuree_anticipation_utilisateur() {
        return duree_anticipation_utilisateur;
    }

    public void setDuree_anticipation_utilisateur(Integer duree_anticipation_utilisateur) {
        this.duree_anticipation_utilisateur = duree_anticipation_utilisateur;
    }

    public Double getCout_formation_individuelle() {
        return cout_formation_individuelle;
    }

    public void setCout_formation_individuelle(Double cout_formation_individuelle) {
        this.cout_formation_individuelle = cout_formation_individuelle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeFormationDTO)) {
            return false;
        }

        ThemeFormationDTO themeFormationDTO = (ThemeFormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, themeFormationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeFormationDTO{" +
            "id=" + getId() +
            ", theme='" + getTheme() + "'" +
            ", cout=" + getCout() +
            ", duree_validite_certificat=" + getDuree_validite_certificat() +
            ", notifier_expiration_avant=" + getNotifier_expiration_avant() +
            ", duree_anticipation_utilisateur=" + getDuree_anticipation_utilisateur() +
            ", cout_formation_individuelle=" + getCout_formation_individuelle() +
            ", status='" + getStatus() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
