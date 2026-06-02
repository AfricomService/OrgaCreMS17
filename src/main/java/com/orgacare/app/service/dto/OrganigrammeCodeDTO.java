package com.orgacare.app.service.dto;

import java.io.Serializable;

public class OrganigrammeCodeDTO implements Serializable {

    private Long id;
    private String code;

    public OrganigrammeCodeDTO(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
