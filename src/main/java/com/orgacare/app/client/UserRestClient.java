package com.orgacare.app.client;

import com.orgacare.app.service.dto.UserDTO;
import com.orgacare.app.web.rest.vm.ManagedUserVM;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORGACAREGATEWAY")
public interface UserRestClient {
    // Création utilisateur (admin)
    //    @PostMapping("/api/admin/users")
    //    UserDTO createUser(@RequestBody UserDTO userDTO);

    // Enregistrement public (si disponible)
    //    @PostMapping("/api/register")
    //    void registerUser(@RequestBody ManagedUserVM managedUserVM);

    // Vérifications
    //    @GetMapping("/api/users/check-login/{login}")
    //    boolean checkLoginExists(@PathVariable("login") String login);

    //    @GetMapping("/api/users/check-email/{email}")
    //    boolean checkEmailExists(@PathVariable("email") String email);

    //    @GetMapping("/api/users/check-login-by-name/{firstName}/{lastName}")
    //    boolean checkLoginByNameExists(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName);
}
