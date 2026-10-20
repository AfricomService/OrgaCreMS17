package com.orgacare.app.web.rest;

import com.orgacare.app.domain.AclSid;
import com.orgacare.app.repository.AclSidRepository;
import com.orgacare.app.service.AclSidService;
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
 * REST controller for managing {@link com.orgacare.app.domain.AclSid}.
 */
@RestController
@RequestMapping("/api")
public class AclSidResource {

    private final Logger log = LoggerFactory.getLogger(AclSidResource.class);

    private static final String ENTITY_NAME = "orgaCareAclSid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclSidService aclSidService;

    private final AclSidRepository aclSidRepository;

    public AclSidResource(AclSidService aclSidService, AclSidRepository aclSidRepository) {
        this.aclSidService = aclSidService;
        this.aclSidRepository = aclSidRepository;
    }

    /**
     * {@code POST  /acl-sids} : Create a new aclSid.
     *
     * @param aclSid the aclSid to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclSid, or with status {@code 400 (Bad Request)} if the aclSid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-sids")
    public ResponseEntity<AclSid> createAclSid(@RequestBody AclSid aclSid) throws URISyntaxException {
        log.debug("REST request to save AclSid : {}", aclSid);
        if (aclSid.getId() != null) {
            throw new BadRequestAlertException("A new aclSid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclSid result = aclSidService.save(aclSid);
        return ResponseEntity
            .created(new URI("/api/acl-sids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-sids/:id} : Updates an existing aclSid.
     *
     * @param id the id of the aclSid to save.
     * @param aclSid the aclSid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclSid,
     * or with status {@code 400 (Bad Request)} if the aclSid is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclSid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-sids/{id}")
    public ResponseEntity<AclSid> updateAclSid(@PathVariable(value = "id", required = false) final Long id, @RequestBody AclSid aclSid)
        throws URISyntaxException {
        log.debug("REST request to update AclSid : {}, {}", id, aclSid);
        if (aclSid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclSid.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclSidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclSid result = aclSidService.save(aclSid);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclSid.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-sids/:id} : Partial updates given fields of an existing aclSid, field will ignore if it is null
     *
     * @param id the id of the aclSid to save.
     * @param aclSid the aclSid to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclSid,
     * or with status {@code 400 (Bad Request)} if the aclSid is not valid,
     * or with status {@code 404 (Not Found)} if the aclSid is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclSid couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-sids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AclSid> partialUpdateAclSid(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclSid aclSid
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclSid partially : {}, {}", id, aclSid);
        if (aclSid.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclSid.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclSidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclSid> result = aclSidService.partialUpdate(aclSid);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclSid.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-sids} : get all the aclSids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclSids in body.
     */
    @GetMapping("/acl-sids")
    public List<AclSid> getAllAclSids() {
        log.debug("REST request to get all AclSids");
        return aclSidService.findAll();
    }

    /**
     * {@code GET  /acl-sids/:id} : get the "id" aclSid.
     *
     * @param id the id of the aclSid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclSid, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-sids/{id}")
    public ResponseEntity<AclSid> getAclSid(@PathVariable Long id) {
        log.debug("REST request to get AclSid : {}", id);
        Optional<AclSid> aclSid = aclSidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclSid);
    }

    /**
     * {@code DELETE  /acl-sids/:id} : delete the "id" aclSid.
     *
     * @param id the id of the aclSid to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-sids/{id}")
    public ResponseEntity<Void> deleteAclSid(@PathVariable Long id) {
        log.debug("REST request to delete AclSid : {}", id);
        aclSidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
