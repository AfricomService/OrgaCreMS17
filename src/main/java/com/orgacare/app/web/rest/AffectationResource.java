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

    public AffectationResource(AffectationService affectationService) {
        this.affectationService = affectationService;
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
     * {@code PUT  /affectations} : Updates an existing affectation.
     *
     * @param affectationDTO the affectationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationDTO,
     * or with status {@code 400 (Bad Request)} if the affectationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affectations")
    public ResponseEntity<AffectationDTO> updateAffectation(@Valid @RequestBody AffectationDTO affectationDTO) throws URISyntaxException {
        log.debug("REST request to update Affectation : {}", affectationDTO);
        if (affectationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AffectationDTO result = affectationService.save(affectationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationDTO.getId().toString()))
            .body(result);
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

    @GetMapping("/affectations/by-departement/{departementId}")
    public ResponseEntity<List<AffectationDTO>> getAffectationsByDepartementId(@PathVariable Long departementId) {
        List<AffectationDTO> affectations = affectationService.findByDepartementId(departementId);
        return ResponseEntity.ok().body(affectations);
    }

    @GetMapping("/affectations/by-personne/{personneId}")
    public ResponseEntity<List<AffectationDTO>> getAffectationsByPersonneId(@PathVariable Long personneId) {
        List<AffectationDTO> affectations = affectationService.findByPersonneId(personneId);
        return ResponseEntity.ok().body(affectations);
    }

    @GetMapping("/affectations/emails-by-departement-and-type")
    public ResponseEntity<List<String>> getEmailsByDepartementAndType(@RequestParam Long departementId, @RequestParam String type) {
        try {
            com.orgacare.app.domain.enumeration.TypeAffectation typeAffectation = com.orgacare.app.domain.enumeration.TypeAffectation.valueOf(
                type.toUpperCase()
            );
            List<String> emails = affectationService.findEmailsOfPersonnesByDepartementIdAndType(departementId, typeAffectation);
            return ResponseEntity.ok(emails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /affectations/affecter-personne
     * Affecter une personne à un département.
     */
    @PostMapping("/affectations/affecter-personne")
    public ResponseEntity<AffectationDTO> affecterPersonne(
        @Valid @RequestBody com.orgacare.app.service.dto.AffecterPersonneRequest request
    ) {
        log.debug("REST request to affecter personne {} au departement {}", request.getPersonneId(), request.getDepartementId());

        if (request.getPersonneId() == null) {
            throw new BadRequestAlertException("personneId est requis", ENTITY_NAME, "personneidnull");
        }
        if (request.getDepartementId() == null) {
            throw new BadRequestAlertException("departementId est requis", ENTITY_NAME, "departementidnull");
        }
        if (request.getType() == null) {
            throw new BadRequestAlertException("type est requis", ENTITY_NAME, "typenull");
        }

        AffectationDTO result = affectationService.affecterPersonne(
            request.getPersonneId(),
            request.getDepartementId(),
            request.getSocieteId(),
            request.getType(),
            request.getDateAction(),
            request.getDateFin()
        );
        return ResponseEntity.ok(result);
    }

    /**
     * GET /affectations/by-personne/{personneId}/active
     * Récupérer toutes les affectations actives d'une personne.
     */
    @GetMapping("/affectations/by-personne/{personneId}/active")
    public ResponseEntity<List<AffectationDTO>> getAffectationsActivesByPersonneId(@PathVariable Long personneId) {
        log.debug("REST request to get affectations actives for personne {}", personneId);

        List<AffectationDTO> result = affectationService
            .findByPersonneId(personneId)
            .stream()
            .filter(a -> a.getEtat() != null && !a.getEtat().name().equals("CANCELED"))
            .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
