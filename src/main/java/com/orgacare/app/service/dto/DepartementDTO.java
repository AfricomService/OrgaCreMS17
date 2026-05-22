package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Departement} entity.
 */
public class DepartementDTO implements Serializable {

    private Long id;

    private String code;

    private String nom;

    @NotNull
    private Etat status;

    private String email;

    private OrganigrammeDTO organigramme;

    private SiteDTO site;

    private DepartementDTO departementParent;

    private Set<PersonneDTO> personnes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Etat getStatus() {
        return status;
    }

    public void setStatus(Etat status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OrganigrammeDTO getOrganigramme() {
        return organigramme;
    }

    public void setOrganigramme(OrganigrammeDTO organigramme) {
        this.organigramme = organigramme;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public DepartementDTO getDepartementParent() {
        return departementParent;
    }

    public void setDepartementParent(DepartementDTO departementParent) {
        this.departementParent = departementParent;
    }

    public Set<PersonneDTO> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(Set<PersonneDTO> personnes) {
        this.personnes = personnes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", status='" + getStatus() + "'" +
            ", email='" + getEmail() + "'" +
            ", organigramme=" + getOrganigramme() +
            ", site=" + getSite() +
            ", departementParent=" + getDepartementParent() +
            ", personnes=" + getPersonnes() +
            "}";
    }
}
