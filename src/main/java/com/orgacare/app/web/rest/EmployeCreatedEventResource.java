package com.orgacare.app.web.rest;

import com.orgacare.app.domain.EmployeCreatedEvent;
import com.orgacare.app.repository.EmployeCreatedEventRepository;
import com.orgacare.app.service.EmployeCreatedEventService;
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
 * REST controller for managing {@link com.orgacare.app.domain.EmployeCreatedEvent}.
 */
@RestController
@RequestMapping("/api")
public class EmployeCreatedEventResource {

    private final Logger log = LoggerFactory.getLogger(EmployeCreatedEventResource.class);

    private static final String ENTITY_NAME = "orgaCareEmployeCreatedEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeCreatedEventService employeCreatedEventService;

    private final EmployeCreatedEventRepository employeCreatedEventRepository;

    public EmployeCreatedEventResource(
        EmployeCreatedEventService employeCreatedEventService,
        EmployeCreatedEventRepository employeCreatedEventRepository
    ) {
        this.employeCreatedEventService = employeCreatedEventService;
        this.employeCreatedEventRepository = employeCreatedEventRepository;
    }

    /**
     * {@code POST  /employe-created-events} : Create a new employeCreatedEvent.
     *
     * @param employeCreatedEvent the employeCreatedEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeCreatedEvent, or with status {@code 400 (Bad Request)} if the employeCreatedEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employe-created-events")
    public ResponseEntity<EmployeCreatedEvent> createEmployeCreatedEvent(@RequestBody EmployeCreatedEvent employeCreatedEvent)
        throws URISyntaxException {
        log.debug("REST request to save EmployeCreatedEvent : {}", employeCreatedEvent);
        if (employeCreatedEvent.getId() != null) {
            throw new BadRequestAlertException("A new employeCreatedEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeCreatedEvent result = employeCreatedEventService.save(employeCreatedEvent);
        return ResponseEntity
            .created(new URI("/api/employe-created-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employe-created-events/:id} : Updates an existing employeCreatedEvent.
     *
     * @param id the id of the employeCreatedEvent to save.
     * @param employeCreatedEvent the employeCreatedEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeCreatedEvent,
     * or with status {@code 400 (Bad Request)} if the employeCreatedEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeCreatedEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employe-created-events/{id}")
    public ResponseEntity<EmployeCreatedEvent> updateEmployeCreatedEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeCreatedEvent employeCreatedEvent
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeCreatedEvent : {}, {}", id, employeCreatedEvent);
        if (employeCreatedEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeCreatedEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeCreatedEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeCreatedEvent result = employeCreatedEventService.save(employeCreatedEvent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeCreatedEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employe-created-events/:id} : Partial updates given fields of an existing employeCreatedEvent, field will ignore if it is null
     *
     * @param id the id of the employeCreatedEvent to save.
     * @param employeCreatedEvent the employeCreatedEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeCreatedEvent,
     * or with status {@code 400 (Bad Request)} if the employeCreatedEvent is not valid,
     * or with status {@code 404 (Not Found)} if the employeCreatedEvent is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeCreatedEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employe-created-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeCreatedEvent> partialUpdateEmployeCreatedEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeCreatedEvent employeCreatedEvent
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeCreatedEvent partially : {}, {}", id, employeCreatedEvent);
        if (employeCreatedEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeCreatedEvent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeCreatedEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeCreatedEvent> result = employeCreatedEventService.partialUpdate(employeCreatedEvent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeCreatedEvent.getId().toString())
        );
    }

    /**
     * {@code GET  /employe-created-events} : get all the employeCreatedEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeCreatedEvents in body.
     */
    @GetMapping("/employe-created-events")
    public List<EmployeCreatedEvent> getAllEmployeCreatedEvents() {
        log.debug("REST request to get all EmployeCreatedEvents");
        return employeCreatedEventService.findAll();
    }

    /**
     * {@code GET  /employe-created-events/:id} : get the "id" employeCreatedEvent.
     *
     * @param id the id of the employeCreatedEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeCreatedEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employe-created-events/{id}")
    public ResponseEntity<EmployeCreatedEvent> getEmployeCreatedEvent(@PathVariable Long id) {
        log.debug("REST request to get EmployeCreatedEvent : {}", id);
        Optional<EmployeCreatedEvent> employeCreatedEvent = employeCreatedEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeCreatedEvent);
    }

    /**
     * {@code DELETE  /employe-created-events/:id} : delete the "id" employeCreatedEvent.
     *
     * @param id the id of the employeCreatedEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employe-created-events/{id}")
    public ResponseEntity<Void> deleteEmployeCreatedEvent(@PathVariable Long id) {
        log.debug("REST request to delete EmployeCreatedEvent : {}", id);
        employeCreatedEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
