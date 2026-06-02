package com.orgacare.app.web.rest;

import com.orgacare.app.repository.FormeJuridiqueRepository;
import com.orgacare.app.service.FormeJuridiqueService;
import com.orgacare.app.service.dto.FormeJuridiqueDTO;
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
 * REST controller for managing {@link com.orgacare.app.domain.FormeJuridique}.
 */
@RestController
@RequestMapping("/api")
public class FormeJuridiqueResource {

    private final Logger log = LoggerFactory.getLogger(FormeJuridiqueResource.class);

    private static final String ENTITY_NAME = "orgaCareFormeJuridique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormeJuridiqueService formeJuridiqueService;

    public FormeJuridiqueResource(FormeJuridiqueService formeJuridiqueService) {
        this.formeJuridiqueService = formeJuridiqueService;
    }

    /**
     * {@code POST  /forme-juridiques} : Create a new formeJuridique.
     *
     * @param formeJuridiqueDTO the formeJuridiqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formeJuridiqueDTO, or with status {@code 400 (Bad Request)} if the formeJuridique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forme-juridiques")
    public ResponseEntity<FormeJuridiqueDTO> createFormeJuridique(@RequestBody FormeJuridiqueDTO formeJuridiqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormeJuridique : {}", formeJuridiqueDTO);
        if (formeJuridiqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new formeJuridique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormeJuridiqueDTO result = formeJuridiqueService.save(formeJuridiqueDTO);
        return ResponseEntity
            .created(new URI("/api/forme-juridiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forme-juridiques} : Updates an existing formeJuridique.
     *
     * @param formeJuridiqueDTO the formeJuridiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formeJuridiqueDTO,
     * or with status {@code 400 (Bad Request)} if the formeJuridiqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formeJuridiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forme-juridiques")
    public ResponseEntity<FormeJuridiqueDTO> updateFormeJuridique(@RequestBody FormeJuridiqueDTO formeJuridiqueDTO)
        throws URISyntaxException {
        log.debug("REST request to update FormeJuridique : {}", formeJuridiqueDTO);
        if (formeJuridiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormeJuridiqueDTO result = formeJuridiqueService.save(formeJuridiqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formeJuridiqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /forme-juridiques} : get all the formeJuridiques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formeJuridiques in body.
     */
    @GetMapping("/forme-juridiques")
    public List<FormeJuridiqueDTO> getAllFormeJuridiques() {
        log.debug("REST request to get all FormeJuridiques");
        return formeJuridiqueService.findAll();
    }

    /**
     * {@code GET  /forme-juridiques/:id} : get the "id" formeJuridique.
     *
     * @param id the id of the formeJuridiqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formeJuridiqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forme-juridiques/{id}")
    public ResponseEntity<FormeJuridiqueDTO> getFormeJuridique(@PathVariable Long id) {
        log.debug("REST request to get FormeJuridique : {}", id);
        Optional<FormeJuridiqueDTO> formeJuridiqueDTO = formeJuridiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formeJuridiqueDTO);
    }

    /**
     * {@code DELETE  /forme-juridiques/:id} : delete the "id" formeJuridique.
     *
     * @param id the id of the formeJuridiqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forme-juridiques/{id}")
    public ResponseEntity<Void> deleteFormeJuridique(@PathVariable Long id) {
        log.debug("REST request to delete FormeJuridique : {}", id);
        formeJuridiqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/forme-juridiques/{id}/passif")
    public ResponseEntity<FormeJuridiqueDTO> setFormeJuridiqueToPassif(@PathVariable Long id) {
        log.debug("REST request to set FormeJuridique {} to passif", id);

        FormeJuridiqueDTO result = formeJuridiqueService.updateEtatToPassif(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }
}
