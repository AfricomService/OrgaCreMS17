package com.orgacare.app.web.rest;

import com.orgacare.app.repository.StatusHistoryRepository;
import com.orgacare.app.service.StatusHistoryService;
import com.orgacare.app.service.dto.StatusHistoryDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.orgacare.app.domain.StatusHistory}.
 */
@RestController
@RequestMapping("/api")
public class StatusHistoryResource {

    private final Logger log = LoggerFactory.getLogger(StatusHistoryResource.class);

    private static final String ENTITY_NAME = "orgaCareStatusHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusHistoryService statusHistoryService;

    public StatusHistoryResource(StatusHistoryService statusHistoryService) {
        this.statusHistoryService = statusHistoryService;
    }

    /**
     * {@code POST  /status-histories} : Create a new statusHistory.
     *
     * @param statusHistoryDTO the statusHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusHistoryDTO, or with status {@code 400 (Bad Request)} if the statusHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/status-histories")
    public ResponseEntity<StatusHistoryDTO> createStatusHistory(@RequestBody StatusHistoryDTO statusHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save StatusHistory : {}", statusHistoryDTO);
        if (statusHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new statusHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatusHistoryDTO result = statusHistoryService.save(statusHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/status-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /status-histories} : Updates an existing statusHistory.
     *
     * @param statusHistoryDTO the statusHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the statusHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/status-histories")
    public ResponseEntity<StatusHistoryDTO> updateStatusHistory(@RequestBody StatusHistoryDTO statusHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update StatusHistory : {}", statusHistoryDTO);
        if (statusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatusHistoryDTO result = statusHistoryService.save(statusHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /status-histories} : get all the statusHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusHistories in body.
     */
    @GetMapping("/status-histories")
    public ResponseEntity<List<StatusHistoryDTO>> getAllStatusHistories(Pageable pageable) {
        log.debug("REST request to get a page of StatusHistories");
        Page<StatusHistoryDTO> page = statusHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /status-histories/:id} : get the "id" statusHistory.
     *
     * @param id the id of the statusHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/status-histories/{id}")
    public ResponseEntity<StatusHistoryDTO> getStatusHistory(@PathVariable Long id) {
        log.debug("REST request to get StatusHistory : {}", id);
        Optional<StatusHistoryDTO> statusHistoryDTO = statusHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusHistoryDTO);
    }

    /**
     * {@code DELETE  /status-histories/:id} : delete the "id" statusHistory.
     *
     * @param id the id of the statusHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/status-histories/{id}")
    public ResponseEntity<Void> deleteStatusHistory(@PathVariable Long id) {
        log.debug("REST request to delete StatusHistory : {}", id);
        statusHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
