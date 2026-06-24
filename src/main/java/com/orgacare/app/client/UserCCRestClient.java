package com.orgacare.app.client;

import com.orgacare.app.service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AuthorizedFeignClient(name = "CORRESPCARE")
public interface UserCCRestClient {
    @GetMapping("/api/admin/users/id/{id}")
    UserDTO getUserById(@PathVariable("id") String id);
}
