package com.orgacare.app.client;

import com.orgacare.app.config.KeycloakAdminProperties;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakAdminClient {

    private final Logger log = LoggerFactory.getLogger(KeycloakAdminClient.class);

    private final KeycloakAdminProperties keycloakAdminProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    // Injection par constructeur (bonne pratique)
    public KeycloakAdminClient(KeycloakAdminProperties keycloakAdminProperties) {
        this.keycloakAdminProperties = keycloakAdminProperties;
    }

    private String getAdminToken() {
        String tokenUrl =
            keycloakAdminProperties.getServerUrl() + "/realms/" + keycloakAdminProperties.getRealm() + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", keycloakAdminProperties.getClientId());
        body.add("client_secret", keycloakAdminProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
            throw new IllegalStateException("Impossible d'obtenir le token admin Keycloak");
        }
        return (String) response.getBody().get("access_token");
    }

    public void createUser(String login, String firstName, String lastName, String email, String password) {
        String adminToken = getAdminToken();
        String usersUrl = keycloakAdminProperties.getServerUrl() + "/admin/realms/" + keycloakAdminProperties.getRealm() + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", password);
        credential.put("temporary", false);

        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", login);
        userRepresentation.put("firstName", firstName);
        userRepresentation.put("lastName", lastName);
        userRepresentation.put("email", email);
        userRepresentation.put("enabled", true);
        userRepresentation.put("emailVerified", true);
        userRepresentation.put("credentials", Collections.singletonList(credential));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(usersUrl, request, Void.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("Utilisateur Keycloak créé avec succès : login={}", login);
            }
        } catch (HttpClientErrorException.Conflict e) {
            log.warn("L'utilisateur existe déjà dans Keycloak : login={}", login);
            throw new IllegalStateException("Un compte Keycloak existe déjà pour ce login : " + login);
        } catch (HttpClientErrorException e) {
            log.error("Erreur Keycloak lors de la création : status={} body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new IllegalStateException("Erreur Keycloak : " + e.getMessage());
        }
    }

    public boolean emailExists(String email) {
        String adminToken = getAdminToken();
        String searchUrl =
            keycloakAdminProperties.getServerUrl() +
            "/admin/realms/" +
            keycloakAdminProperties.getRealm() +
            "/users?email=" +
            email +
            "&exact=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<List> response = restTemplate.exchange(searchUrl, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        return response.getBody() != null && !response.getBody().isEmpty();
    }

    public boolean loginExists(String login) {
        String adminToken = getAdminToken();
        String searchUrl =
            keycloakAdminProperties.getServerUrl() +
            "/admin/realms/" +
            keycloakAdminProperties.getRealm() +
            "/users?username=" +
            login +
            "&exact=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<List> response = restTemplate.exchange(searchUrl, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        return response.getBody() != null && !response.getBody().isEmpty();
    }
}
