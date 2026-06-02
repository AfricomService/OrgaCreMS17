package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.orgacare.app.domain.Societe} entity.
 */
public class SocieteDTO implements Serializable {

    private Long id;

    @NotNull
    private String raisonSociale;

    private String abreviation;

    private String activite;

    private String formeJuridique;

    private String adresse;

    private String codePostale;

    private String ville;

    private String pays;

    private String region;

    private String tel;

    private String fax;

    private String mail;

    private String siteInternet;

    private String matriculeFiscale;

    @Lob
    private byte[] logo;

    private String logoContentType;

    @Lob
    private byte[] imagesSitePrincipale;

    private String imagesSitePrincipaleContentType;
    private String holding;

    @NotNull
    private Etat etat;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateActivation;

    private ZonedDateTime dateCloture;

    @Lob
    private byte[] importTemplate;

    private String importTemplateContentType;

    private String codeSociete;

    @NotNull
    private String codeOrganigramme;

    private Long formeJuridiqueeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSiteInternet() {
        return siteInternet;
    }

    public void setSiteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
    }

    public String getMatriculeFiscale() {
        return matriculeFiscale;
    }

    public void setMatriculeFiscale(String matriculeFiscale) {
        this.matriculeFiscale = matriculeFiscale;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getImagesSitePrincipale() {
        return imagesSitePrincipale;
    }

    public void setImagesSitePrincipale(byte[] imagesSitePrincipale) {
        this.imagesSitePrincipale = imagesSitePrincipale;
    }

    public String getImagesSitePrincipaleContentType() {
        return imagesSitePrincipaleContentType;
    }

    public void setImagesSitePrincipaleContentType(String imagesSitePrincipaleContentType) {
        this.imagesSitePrincipaleContentType = imagesSitePrincipaleContentType;
    }

    public String getHolding() {
        return holding;
    }

    public void setHolding(String holding) {
        this.holding = holding;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public ZonedDateTime getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(ZonedDateTime dateCloture) {
        this.dateCloture = dateCloture;
    }

    public byte[] getImportTemplate() {
        return importTemplate;
    }

    public void setImportTemplate(byte[] importTemplate) {
        this.importTemplate = importTemplate;
    }

    public String getImportTemplateContentType() {
        return importTemplateContentType;
    }

    public void setImportTemplateContentType(String importTemplateContentType) {
        this.importTemplateContentType = importTemplateContentType;
    }

    public String getCodeSociete() {
        return codeSociete;
    }

    public void setCodeSociete(String codeSociete) {
        this.codeSociete = codeSociete;
    }

    public String getCodeOrganigramme() {
        return codeOrganigramme;
    }

    public void setCodeOrganigramme(String codeOrganigramme) {
        this.codeOrganigramme = codeOrganigramme;
    }

    public Long getFormeJuridiqueeId() {
        return formeJuridiqueeId;
    }

    public void setFormeJuridiqueeId(Long formeJuridiqueId) {
        this.formeJuridiqueeId = formeJuridiqueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocieteDTO)) {
            return false;
        }

        return id != null && id.equals(((SocieteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocieteDTO{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", abreviation='" + getAbreviation() + "'" +
            ", activite='" + getActivite() + "'" +
            ", formeJuridique='" + getFormeJuridique() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", codePostale='" + getCodePostale() + "'" +
            ", ville='" + getVille() + "'" +
            ", pays='" + getPays() + "'" +
            ", region='" + getRegion() + "'" +
            ", tel='" + getTel() + "'" +
            ", fax='" + getFax() + "'" +
            ", mail='" + getMail() + "'" +
            ", siteInternet='" + getSiteInternet() + "'" +
            ", matriculeFiscale='" + getMatriculeFiscale() + "'" +
            ", logo='" + getLogo() + "'" +
            ", imagesSitePrincipale='" + getImagesSitePrincipale() + "'" +
            ", holding='" + getHolding() + "'" +
            ", etat='" + getEtat() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", importTemplate='" + getImportTemplate() + "'" +
            ", codeSociete='" + getCodeSociete() + "'" +
            ", codeOrganigramme='" + getCodeOrganigramme() + "'" +
            ", formeJuridiqueeId=" + getFormeJuridiqueeId() +
            "}";
    }
}
