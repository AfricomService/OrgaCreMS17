package com.orgacare.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ThemeFormation.
 */
@Entity
@Table(name = "theme_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ThemeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "cout")
    private Double cout;

    @Column(name = "duree_validite_certificat")
    private Integer duree_validite_certificat;

    @Column(name = "notifier_expiration_avant")
    private Integer notifier_expiration_avant;

    @Column(name = "duree_anticipation_utilisateur")
    private Integer duree_anticipation_utilisateur;

    @Column(name = "cout_formation_individuelle")
    private Double cout_formation_individuelle;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ThemeFormation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return this.theme;
    }

    public ThemeFormation theme(String theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Double getCout() {
        return this.cout;
    }

    public ThemeFormation cout(Double cout) {
        this.setCout(cout);
        return this;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public Integer getDuree_validite_certificat() {
        return this.duree_validite_certificat;
    }

    public ThemeFormation duree_validite_certificat(Integer duree_validite_certificat) {
        this.setDuree_validite_certificat(duree_validite_certificat);
        return this;
    }

    public void setDuree_validite_certificat(Integer duree_validite_certificat) {
        this.duree_validite_certificat = duree_validite_certificat;
    }

    public Integer getNotifier_expiration_avant() {
        return this.notifier_expiration_avant;
    }

    public ThemeFormation notifier_expiration_avant(Integer notifier_expiration_avant) {
        this.setNotifier_expiration_avant(notifier_expiration_avant);
        return this;
    }

    public void setNotifier_expiration_avant(Integer notifier_expiration_avant) {
        this.notifier_expiration_avant = notifier_expiration_avant;
    }

    public Integer getDuree_anticipation_utilisateur() {
        return this.duree_anticipation_utilisateur;
    }

    public ThemeFormation duree_anticipation_utilisateur(Integer duree_anticipation_utilisateur) {
        this.setDuree_anticipation_utilisateur(duree_anticipation_utilisateur);
        return this;
    }

    public void setDuree_anticipation_utilisateur(Integer duree_anticipation_utilisateur) {
        this.duree_anticipation_utilisateur = duree_anticipation_utilisateur;
    }

    public Double getCout_formation_individuelle() {
        return this.cout_formation_individuelle;
    }

    public ThemeFormation cout_formation_individuelle(Double cout_formation_individuelle) {
        this.setCout_formation_individuelle(cout_formation_individuelle);
        return this;
    }

    public void setCout_formation_individuelle(Double cout_formation_individuelle) {
        this.cout_formation_individuelle = cout_formation_individuelle;
    }

    public String getStatus() {
        return this.status;
    }

    public ThemeFormation status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return this.comments;
    }

    public ThemeFormation comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeFormation)) {
            return false;
        }
        return id != null && id.equals(((ThemeFormation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeFormation{" +
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
