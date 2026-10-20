package com.orgacare.app.web.rest;

import com.orgacare.app.domain.AclEntry;
import com.orgacare.app.repository.AclEntryRepository;
import com.orgacare.app.service.AclEntryService;
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
 * REST controller for managing {@link com.orgacare.app.domain.AclEntry}.
 */
@RestController
@RequestMapping("/api")
public class AclEntryResource {

    private final Logger log = LoggerFactory.getLogger(AclEntryResource.class);

    private static final String ENTITY_NAME = "orgaCareAclEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclEntryService aclEntryService;

    private final AclEntryRepository aclEntryRepository;

    public AclEntryResource(AclEntryService aclEntryService, AclEntryRepository aclEntryRepository) {
        this.aclEntryService = aclEntryService;
        this.aclEntryRepository = aclEntryRepository;
    }

    /**
     * {@code POST  /acl-entries} : Create a new aclEntry.
     *
     * @param aclEntry the aclEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclEntry, or with status {@code 400 (Bad Request)} if the aclEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-entries")
    public ResponseEntity<AclEntry> createAclEntry(@RequestBody AclEntry aclEntry) throws URISyntaxException {
        log.debug("REST request to save AclEntry : {}", aclEntry);
        if (aclEntry.getId() != null) {
            throw new BadRequestAlertException("A new aclEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclEntry result = aclEntryService.save(aclEntry);
        return ResponseEntity
            .created(new URI("/api/acl-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-entries/:id} : Updates an existing aclEntry.
     *
     * @param id the id of the aclEntry to save.
     * @param aclEntry the aclEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntry,
     * or with status {@code 400 (Bad Request)} if the aclEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-entries/{id}")
    public ResponseEntity<AclEntry> updateAclEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclEntry aclEntry
    ) throws URISyntaxException {
        log.debug("REST request to update AclEntry : {}, {}", id, aclEntry);
        if (aclEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclEntry result = aclEntryService.save(aclEntry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntry.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-entries/:id} : Partial updates given fields of an existing aclEntry, field will ignore if it is null
     *
     * @param id the id of the aclEntry to save.
     * @param aclEntry the aclEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntry,
     * or with status {@code 400 (Bad Request)} if the aclEntry is not valid,
     * or with status {@code 404 (Not Found)} if the aclEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AclEntry> partialUpdateAclEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclEntry aclEntry
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclEntry partially : {}, {}", id, aclEntry);
        if (aclEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclEntry> result = aclEntryService.partialUpdate(aclEntry);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntry.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-entries} : get all the aclEntries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclEntries in body.
     */
    @GetMapping("/acl-entries")
    public List<AclEntry> getAllAclEntries() {
        log.debug("REST request to get all AclEntries");
        return aclEntryService.findAll();
    }

    /**
     * {@code GET  /acl-entries/:id} : get the "id" aclEntry.
     *
     * @param id the id of the aclEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-entries/{id}")
    public ResponseEntity<AclEntry> getAclEntry(@PathVariable Long id) {
        log.debug("REST request to get AclEntry : {}", id);
        Optional<AclEntry> aclEntry = aclEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclEntry);
    }

    /**
     * {@code DELETE  /acl-entries/:id} : delete the "id" aclEntry.
     *
     * @param id the id of the aclEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-entries/{id}")
    public ResponseEntity<Void> deleteAclEntry(@PathVariable Long id) {
        log.debug("REST request to delete AclEntry : {}", id);
        aclEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
