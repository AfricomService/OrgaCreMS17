package com.orgacare.app.web.rest;

import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.service.AffectationService;
import com.orgacare.app.service.dto.AffectationDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.Affectation}.
 */
@RestController
@RequestMapping("/api")
public class AffectationResource {

    private final Logger log = LoggerFactory.getLogger(AffectationResource.class);

    private static final String ENTITY_NAME = "orgaCareAffectation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffectationService affectationService;

    private final AffectationRepository affectationRepository;

    public AffectationResource(AffectationService affectationService, AffectationRepository affectationRepository) {
        this.affectationService = affectationService;
        this.affectationRepository = affectationRepository;
    }

    /**
     * {@code POST  /affectations} : Create a new affectation.
     *
     * @param affectationDTO the affectationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectationDTO, or with status {@code 400 (Bad Request)} if the affectation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affectations")
    public ResponseEntity<AffectationDTO> createAffectation(@Valid @RequestBody AffectationDTO affectationDTO) throws URISyntaxException {
        log.debug("REST request to save Affectation : {}", affectationDTO);
        if (affectationDTO.getId() != null) {
            throw new BadRequestAlertException("A new affectation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffectationDTO result = affectationService.save(affectationDTO);
        return ResponseEntity
            .created(new URI("/api/affectations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affectations/:id} : Updates an existing affectation.
     *
     * @param id the id of the affectationDTO to save.
     * @param affectationDTO the affectationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationDTO,
     * or with status {@code 400 (Bad Request)} if the affectationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affectations/{id}")
    public ResponseEntity<AffectationDTO> updateAffectation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AffectationDTO affectationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Affectation : {}, {}", id, affectationDTO);
        if (affectationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AffectationDTO result = affectationService.save(affectationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affectations/:id} : Partial updates given fields of an existing affectation, field will ignore if it is null
     *
     * @param id the id of the affectationDTO to save.
     * @param affectationDTO the affectationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationDTO,
     * or with status {@code 400 (Bad Request)} if the affectationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affectationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affectations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffectationDTO> partialUpdateAffectation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AffectationDTO affectationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Affectation partially : {}, {}", id, affectationDTO);
        if (affectationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffectationDTO> result = affectationService.partialUpdate(affectationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affectations} : get all the affectations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectations in body.
     */
    @GetMapping("/affectations")
    public ResponseEntity<List<AffectationDTO>> getAllAffectations(Pageable pageable) {
        log.debug("REST request to get a page of Affectations");
        Page<AffectationDTO> page = affectationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affectations/:id} : get the "id" affectation.
     *
     * @param id the id of the affectationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affectations/{id}")
    public ResponseEntity<AffectationDTO> getAffectation(@PathVariable Long id) {
        log.debug("REST request to get Affectation : {}", id);
        Optional<AffectationDTO> affectationDTO = affectationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectationDTO);
    }

    /**
     * {@code DELETE  /affectations/:id} : delete the "id" affectation.
     *
     * @param id the id of the affectationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affectations/{id}")
    public ResponseEntity<Void> deleteAffectation(@PathVariable Long id) {
        log.debug("REST request to delete Affectation : {}", id);
        affectationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
