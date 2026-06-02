package com.orgacare.app.service.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartementTreeDTO {

    private Long id;
    private String code;
    private String nom;
    private String email;
    private List<DepartementTreeDTO> children = new ArrayList<>();

    /**
     * Map clé = TypeAffectation.name() (ex: "CHEF", "MEMBRE", "ASSISTANT", "INTERIM")
     * valeur = liste de PersonneDTO (id, nomPrenom, matricule, ...)
     *
     * Ce champ peut rester vide pour les nœuds intermédiaires si tu veux n'afficher
     * les affectations que sur les feuilles.
     */
    private Map<String, List<PersonneDTO>> affectationsByType = new HashMap<>();

    public DepartementTreeDTO() {}

    public DepartementTreeDTO(Long id, String code, String nom, String email) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.email = email;
    }

    public DepartementTreeDTO(Long id, String code, String nom) {
        this(id, code, nom, null); // réutilise le constructeur 4-args
    }

    // Getters and setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<DepartementTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DepartementTreeDTO> children) {
        this.children = children;
    }

    public Map<String, List<PersonneDTO>> getAffectationsByType() {
        return affectationsByType;
    }

    public void setAffectationsByType(Map<String, List<PersonneDTO>> affectationsByType) {
        this.affectationsByType = affectationsByType;
    }
}
