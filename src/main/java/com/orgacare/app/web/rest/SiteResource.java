package com.orgacare.app.web.rest;

import com.orgacare.app.domain.Site;
import com.orgacare.app.repository.SiteRepository;
import com.orgacare.app.service.SiteService;
import com.orgacare.app.service.dto.SiteDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);

    private static final String ENTITY_NAME = "orgaCareSite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteService siteService;

    public SiteResource(SiteService siteService) {
        this.siteService = siteService;
    }

    /**
     * POST /sites : Create a new site.
     */
    @PostMapping("/sites")
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
        log.debug("REST request to save Site : {}", siteDTO);
        if (siteDTO.getId() != null) {
            throw new BadRequestAlertException("A new site cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteDTO result = siteService.save(siteDTO);
        return ResponseEntity
            .created(new URI("/api/sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT /sites/{id} : Update an existing site.
     * CORRECTION: l'URL doit inclure {id} pour matcher PUT /api/sites/1
     */
    @PutMapping("/sites/{id}")
    public ResponseEntity<SiteDTO> updateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Site : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        SiteDTO result = siteService.save(siteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(result);
    }

    /**
     * PATCH /sites/{id} : Partial update of an existing site.
     */
    @PatchMapping(value = "/sites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SiteDTO> partialUpdateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Site partially : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        SiteDTO result = siteService.save(siteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET /sites : Get all sites (paginated).
     */
    @GetMapping("/sites")
    public ResponseEntity<List<SiteDTO>> getAllSites(Pageable pageable) {
        log.debug("REST request to get a page of Sites");
        Page<SiteDTO> page = siteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET /sites/{id} : Get one site by id.
     */
    @GetMapping("/sites/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Optional<SiteDTO> siteDTO = siteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteDTO);
    }

    /**
     * DELETE /sites/{id} : Delete a site by id.
     */
    @DeleteMapping("/sites/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        log.debug("REST request to delete Site : {}", id);
        siteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET /sites/by-societe/{societeId} : Get sites by societe id.
     */
    @GetMapping("/sites/by-societe/{societeId}")
    public ResponseEntity<List<Site>> getSitesBySocieteId(@PathVariable Long societeId) {
        List<Site> sites = siteService.findBySocieteId(societeId);
        return ResponseEntity.ok().body(sites);
    }
}
