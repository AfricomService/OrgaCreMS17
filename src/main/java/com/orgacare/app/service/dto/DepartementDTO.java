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

    private Long siteId;

    private Long organigrammeId;

    private Long departementParentId;
    private Set<PersonneDTO> personnes = new HashSet<>();

    private Long societeId;
    private String societeRaisonSociale;
    private String organigrammeNom;
    private String chefs;
    private String membres;
    private String assistants;

    // Getters et setters pour les nouvelles propriétés
    public String getSocieteRaisonSociale() {
        return societeRaisonSociale;
    }

    public void setSocieteRaisonSociale(String societeRaisonSociale) {
        this.societeRaisonSociale = societeRaisonSociale;
    }

    public String getOrganigrammeNom() {
        return organigrammeNom;
    }

    public void setOrganigrammeNom(String organigrammeNom) {
        this.organigrammeNom = organigrammeNom;
    }

    public String getChefs() {
        return chefs;
    }

    public void setChefs(String chefs) {
        this.chefs = chefs;
    }

    public String getMembres() {
        return membres;
    }

    public void setMembres(String membres) {
        this.membres = membres;
    }

    public String getAssistants() {
        return assistants;
    }

    public void setAssistants(String assistants) {
        this.assistants = assistants;
    }

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

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getOrganigrammeId() {
        return organigrammeId;
    }

    public void setOrganigrammeId(Long organigrammeId) {
        this.organigrammeId = organigrammeId;
    }

    public Long getDepartementParentId() {
        return departementParentId;
    }

    public void setDepartementParentId(Long departementId) {
        this.departementParentId = departementId;
    }

    public Set<PersonneDTO> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(Set<PersonneDTO> personnes) {
        this.personnes = personnes;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public DepartementDTO() {}

    public DepartementDTO(
        Long id,
        String code,
        String nom,
        Etat status,
        String email,
        Long siteId,
        Long organigrammeId,
        Long departementParentId,
        Set<PersonneDTO> personnes
    ) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.status = status;
        this.email = email;
        this.siteId = siteId;
        this.organigrammeId = organigrammeId;
        this.departementParentId = departementParentId;
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

        return id != null && id.equals(((DepartementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
            ", siteId=" + getSiteId() +
            ", organigrammeId=" + getOrganigrammeId() +
            ", departementParentId=" + getDepartementParentId() +
            ", personnes='" + getPersonnes() + "'" +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
