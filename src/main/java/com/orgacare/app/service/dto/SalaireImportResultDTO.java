package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Résultat d'un import Excel de salaires.
 */
public class SalaireImportResultDTO implements Serializable {

    private int total;
    private int imported;
    private List<String> errors = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getImported() {
        return imported;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
