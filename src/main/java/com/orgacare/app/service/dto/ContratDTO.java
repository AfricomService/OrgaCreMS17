package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.Contrat} entity.
 */
public class ContratDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateDebut;

    private ZonedDateTime dateFin;

    private String type;

    private String status;
    private String nomPersonne;
    private String raisonSocialeSociete;
    private String nomTypeContrat;

    private String matriculePersonne;
    private Long societeId;

    private Long personneId;

    private Long typeContratId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public String getMatriculePersonne() {
        return matriculePersonne;
    }

    public void setMatriculePersonne(String matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public Long getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    public Long getTypeContratId() {
        return typeContratId;
    }

    public void setTypeContratId(Long typeContratId) {
        this.typeContratId = typeContratId;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

    public void setNomPersonne(String NomPersonne) {
        this.nomPersonne = NomPersonne;
    }

    public String getRaisonSocialeSociete() {
        return raisonSocialeSociete;
    }

    public void setRaisonSocialeSociete(String RaisonSocialeSociete) {
        this.raisonSocialeSociete = RaisonSocialeSociete;
    }

    public String getNomTypeContrat() {
        return nomTypeContrat;
    }

    public void setNomTypeContrat(String NomTypeContrat) {
        this.nomTypeContrat = NomTypeContrat;
    }

    public ContratDTO() {}

    public ContratDTO(String NomPersonne, String RaisonSocialeSociete, String NomTypeContrat) {
        this.nomPersonne = NomPersonne;
        this.raisonSocialeSociete = RaisonSocialeSociete;
        this.nomTypeContrat = NomTypeContrat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContratDTO)) {
            return false;
        }

        return id != null && id.equals(((ContratDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "ContratDTO{" +
            "id=" + id +
            ", dateDebut=" + dateDebut +
            ", dateFin=" + dateFin +
            ", type='" + type + '\'' +
            ", status='" + status + '\'' +
            ", nomPersonne='" + nomPersonne + '\'' +
            ", raisonSocialeSociete='" + raisonSocialeSociete + '\'' +
            ", nomTypeContrat='" + nomTypeContrat + '\'' +
            ", societeId=" + societeId +
            ", personneId=" + personneId +
            ", typeContratId=" + typeContratId +
            '}';
    }
}
