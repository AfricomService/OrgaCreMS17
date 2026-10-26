package com.orgacare.app.web.rest;

import com.orgacare.app.repository.SessionFormationChefProjetRepository;
import com.orgacare.app.service.SessionFormationChefProjetService;
import com.orgacare.app.service.dto.SessionFormationChefProjetDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.SessionFormationChefProjet}.
 */
@RestController
@RequestMapping("/api")
public class SessionFormationChefProjetResource {

    private final Logger log = LoggerFactory.getLogger(SessionFormationChefProjetResource.class);

    private static final String ENTITY_NAME = "orgaCareSessionFormationChefProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionFormationChefProjetService sessionFormationChefProjetService;

    private final SessionFormationChefProjetRepository sessionFormationChefProjetRepository;

    public SessionFormationChefProjetResource(
        SessionFormationChefProjetService sessionFormationChefProjetService,
        SessionFormationChefProjetRepository sessionFormationChefProjetRepository
    ) {
        this.sessionFormationChefProjetService = sessionFormationChefProjetService;
        this.sessionFormationChefProjetRepository = sessionFormationChefProjetRepository;
    }

    /**
     * {@code POST  /session-formation-chef-projets} : Create a new sessionFormationChefProjet.
     *
     * @param sessionFormationChefProjetDTO the sessionFormationChefProjetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionFormationChefProjetDTO, or with status {@code 400 (Bad Request)} if the sessionFormationChefProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-formation-chef-projets")
    public ResponseEntity<SessionFormationChefProjetDTO> createSessionFormationChefProjet(
        @RequestBody SessionFormationChefProjetDTO sessionFormationChefProjetDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SessionFormationChefProjet : {}", sessionFormationChefProjetDTO);
        if (sessionFormationChefProjetDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionFormationChefProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionFormationChefProjetDTO result = sessionFormationChefProjetService.save(sessionFormationChefProjetDTO);
        return ResponseEntity
            .created(new URI("/api/session-formation-chef-projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-formation-chef-projets/:id} : Updates an existing sessionFormationChefProjet.
     *
     * @param id the id of the sessionFormationChefProjetDTO to save.
     * @param sessionFormationChefProjetDTO the sessionFormationChefProjetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationChefProjetDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationChefProjetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationChefProjetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-formation-chef-projets/{id}")
    public ResponseEntity<SessionFormationChefProjetDTO> updateSessionFormationChefProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationChefProjetDTO sessionFormationChefProjetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SessionFormationChefProjet : {}, {}", id, sessionFormationChefProjetDTO);
        if (sessionFormationChefProjetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationChefProjetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationChefProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionFormationChefProjetDTO result = sessionFormationChefProjetService.save(sessionFormationChefProjetDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationChefProjetDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /session-formation-chef-projets/:id} : Partial updates given fields of an existing sessionFormationChefProjet, field will ignore if it is null
     *
     * @param id the id of the sessionFormationChefProjetDTO to save.
     * @param sessionFormationChefProjetDTO the sessionFormationChefProjetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationChefProjetDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationChefProjetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionFormationChefProjetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationChefProjetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-formation-chef-projets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionFormationChefProjetDTO> partialUpdateSessionFormationChefProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationChefProjetDTO sessionFormationChefProjetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionFormationChefProjet partially : {}, {}", id, sessionFormationChefProjetDTO);
        if (sessionFormationChefProjetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationChefProjetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationChefProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionFormationChefProjetDTO> result = sessionFormationChefProjetService.partialUpdate(sessionFormationChefProjetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationChefProjetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-formation-chef-projets} : get all the sessionFormationChefProjets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionFormationChefProjets in body.
     */
    @GetMapping("/session-formation-chef-projets")
    public List<SessionFormationChefProjetDTO> getAllSessionFormationChefProjets() {
        log.debug("REST request to get all SessionFormationChefProjets");
        return sessionFormationChefProjetService.findAll();
    }

    /**
     * {@code GET  /session-formation-chef-projets/:id} : get the "id" sessionFormationChefProjet.
     *
     * @param id the id of the sessionFormationChefProjetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionFormationChefProjetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-formation-chef-projets/by-session/{sessionId}")
    public List<Long> getChefIdsBySession(@PathVariable Long sessionId) {
        return sessionFormationChefProjetService.findChefIdsBySession(sessionId);
    }

    @PutMapping("/session-formation-chef-projets/by-session/{sessionId}")
    public List<Long> replaceChefsOfSession(@PathVariable Long sessionId, @RequestBody List<Long> chefIds) {
        return sessionFormationChefProjetService.replaceForSession(sessionId, chefIds);
    }

    @GetMapping("/session-formation-chef-projets/{id}")
    public ResponseEntity<SessionFormationChefProjetDTO> getSessionFormationChefProjet(@PathVariable Long id) {
        log.debug("REST request to get SessionFormationChefProjet : {}", id);
        Optional<SessionFormationChefProjetDTO> sessionFormationChefProjetDTO = sessionFormationChefProjetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionFormationChefProjetDTO);
    }

    /**
     * {@code DELETE  /session-formation-chef-projets/:id} : delete the "id" sessionFormationChefProjet.
     *
     * @param id the id of the sessionFormationChefProjetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-formation-chef-projets/{id}")
    public ResponseEntity<Void> deleteSessionFormationChefProjet(@PathVariable Long id) {
        log.debug("REST request to delete SessionFormationChefProjet : {}", id);
        sessionFormationChefProjetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
