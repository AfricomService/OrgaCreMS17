package com.orgacare.app.service.dto;

import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.io.Serializable;

public class PersonneAffectationDTO implements Serializable {

    private Long personneId;
    private String matricule;
    private String nomPrenom;
    private TypeAffectation typeAffectation;

    public PersonneAffectationDTO() {}

    public PersonneAffectationDTO(Long personneId, String matricule, String nomPrenom, TypeAffectation typeAffectation) {
        this.personneId = personneId;
        this.matricule = matricule;
        this.nomPrenom = nomPrenom;
        this.typeAffectation = typeAffectation;
    }

    public Long getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public TypeAffectation getTypeAffectation() {
        return typeAffectation;
    }

    public void setTypeAffectation(TypeAffectation typeAffectation) {
        this.typeAffectation = typeAffectation;
    }
}
