package com.orgacare.app.web.rest;

import com.orgacare.app.domain.Departement;
import com.orgacare.app.repository.DepartementRepository;
import com.orgacare.app.service.AffectationService;
import com.orgacare.app.service.DepartementService;
import com.orgacare.app.service.dto.DepartementDTO;
import com.orgacare.app.service.dto.DepartementTreeDTO;
import com.orgacare.app.service.mapper.DepartementMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
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
 * REST controller for managing {@link com.orgacare.app.domain.Departement}.
 */
@RestController
@RequestMapping("/api")
public class DepartementResource {

    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "orgaCareDepartement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementService departementService;
    private final AffectationService affectationService;
    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;

    public DepartementResource(
        DepartementService departementService,
        DepartementRepository departementRepository,
        AffectationService affectationService,
        DepartementMapper departementMapper
    ) {
        this.departementService = departementService;
        this.affectationService = affectationService;
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departementDTO the departementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departementDTO, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departements")
    public ResponseEntity<DepartementDTO> createDepartement(@Valid @RequestBody DepartementDTO departementDTO) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departementDTO);
        if (departementDTO.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartementDTO result = departementService.save(departementDTO);
        return ResponseEntity
            .created(new URI("/api/departements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departements} : Updates an existing departement.
     *
     * @param departementDTO the departementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departementDTO,
     * or with status {@code 400 (Bad Request)} if the departementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departements")
    public ResponseEntity<DepartementDTO> updateDepartement(@Valid @RequestBody DepartementDTO departementDTO) throws URISyntaxException {
        log.debug("REST request to update Departement : {}", departementDTO);
        if (departementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartementDTO result = departementService.save(departementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("/departements")
    public ResponseEntity<List<DepartementDTO>> getAllDepartements(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Departements");
        Page<DepartementDTO> page;
        if (eagerload) {
            page = departementService.findAllWithEagerRelationships(pageable);
        } else {
            page = departementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/departements/All")
    public ResponseEntity<List<DepartementDTO>> getAllDepartements() {
        List<DepartementDTO> departements = departementRepository
            .findAll()
            .stream()
            .map(departementMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(departements);
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departements/{id}")
    public ResponseEntity<DepartementDTO> getDepartement(@PathVariable Long id) {
        log.debug("REST request to get Departement : {}", id);
        Optional<DepartementDTO> departementDTO = departementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departementDTO);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departements/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        log.debug("REST request to delete Departement : {}", id);
        departementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/departements/by-organigramme/{organigrammeId}")
    public ResponseEntity<List<DepartementDTO>> getDepartementsByOrganigrammeId(@PathVariable Long organigrammeId) {
        List<DepartementDTO> departements = departementService.findByOrganigrammeId(organigrammeId);
        return ResponseEntity.ok().body(departements);
    }

    @GetMapping("/departements/by-code/{code}")
    public ResponseEntity<DepartementDTO> getDepartementByCode(@PathVariable String code) {
        return departementService.findOnByCode(code).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/departements/ogra-code-by-dept-code")
    public String getOrgaCodeByDeptCode(@RequestParam(name = "deptCode") String deptCode) {
        return departementService.findOrgaByDepartementCode(deptCode);
    }

    @GetMapping("/departements/generate-next-code")
    public ResponseEntity<String> generateNextCode(@RequestParam String prefix) {
        String nextCode = departementService.generateNextCode(prefix);
        return ResponseEntity.ok(nextCode);
    }

    @GetMapping("/departements/{id}/hierarchy")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentHierarchy(@PathVariable Long id) {
        List<Map<String, Object>> hierarchy = affectationService.getDepartmentHierarchy(id);
        return ResponseEntity.ok().body(hierarchy);
    }

    @PutMapping("/departements/{departementId}/deplacer")
    public ResponseEntity<Departement> deplacerDepartement(@PathVariable Long departementId, @RequestParam Long nouveauParentId) {
        Departement result = departementService.deplacerDepartement(departementId, nouveauParentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/departements/with-affectations")
    public ResponseEntity<List<DepartementDTO>> getDepartementsWithAffectations(
        @RequestParam Long societeId,
        @RequestParam(required = false) Long organigrammeId
    ) {
        List<DepartementDTO> result = departementService.getDepartementsWithAffectations(societeId, organigrammeId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/departementshierarchy/{organigrammeCode}")
    public ResponseEntity<List<DepartementTreeDTO>> getDepartementHierarchy(@PathVariable("organigrammeCode") String organigrammeCode) {
        log.debug("REST request to get departement tree for organigramme : {}", organigrammeCode);

        List<DepartementTreeDTO> tree = departementService.getDepartementTreeByOrganigramme(organigrammeCode);

        // préférence personnelle : renvoyer 200 + [] plutôt que 204 No Content (plus simple côté client)
        if (tree == null || tree.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(tree);
    }

    @GetMapping("/departements/tree/{organigrammeCode}")
    public ResponseEntity<List<DepartementTreeDTO>> getDepartementTreeOnly(@PathVariable("organigrammeCode") String organigrammeCode) {
        log.debug("REST request to get simple departement tree for organigramme : {}", organigrammeCode);

        List<DepartementTreeDTO> tree = departementService.getDepartementTreeOnlyByOrganigramme(organigrammeCode);

        if (tree == null || tree.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/departement-code-with-children/{deptCode}")
    public ResponseEntity<List<String>> getDepartementAndChildrenCodes(@PathVariable String deptCode) {
        List<String> tree = departementService.getDepartementAndChildrenCodes(deptCode);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/departement-code-with-parents/{deptCode}")
    public ResponseEntity<List<String>> getDepartementAndParentsCodes(@PathVariable String deptCode) {
        List<String> tree = departementService.getDepartementAndParentsCodes(deptCode);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/departements/map-parents-by-code/{deptCode}")
    public ResponseEntity<Map<String, List<String>>> getMapDepartementParentsByCode(@PathVariable(name = "deptCode") String deptCode) {
        Map<String, List<String>> result = departementService.getDepartementAndParentsCodesByNom(deptCode);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/departements/by-code-or-throw/{code}")
    public ResponseEntity<DepartementDTO> getDepartementByCodeOrThrow(@PathVariable String code) {
        log.debug("REST request to get Departement by code (or throw) : {}", code);
        try {
            DepartementDTO dto = departementService.getDepartementByCodeOrThrow(code);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            // si tu veux renvoyer 404 au lieu d'exception, tu peux catcher et retourner notFound()
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/departements-nom-by-code/{deptCode}")
    public ResponseEntity<String> getDepartementNomByCode(@PathVariable String deptCode) {
        String cleaned = deptCode == null ? null : deptCode.trim();
        log.debug("REST request to get Departement : '{}'", cleaned);
        return ResponseUtil.wrapOrNotFound(departementRepository.findByCodeIgnoreCase(cleaned).map(Departement::getNom));
    }

    @PostMapping("/departements/names-by-codes")
    public ResponseEntity<Map<String, String>> getNamesByCodes(@RequestBody List<String> codes) {
        return ResponseEntity.ok(departementService.findDepartementNamesByCodes(codes));
    }

    @GetMapping("/departements/user/{userId}/affectations")
    public ResponseEntity<List<Map<String, Object>>> getDepartementAffectationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(departementService.findDepartementNamesAndAffectationTypesByUserId(userId));
    }

    @GetMapping("/departements/nom-by-matricule/{matricule}")
    public ResponseEntity<String> getDepartementNomByMatricule(@PathVariable String matricule) {
        log.debug("REST request to get departement nom for matricule : {}", matricule);
        // Chercher les affectations actives de la personne par matricule
        String nomDept = departementService.findFirstDepartementNomByMatricule(matricule);
        if (nomDept == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nomDept);
    }

    /**
     * GET /api/departements/by-matricule/{matricule}
     * Retourne le premier département actif (id, code, nom, email) d'une personne
     * identifiée par son matricule. Utilisé par correspmanage pour la tâche BOC département.
     */
    @GetMapping("/departements/by-matricule/{matricule}")
    public ResponseEntity<DepartementDTO> getDepartementByMatricule(@PathVariable String matricule) {
        log.debug("REST request to get departement for matricule : {}", matricule);
        return departementService
            .findFirstDepartementByMatricule(matricule)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
