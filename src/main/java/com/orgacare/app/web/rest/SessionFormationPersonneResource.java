package com.orgacare.app.web.rest;

import com.orgacare.app.repository.SessionFormationPersonneRepository;
import com.orgacare.app.service.SessionFormationPersonneService;
import com.orgacare.app.service.dto.SessionFormationPersonneDTO;
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
 * REST controller for managing {@link com.orgacare.app.domain.SessionFormationPersonne}.
 */
@RestController
@RequestMapping("/api")
public class SessionFormationPersonneResource {

    private final Logger log = LoggerFactory.getLogger(SessionFormationPersonneResource.class);

    private static final String ENTITY_NAME = "orgaCareSessionFormationPersonne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionFormationPersonneService sessionFormationPersonneService;

    private final SessionFormationPersonneRepository sessionFormationPersonneRepository;

    public SessionFormationPersonneResource(
        SessionFormationPersonneService sessionFormationPersonneService,
        SessionFormationPersonneRepository sessionFormationPersonneRepository
    ) {
        this.sessionFormationPersonneService = sessionFormationPersonneService;
        this.sessionFormationPersonneRepository = sessionFormationPersonneRepository;
    }

    /**
     * {@code POST  /session-formation-personnes} : Create a new sessionFormationPersonne.
     *
     * @param sessionFormationPersonneDTO the sessionFormationPersonneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionFormationPersonneDTO, or with status {@code 400 (Bad Request)} if the sessionFormationPersonne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-formation-personnes")
    public ResponseEntity<SessionFormationPersonneDTO> createSessionFormationPersonne(
        @RequestBody SessionFormationPersonneDTO sessionFormationPersonneDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SessionFormationPersonne : {}", sessionFormationPersonneDTO);
        if (sessionFormationPersonneDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionFormationPersonne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionFormationPersonneDTO result = sessionFormationPersonneService.save(sessionFormationPersonneDTO);
        return ResponseEntity
            .created(new URI("/api/session-formation-personnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-formation-personnes/:id} : Updates an existing sessionFormationPersonne.
     *
     * @param id the id of the sessionFormationPersonneDTO to save.
     * @param sessionFormationPersonneDTO the sessionFormationPersonneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationPersonneDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationPersonneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationPersonneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-formation-personnes/{id}")
    public ResponseEntity<SessionFormationPersonneDTO> updateSessionFormationPersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationPersonneDTO sessionFormationPersonneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SessionFormationPersonne : {}, {}", id, sessionFormationPersonneDTO);
        if (sessionFormationPersonneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationPersonneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationPersonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionFormationPersonneDTO result = sessionFormationPersonneService.save(sessionFormationPersonneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationPersonneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-formation-personnes/:id} : Partial updates given fields of an existing sessionFormationPersonne, field will ignore if it is null
     *
     * @param id the id of the sessionFormationPersonneDTO to save.
     * @param sessionFormationPersonneDTO the sessionFormationPersonneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationPersonneDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationPersonneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionFormationPersonneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationPersonneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-formation-personnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionFormationPersonneDTO> partialUpdateSessionFormationPersonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationPersonneDTO sessionFormationPersonneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionFormationPersonne partially : {}, {}", id, sessionFormationPersonneDTO);
        if (sessionFormationPersonneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationPersonneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationPersonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionFormationPersonneDTO> result = sessionFormationPersonneService.partialUpdate(sessionFormationPersonneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationPersonneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-formation-personnes} : get all the sessionFormationPersonnes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionFormationPersonnes in body.
     */
    @GetMapping("/session-formation-personnes")
    public List<SessionFormationPersonneDTO> getAllSessionFormationPersonnes() {
        log.debug("REST request to get all SessionFormationPersonnes");
        return sessionFormationPersonneService.findAll();
    }

    /**
     * {@code GET  /session-formation-personnes/:id} : get the "id" sessionFormationPersonne.
     *
     * @param id the id of the sessionFormationPersonneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionFormationPersonneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-formation-personnes/{id}")
    public ResponseEntity<SessionFormationPersonneDTO> getSessionFormationPersonne(@PathVariable Long id) {
        log.debug("REST request to get SessionFormationPersonne : {}", id);
        Optional<SessionFormationPersonneDTO> sessionFormationPersonneDTO = sessionFormationPersonneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionFormationPersonneDTO);
    }

    /**
     * {@code DELETE  /session-formation-personnes/:id} : delete the "id" sessionFormationPersonne.
     *
     * @param id the id of the sessionFormationPersonneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-formation-personnes/{id}")
    public ResponseEntity<Void> deleteSessionFormationPersonne(@PathVariable Long id) {
        log.debug("REST request to delete SessionFormationPersonne : {}", id);
        sessionFormationPersonneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
