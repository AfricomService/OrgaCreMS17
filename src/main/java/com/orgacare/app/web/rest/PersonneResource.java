package com.orgacare.app.web.rest;

import com.orgacare.app.client.KeycloakAdminClient;
import com.orgacare.app.client.UserRestClient;
import com.orgacare.app.service.DepartementService;
import com.orgacare.app.service.PersonneService;
import com.orgacare.app.service.dto.PersonneDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class PersonneResource {

    private final Logger log = LoggerFactory.getLogger(PersonneResource.class);
    private static final String ENTITY_NAME = "orgaCarePersonne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRestClient userRestClient;
    private final PersonneService personneService;
    private final DepartementService departementService;
    private final KeycloakAdminClient keycloakAdminClient;

    public PersonneResource(
        UserRestClient userRestClient,
        PersonneService personneService,
        DepartementService departementService,
        KeycloakAdminClient keycloakAdminClient
    ) {
        this.userRestClient = userRestClient;
        this.personneService = personneService;
        this.departementService = departementService;
        this.keycloakAdminClient = keycloakAdminClient;
    }

    @PostMapping("/personnes")
    public ResponseEntity<PersonneDTO> createPersonne(@Valid @RequestBody PersonneDTO personneDTO) throws URISyntaxException {
        log.debug("REST request to save Personne : {}", personneDTO);
        if (personneDTO.getId() != null) {
            throw new BadRequestAlertException("A new personne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonneDTO result = personneService.save(personneDTO);
        return ResponseEntity
            .created(new URI("/api/personnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/personnes/{id}/create-account")
    public ResponseEntity<Void> createAccountForPersonne(
        @PathVariable Long id,
        @RequestParam String password,
        @RequestParam String confirmPassword
    ) {
        log.debug("REST request to create a Keycloak account for Personne ID : {}", id);

        PersonneDTO personne = personneService
            .findOne(id)
            .orElseThrow(() -> new BadRequestAlertException("Personne introuvable", ENTITY_NAME, "notfound"));

        if (personne.getEmail() == null || personne.getEmail().trim().isEmpty()) {
            throw new BadRequestAlertException("Email manquant pour la personne", ENTITY_NAME, "emailempty");
        }
        if (keycloakAdminClient.emailExists(personne.getEmail())) {
            throw new BadRequestAlertException("Un compte existe déjà pour cet email", ENTITY_NAME, "accountexists");
        }
        if (!password.equals(confirmPassword)) {
            throw new BadRequestAlertException("Les mots de passe ne correspondent pas", ENTITY_NAME, "passwordmismatch");
        }

        String[] parts = personne.getNomPrenom() != null ? personne.getNomPrenom().trim().split(" ", 2) : new String[] { "", "" };
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        String login = (personne.getMatricule() != null && !personne.getMatricule().trim().isEmpty())
            ? sanitizeLogin(personne.getMatricule().trim())
            : sanitizeLogin((lastName + "." + firstName).toLowerCase());

        String baseLogin = login;
        int counter = 1;
        while (keycloakAdminClient.loginExists(login)) {
            login = baseLogin + counter++;
        }

        try {
            keycloakAdminClient.createUser(login, firstName, lastName, personne.getEmail(), password);
            log.info("Compte Keycloak créé : personne id={} login={}", id, login);
        } catch (IllegalStateException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "keycloakerror");
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/personnes")
    public ResponseEntity<PersonneDTO> updatePersonne(@Valid @RequestBody PersonneDTO personneDTO) throws URISyntaxException {
        log.debug("REST request to update Personne : {}", personneDTO);
        if (personneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonneDTO result = personneService.save(personneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personneDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/personnes")
    public ResponseEntity<List<PersonneDTO>> getAllPersonnes(
        Pageable pageable,
        @RequestParam(required = false) String matricule,
        @RequestParam(required = false) String nomPrenom,
        @RequestParam(required = false) String numTelephone,
        @RequestParam(required = false) String cin,
        @RequestParam(required = false) String typeContratId
    ) {
        log.debug("REST request to get a page of Personnes");
        Page<PersonneDTO> page = personneService.findAll(pageable, matricule, nomPrenom, numTelephone, cin, typeContratId);
        return ResponseEntity
            .ok()
            .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
            .body(page.getContent());
    }

    @GetMapping("/personnes/{id}")
    public ResponseEntity<PersonneDTO> getPersonne(@PathVariable Long id) {
        log.debug("REST request to get Personne : {}", id);
        return ResponseUtil.wrapOrNotFound(personneService.findOne(id));
    }

    @GetMapping("/personnes/search")
    public ResponseEntity<List<PersonneDTO>> searchPersonnes(
        Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to search Personnes with keyword: {}", keyword);
        Page<PersonneDTO> page = personneService.searchByKeyword(pageable, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @DeleteMapping("/personnes/{id}")
    public ResponseEntity<Void> deletePersonne(@PathVariable Long id) {
        log.debug("REST request to delete Personne : {}", id);
        personneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/personnes/generate-next-matricule")
    public ResponseEntity<String> generateNextMatricule(@RequestParam String prefix) {
        return ResponseEntity.ok(personneService.generateNextMatricule(prefix));
    }

    @PostMapping("/personnes/import-excel")
    public ResponseEntity<List<PersonneDTO>> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(personneService.importFromExcel(file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/personnesList")
    public List<PersonneDTO> getAllPersonnesList() {
        log.debug("REST request to get all Personnes (list)");
        return personneService.findAllListPersonne();
    }

    @GetMapping("/personnes/by-user/{userId}")
    public ResponseEntity<PersonneDTO> getPersonneByUserId(@PathVariable Long userId) {
        return personneService.findByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/personnes/names-by-matricules")
    public ResponseEntity<Map<String, String>> getPersonneNames(@RequestBody List<String> matricules) {
        return ResponseEntity.ok(personneService.findPersonneNamesByMatricules(matricules));
    }

    @PutMapping("/personnes/by-matricule/{matricule}")
    public ResponseEntity<PersonneDTO> updatePersonneByMatricule(@PathVariable String matricule, @RequestBody Map<String, String> updates) {
        log.debug("REST request to update Personne by matricule {} with {}", matricule, updates);
        try {
            return ResponseUtil.wrapOrNotFound(
                personneService.updateNomPrenomAndEmailByMatricule(matricule, updates.get("nomPrenom"), updates.get("email"))
            );
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "validationerror");
        }
    }

    @GetMapping("/personnes/by-matricule/{matricule}")
    public ResponseEntity<PersonneDTO> getPersonneByMatricule(@PathVariable String matricule) {
        log.debug("REST request to get Personne by matricule : {}", matricule);
        return ResponseUtil.wrapOrNotFound(personneService.findByMatricule(matricule));
    }

    private String sanitizeLogin(String raw) {
        if (raw == null) return "unknown.login";
        String cleaned = raw.trim().toLowerCase().replaceAll("[^a-z0-9._-]", ".");
        cleaned = cleaned.replaceAll("\\.{2,}", ".");
        return cleaned.isEmpty() ? "unknown.login" : cleaned;
    }

    // Ajouter après @GetMapping("/personnes/by-matricule/{matricule}")

    @PutMapping("/personnes/{id}/assign-user/{userId}")
    public ResponseEntity<PersonneDTO> assignUser(@PathVariable Long id, @PathVariable String userId) {
        log.debug("REST request to assign userId {} to Personne {}", userId, id);
        try {
            PersonneDTO result = personneService.assignUser(id, userId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "notfound");
        }
    }

    @PutMapping("/personnes/{id}/unassign-user")
    public ResponseEntity<PersonneDTO> unassignUser(@PathVariable Long id) {
        log.debug("REST request to unassign user from Personne {}", id);
        try {
            PersonneDTO result = personneService.unassignUser(id);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "notfound");
        }
    }

    // Retourne les userId déjà assignés (pour filtrage côté frontend)
    @GetMapping("/personnes/assigned-user-ids")
    public ResponseEntity<List<String>> getAssignedUserIds() {
        List<String> assignedIds = personneService
            .findAllListPersonne()
            .stream()
            .map(p -> p.getUserId())
            .filter(uid -> uid != null && !uid.isEmpty())
            .distinct()
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(assignedIds);
    }
}
