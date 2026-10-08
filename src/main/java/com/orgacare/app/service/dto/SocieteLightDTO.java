package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.Etat;
import java.io.Serializable;

/**
 * Version légère de Societe (sans les champs @Lob).
 */
public class SocieteLightDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String raisonSociale;
    private String abreviation;
    private String codeSociete;
    private String codeOrganigramme;
    private Etat etat;

    public SocieteLightDTO() {}

    // Constructeur utilisé par la requête JPQL ("select new ...")
    public SocieteLightDTO(Long id, String raisonSociale, String abreviation, String codeSociete, String codeOrganigramme, Etat etat) {
        this.id = id;
        this.raisonSociale = raisonSociale;
        this.abreviation = abreviation;
        this.codeSociete = codeSociete;
        this.codeOrganigramme = codeOrganigramme;
        this.etat = etat;
    }

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

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }
}
