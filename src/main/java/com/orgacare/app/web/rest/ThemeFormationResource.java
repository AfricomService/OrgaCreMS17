package com.orgacare.app.web.rest;

import com.orgacare.app.repository.ThemeFormationRepository;
import com.orgacare.app.service.ThemeFormationService;
import com.orgacare.app.service.dto.ThemeFormationDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.ThemeFormation}.
 */
@RestController
@RequestMapping("/api")
public class ThemeFormationResource {

    private final Logger log = LoggerFactory.getLogger(ThemeFormationResource.class);

    private static final String ENTITY_NAME = "orgaCareThemeFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeFormationService themeFormationService;

    private final ThemeFormationRepository themeFormationRepository;

    public ThemeFormationResource(ThemeFormationService themeFormationService, ThemeFormationRepository themeFormationRepository) {
        this.themeFormationService = themeFormationService;
        this.themeFormationRepository = themeFormationRepository;
    }

    /**
     * {@code POST  /theme-formations} : Create a new themeFormation.
     *
     * @param themeFormationDTO the themeFormationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeFormationDTO, or with status {@code 400 (Bad Request)} if the themeFormation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theme-formations")
    public ResponseEntity<ThemeFormationDTO> createThemeFormation(@Valid @RequestBody ThemeFormationDTO themeFormationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ThemeFormation : {}", themeFormationDTO);
        if (themeFormationDTO.getId() != null) {
            throw new BadRequestAlertException("A new themeFormation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeFormationDTO result = themeFormationService.save(themeFormationDTO);
        return ResponseEntity
            .created(new URI("/api/theme-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theme-formations/:id} : Updates an existing themeFormation.
     *
     * @param id the id of the themeFormationDTO to save.
     * @param themeFormationDTO the themeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the themeFormationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theme-formations/{id}")
    public ResponseEntity<ThemeFormationDTO> updateThemeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThemeFormationDTO themeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ThemeFormation : {}, {}", id, themeFormationDTO);
        if (themeFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeFormationDTO result = themeFormationService.save(themeFormationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFormationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theme-formations/:id} : Partial updates given fields of an existing themeFormation, field will ignore if it is null
     *
     * @param id the id of the themeFormationDTO to save.
     * @param themeFormationDTO the themeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the themeFormationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the themeFormationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/theme-formations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeFormationDTO> partialUpdateThemeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThemeFormationDTO themeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ThemeFormation partially : {}, {}", id, themeFormationDTO);
        if (themeFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeFormationDTO> result = themeFormationService.partialUpdate(themeFormationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeFormationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /theme-formations} : get all the themeFormations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeFormations in body.
     */
    @GetMapping("/theme-formations")
    public List<ThemeFormationDTO> getAllThemeFormations(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "status", required = false) String status
    ) {
        log.debug("REST request to get all ThemeFormations, search : {}, status : {}", search, status);
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty();
        if (hasSearch || hasStatus) {
            return themeFormationService.search(search, status);
        }
        return themeFormationService.findAll();
    }

    /**
     * {@code GET  /theme-formations/:id} : get the "id" themeFormation.
     *
     * @param id the id of the themeFormationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeFormationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theme-formations/{id}")
    public ResponseEntity<ThemeFormationDTO> getThemeFormation(@PathVariable Long id) {
        log.debug("REST request to get ThemeFormation : {}", id);
        Optional<ThemeFormationDTO> themeFormationDTO = themeFormationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeFormationDTO);
    }

    /**
     * {@code DELETE  /theme-formations/:id} : delete the "id" themeFormation.
     *
     * @param id the id of the themeFormationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theme-formations/{id}")
    public ResponseEntity<Void> deleteThemeFormation(@PathVariable Long id) {
        log.debug("REST request to delete ThemeFormation : {}", id);
        themeFormationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
