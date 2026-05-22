package com.orgacare.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Societe.
 */
@Entity
@Table(name = "societe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Societe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @Column(name = "abreviation")
    private String abreviation;

    @Column(name = "activite")
    private String activite;

    @Column(name = "forme_juridique")
    private String formeJuridique;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "code_postale")
    private String codePostale;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @Column(name = "region")
    private String region;

    @Column(name = "tel")
    private String tel;

    @Column(name = "fax")
    private String fax;

    @Column(name = "mail")
    private String mail;

    @Column(name = "site_internet")
    private String siteInternet;

    @Column(name = "matricule_fiscale")
    private String matriculeFiscale;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Lob
    @Column(name = "images_site_principale")
    private byte[] imagesSitePrincipale;

    @Column(name = "images_site_principale_content_type")
    private String imagesSitePrincipaleContentType;

    @Column(name = "holding")
    private String holding;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private Etat etat;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "date_activation")
    private ZonedDateTime dateActivation;

    @Column(name = "date_cloture")
    private ZonedDateTime dateCloture;

    @Lob
    @Column(name = "import_template")
    private byte[] importTemplate;

    @Column(name = "import_template_content_type")
    private String importTemplateContentType;

    @Column(name = "code_societe", unique = true)
    private String codeSociete;

    @NotNull
    @Column(name = "code_organigramme", nullable = false)
    private String codeOrganigramme;

    @OneToMany(mappedBy = "societe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "societe" }, allowSetters = true)
    private Set<Organigramme> organigrammes = new HashSet<>();

    @OneToMany(mappedBy = "societe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "societe" }, allowSetters = true)
    private Set<Site> sites = new HashSet<>();

    @OneToMany(mappedBy = "societe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "societe", "typeContrat", "personne" }, allowSetters = true)
    private Set<Contrat> contrats = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "societes" }, allowSetters = true)
    private FormeJuridique formeJuridiquee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Societe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return this.raisonSociale;
    }

    public Societe raisonSociale(String raisonSociale) {
        this.setRaisonSociale(raisonSociale);
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAbreviation() {
        return this.abreviation;
    }

    public Societe abreviation(String abreviation) {
        this.setAbreviation(abreviation);
        return this;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getActivite() {
        return this.activite;
    }

    public Societe activite(String activite) {
        this.setActivite(activite);
        return this;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getFormeJuridique() {
        return this.formeJuridique;
    }

    public Societe formeJuridique(String formeJuridique) {
        this.setFormeJuridique(formeJuridique);
        return this;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Societe adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostale() {
        return this.codePostale;
    }

    public Societe codePostale(String codePostale) {
        this.setCodePostale(codePostale);
        return this;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getVille() {
        return this.ville;
    }

    public Societe ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return this.pays;
    }

    public Societe pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getRegion() {
        return this.region;
    }

    public Societe region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTel() {
        return this.tel;
    }

    public Societe tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return this.fax;
    }

    public Societe fax(String fax) {
        this.setFax(fax);
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return this.mail;
    }

    public Societe mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSiteInternet() {
        return this.siteInternet;
    }

    public Societe siteInternet(String siteInternet) {
        this.setSiteInternet(siteInternet);
        return this;
    }

    public void setSiteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
    }

    public String getMatriculeFiscale() {
        return this.matriculeFiscale;
    }

    public Societe matriculeFiscale(String matriculeFiscale) {
        this.setMatriculeFiscale(matriculeFiscale);
        return this;
    }

    public void setMatriculeFiscale(String matriculeFiscale) {
        this.matriculeFiscale = matriculeFiscale;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Societe logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Societe logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getImagesSitePrincipale() {
        return this.imagesSitePrincipale;
    }

    public Societe imagesSitePrincipale(byte[] imagesSitePrincipale) {
        this.setImagesSitePrincipale(imagesSitePrincipale);
        return this;
    }

    public void setImagesSitePrincipale(byte[] imagesSitePrincipale) {
        this.imagesSitePrincipale = imagesSitePrincipale;
    }

    public String getImagesSitePrincipaleContentType() {
        return this.imagesSitePrincipaleContentType;
    }

    public Societe imagesSitePrincipaleContentType(String imagesSitePrincipaleContentType) {
        this.imagesSitePrincipaleContentType = imagesSitePrincipaleContentType;
        return this;
    }

    public void setImagesSitePrincipaleContentType(String imagesSitePrincipaleContentType) {
        this.imagesSitePrincipaleContentType = imagesSitePrincipaleContentType;
    }

    public String getHolding() {
        return this.holding;
    }

    public Societe holding(String holding) {
        this.setHolding(holding);
        return this;
    }

    public void setHolding(String holding) {
        this.holding = holding;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public Societe etat(Etat etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Societe dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateActivation() {
        return this.dateActivation;
    }

    public Societe dateActivation(ZonedDateTime dateActivation) {
        this.setDateActivation(dateActivation);
        return this;
    }

    public void setDateActivation(ZonedDateTime dateActivation) {
        this.dateActivation = dateActivation;
    }

    public ZonedDateTime getDateCloture() {
        return this.dateCloture;
    }

    public Societe dateCloture(ZonedDateTime dateCloture) {
        this.setDateCloture(dateCloture);
        return this;
    }

    public void setDateCloture(ZonedDateTime dateCloture) {
        this.dateCloture = dateCloture;
    }

    public byte[] getImportTemplate() {
        return this.importTemplate;
    }

    public Societe importTemplate(byte[] importTemplate) {
        this.setImportTemplate(importTemplate);
        return this;
    }

    public void setImportTemplate(byte[] importTemplate) {
        this.importTemplate = importTemplate;
    }

    public String getImportTemplateContentType() {
        return this.importTemplateContentType;
    }

    public Societe importTemplateContentType(String importTemplateContentType) {
        this.importTemplateContentType = importTemplateContentType;
        return this;
    }

    public void setImportTemplateContentType(String importTemplateContentType) {
        this.importTemplateContentType = importTemplateContentType;
    }

    public String getCodeSociete() {
        return this.codeSociete;
    }

    public Societe codeSociete(String codeSociete) {
        this.setCodeSociete(codeSociete);
        return this;
    }

    public void setCodeSociete(String codeSociete) {
        this.codeSociete = codeSociete;
    }

    public String getCodeOrganigramme() {
        return this.codeOrganigramme;
    }

    public Societe codeOrganigramme(String codeOrganigramme) {
        this.setCodeOrganigramme(codeOrganigramme);
        return this;
    }

    public void setCodeOrganigramme(String codeOrganigramme) {
        this.codeOrganigramme = codeOrganigramme;
    }

    public Set<Organigramme> getOrganigrammes() {
        return this.organigrammes;
    }

    public void setOrganigrammes(Set<Organigramme> organigrammes) {
        if (this.organigrammes != null) {
            this.organigrammes.forEach(i -> i.setSociete(null));
        }
        if (organigrammes != null) {
            organigrammes.forEach(i -> i.setSociete(this));
        }
        this.organigrammes = organigrammes;
    }

    public Societe organigrammes(Set<Organigramme> organigrammes) {
        this.setOrganigrammes(organigrammes);
        return this;
    }

    public Societe addOrganigramme(Organigramme organigramme) {
        this.organigrammes.add(organigramme);
        organigramme.setSociete(this);
        return this;
    }

    public Societe removeOrganigramme(Organigramme organigramme) {
        this.organigrammes.remove(organigramme);
        organigramme.setSociete(null);
        return this;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        if (this.sites != null) {
            this.sites.forEach(i -> i.setSociete(null));
        }
        if (sites != null) {
            sites.forEach(i -> i.setSociete(this));
        }
        this.sites = sites;
    }

    public Societe sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public Societe addSite(Site site) {
        this.sites.add(site);
        site.setSociete(this);
        return this;
    }

    public Societe removeSite(Site site) {
        this.sites.remove(site);
        site.setSociete(null);
        return this;
    }

    public Set<Contrat> getContrats() {
        return this.contrats;
    }

    public void setContrats(Set<Contrat> contrats) {
        if (this.contrats != null) {
            this.contrats.forEach(i -> i.setSociete(null));
        }
        if (contrats != null) {
            contrats.forEach(i -> i.setSociete(this));
        }
        this.contrats = contrats;
    }

    public Societe contrats(Set<Contrat> contrats) {
        this.setContrats(contrats);
        return this;
    }

    public Societe addContrat(Contrat contrat) {
        this.contrats.add(contrat);
        contrat.setSociete(this);
        return this;
    }

    public Societe removeContrat(Contrat contrat) {
        this.contrats.remove(contrat);
        contrat.setSociete(null);
        return this;
    }

    public FormeJuridique getFormeJuridiquee() {
        return this.formeJuridiquee;
    }

    public void setFormeJuridiquee(FormeJuridique formeJuridique) {
        this.formeJuridiquee = formeJuridique;
    }

    public Societe formeJuridiquee(FormeJuridique formeJuridique) {
        this.setFormeJuridiquee(formeJuridique);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Societe)) {
            return false;
        }
        return id != null && id.equals(((Societe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Societe{" +
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
            ", logoContentType='" + getLogoContentType() + "'" +
            ", imagesSitePrincipale='" + getImagesSitePrincipale() + "'" +
            ", imagesSitePrincipaleContentType='" + getImagesSitePrincipaleContentType() + "'" +
            ", holding='" + getHolding() + "'" +
            ", etat='" + getEtat() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateActivation='" + getDateActivation() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", importTemplate='" + getImportTemplate() + "'" +
            ", importTemplateContentType='" + getImportTemplateContentType() + "'" +
            ", codeSociete='" + getCodeSociete() + "'" +
            ", codeOrganigramme='" + getCodeOrganigramme() + "'" +
            "}";
    }
}
