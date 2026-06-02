package com.orgacare.app.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CONFIGMANAGE")
public interface ConfigManageRestClient {
    @GetMapping("/api/orgacares/organigramme-codes")
    List<OrgacareFeignDTO> getAllOrganigrammesCodes();
}
