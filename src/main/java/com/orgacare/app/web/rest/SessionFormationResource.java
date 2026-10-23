package com.orgacare.app.web.rest;

import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.service.SessionFormationService;
import com.orgacare.app.service.dto.SessionFormationDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.SessionFormation}.
 */
@RestController
@RequestMapping("/api")
public class SessionFormationResource {

    private final Logger log = LoggerFactory.getLogger(SessionFormationResource.class);

    private static final String ENTITY_NAME = "orgaCareSessionFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionFormationService sessionFormationService;

    private final SessionFormationRepository sessionFormationRepository;

    public SessionFormationResource(
        SessionFormationService sessionFormationService,
        SessionFormationRepository sessionFormationRepository
    ) {
        this.sessionFormationService = sessionFormationService;
        this.sessionFormationRepository = sessionFormationRepository;
    }

    /**
     * {@code POST  /session-formations} : Create a new sessionFormation.
     *
     * @param sessionFormationDTO the sessionFormationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionFormationDTO, or with status {@code 400 (Bad Request)} if the sessionFormation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-formations")
    public ResponseEntity<SessionFormationDTO> createSessionFormation(@RequestBody SessionFormationDTO sessionFormationDTO)
        throws URISyntaxException {
        log.debug("REST request to save SessionFormation : {}", sessionFormationDTO);
        if (sessionFormationDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionFormation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionFormationDTO result = sessionFormationService.create(sessionFormationDTO);
        return ResponseEntity
            .created(new URI("/api/session-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-formations/:id} : Updates an existing sessionFormation.
     *
     * @param id the id of the sessionFormationDTO to save.
     * @param sessionFormationDTO the sessionFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-formations/{id}")
    public ResponseEntity<SessionFormationDTO> updateSessionFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationDTO sessionFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SessionFormation : {}, {}", id, sessionFormationDTO);
        if (sessionFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionFormationDTO result = sessionFormationService.update(sessionFormationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-formations/:id/executer} : passe la session au statut "Exécuté".
     *
     * @param id the id of the sessionFormation to execute.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the updated sessionFormationDTO, or {@code 404 (Not Found)}.
     */
    @PutMapping("/session-formations/{id}/executer")
    public ResponseEntity<SessionFormationDTO> executerSessionFormation(@PathVariable Long id) {
        log.debug("REST request to execute SessionFormation : {}", id);
        Optional<SessionFormationDTO> result = sessionFormationService.executer(id);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code PATCH  /session-formations/:id} : Partial updates given fields of an existing sessionFormation, field will ignore if it is null
     *
     * @param id the id of the sessionFormationDTO to save.
     * @param sessionFormationDTO the sessionFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionFormationDTO,
     * or with status {@code 400 (Bad Request)} if the sessionFormationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionFormationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-formations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionFormationDTO> partialUpdateSessionFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionFormationDTO sessionFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionFormation partially : {}, {}", id, sessionFormationDTO);
        if (sessionFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionFormationDTO> result = sessionFormationService.partialUpdate(sessionFormationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionFormationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-formations} : get all the sessionFormations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionFormations in body.
     */
    @GetMapping("/session-formations")
    public ResponseEntity<List<SessionFormationDTO>> getAllSessionFormations(
        Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "formateurId", required = false) Long formateurId,
        @RequestParam(value = "statut", required = false) String statut,
        @RequestParam(value = "datePlanificationDebut", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
        ) LocalDate datePlanificationDebut,
        @RequestParam(value = "datePlanificationFin", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
        ) LocalDate datePlanificationFin,
        @RequestParam(value = "dateExecutionDebut", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
        ) LocalDate dateExecutionDebut,
        @RequestParam(value = "dateExecutionFin", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
        ) LocalDate dateExecutionFin
    ) {
        log.debug("REST request to get a page of SessionFormations");
        Page<SessionFormationDTO> page = sessionFormationService.search(
            pageable,
            keyword,
            formateurId,
            statut,
            datePlanificationDebut,
            datePlanificationFin,
            dateExecutionDebut,
            dateExecutionFin
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-formations/:id} : get the "id" sessionFormation.
     *
     * @param id the id of the sessionFormationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionFormationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-formations/{id}")
    public ResponseEntity<SessionFormationDTO> getSessionFormation(@PathVariable Long id) {
        log.debug("REST request to get SessionFormation : {}", id);
        Optional<SessionFormationDTO> sessionFormationDTO = sessionFormationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionFormationDTO);
    }

    /**
     * {@code DELETE  /session-formations/:id} : delete the "id" sessionFormation.
     *
     * @param id the id of the sessionFormationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-formations/{id}")
    public ResponseEntity<Void> deleteSessionFormation(@PathVariable Long id) {
        log.debug("REST request to delete SessionFormation : {}", id);
        sessionFormationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
