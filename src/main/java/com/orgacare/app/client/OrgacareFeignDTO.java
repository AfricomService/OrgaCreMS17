package com.orgacare.app.client;

public class OrgacareFeignDTO {

    private Long id;
    private String orgaCode;

    public OrgacareFeignDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgaCode() {
        return orgaCode;
    }

    public void setOrgaCode(String orgaCode) {
        this.orgaCode = orgaCode;
    }
}
