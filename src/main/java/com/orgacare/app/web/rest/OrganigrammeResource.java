package com.orgacare.app.web.rest;

import com.orgacare.app.repository.OrganigrammeRepository;
import com.orgacare.app.service.OrganigrammeService;
import com.orgacare.app.service.dto.OrganigrammeDTO;
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
 * REST controller for managing {@link com.orgacare.app.domain.Organigramme}.
 */
@RestController
@RequestMapping("/api")
public class OrganigrammeResource {

    private final Logger log = LoggerFactory.getLogger(OrganigrammeResource.class);

    private static final String ENTITY_NAME = "orgaCareOrganigramme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganigrammeService organigrammeService;

    private final OrganigrammeRepository organigrammeRepository;

    public OrganigrammeResource(OrganigrammeService organigrammeService, OrganigrammeRepository organigrammeRepository) {
        this.organigrammeService = organigrammeService;
        this.organigrammeRepository = organigrammeRepository;
    }

    /**
     * {@code POST  /organigrammes} : Create a new organigramme.
     *
     * @param organigrammeDTO the organigrammeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organigrammeDTO, or with status {@code 400 (Bad Request)} if the organigramme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organigrammes")
    public ResponseEntity<OrganigrammeDTO> createOrganigramme(@Valid @RequestBody OrganigrammeDTO organigrammeDTO)
        throws URISyntaxException {
        log.debug("REST request to save Organigramme : {}", organigrammeDTO);
        if (organigrammeDTO.getId() != null) {
            throw new BadRequestAlertException("A new organigramme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganigrammeDTO result = organigrammeService.save(organigrammeDTO);
        return ResponseEntity
            .created(new URI("/api/organigrammes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organigrammes/:id} : Updates an existing organigramme.
     *
     * @param id the id of the organigrammeDTO to save.
     * @param organigrammeDTO the organigrammeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organigrammeDTO,
     * or with status {@code 400 (Bad Request)} if the organigrammeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organigrammeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organigrammes/{id}")
    public ResponseEntity<OrganigrammeDTO> updateOrganigramme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganigrammeDTO organigrammeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Organigramme : {}, {}", id, organigrammeDTO);
        if (organigrammeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organigrammeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organigrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganigrammeDTO result = organigrammeService.save(organigrammeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organigrammeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organigrammes/:id} : Partial updates given fields of an existing organigramme, field will ignore if it is null
     *
     * @param id the id of the organigrammeDTO to save.
     * @param organigrammeDTO the organigrammeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organigrammeDTO,
     * or with status {@code 400 (Bad Request)} if the organigrammeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organigrammeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organigrammeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organigrammes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganigrammeDTO> partialUpdateOrganigramme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganigrammeDTO organigrammeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Organigramme partially : {}, {}", id, organigrammeDTO);
        if (organigrammeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organigrammeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organigrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganigrammeDTO> result = organigrammeService.partialUpdate(organigrammeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organigrammeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /organigrammes} : get all the organigrammes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organigrammes in body.
     */
    @GetMapping("/organigrammes")
    public ResponseEntity<List<OrganigrammeDTO>> getAllOrganigrammes(Pageable pageable) {
        log.debug("REST request to get a page of Organigrammes");
        Page<OrganigrammeDTO> page = organigrammeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organigrammes/:id} : get the "id" organigramme.
     *
     * @param id the id of the organigrammeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organigrammeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organigrammes/{id}")
    public ResponseEntity<OrganigrammeDTO> getOrganigramme(@PathVariable Long id) {
        log.debug("REST request to get Organigramme : {}", id);
        Optional<OrganigrammeDTO> organigrammeDTO = organigrammeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organigrammeDTO);
    }

    /**
     * {@code DELETE  /organigrammes/:id} : delete the "id" organigramme.
     *
     * @param id the id of the organigrammeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organigrammes/{id}")
    public ResponseEntity<Void> deleteOrganigramme(@PathVariable Long id) {
        log.debug("REST request to delete Organigramme : {}", id);
        organigrammeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
