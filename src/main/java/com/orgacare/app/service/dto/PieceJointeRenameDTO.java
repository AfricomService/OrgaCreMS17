package com.orgacare.app.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PieceJointeRenameDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 200)
    private String nomFichier;

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
}
