package com.orgacare.app.web.rest;

import com.orgacare.app.repository.SalaireRepository;
import com.orgacare.app.service.SalaireService;
import com.orgacare.app.service.dto.SalaireDTO;
import com.orgacare.app.service.dto.SalaireImportResultDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.Salaire}.
 */
@RestController
@RequestMapping("/api")
public class SalaireResource {

    private final Logger log = LoggerFactory.getLogger(SalaireResource.class);

    private static final String ENTITY_NAME = "orgaCareSalaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaireService salaireService;

    private final SalaireRepository salaireRepository;

    public SalaireResource(SalaireService salaireService, SalaireRepository salaireRepository) {
        this.salaireService = salaireService;
        this.salaireRepository = salaireRepository;
    }

    /**
     * {@code POST  /salaires} : Create a new salaire.
     *
     * @param salaireDTO the salaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaireDTO, or with status {@code 400 (Bad Request)} if the salaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salaires")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<SalaireDTO> createSalaire(@Valid @RequestBody SalaireDTO salaireDTO) throws URISyntaxException {
        log.debug("REST request to save Salaire : {}", salaireDTO);
        if (salaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalaireDTO result = salaireService.save(salaireDTO);
        return ResponseEntity
            .created(new URI("/api/salaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salaires/:id} : Updates an existing salaire.
     *
     * @param id the id of the salaireDTO to save.
     * @param salaireDTO the salaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaireDTO,
     * or with status {@code 400 (Bad Request)} if the salaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salaires/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<SalaireDTO> updateSalaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaireDTO salaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Salaire : {}, {}", id, salaireDTO);
        if (salaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalaireDTO result = salaireService.save(salaireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salaires/:id} : Partial updates given fields of an existing salaire, field will ignore if it is null
     *
     * @param id the id of the salaireDTO to save.
     * @param salaireDTO the salaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaireDTO,
     * or with status {@code 400 (Bad Request)} if the salaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<SalaireDTO> partialUpdateSalaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaireDTO salaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Salaire partially : {}, {}", id, salaireDTO);
        if (salaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalaireDTO> result = salaireService.partialUpdate(salaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /salaires} : get all the salaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaires in body.
     */
    @GetMapping("/salaires")
    public List<SalaireDTO> getAllSalaires() {
        log.debug("REST request to get all Salaires");
        return salaireService.findAll();
    }

    /**
     * {@code GET  /salaires/by-personne/:personneId} : historique des salaires d'une personne.
     */
    @GetMapping("/salaires/by-personne/{personneId}")
    public List<SalaireDTO> getSalairesByPersonne(@PathVariable Long personneId) {
        log.debug("REST request to get Salaires by Personne : {}", personneId);
        return salaireService.findByPersonneId(personneId);
    }

    /**
     * {@code GET  /salaires/import-template} : télécharger le modèle Excel (Matricule ; Salaire).
     */
    @GetMapping("/salaires/import-template")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        byte[] content = salaireService.generateImportTemplate();
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modele_salaires.xlsx\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(content);
    }

    /**
     * {@code POST  /salaires/import} : import en masse de salaires depuis un fichier Excel.
     */
    @PostMapping(value = "/salaires/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<SalaireImportResultDTO> importSalaires(@RequestParam("file") MultipartFile file) {
        log.debug("REST request to import Salaires from Excel : {}", file.getOriginalFilename());
        return ResponseEntity.ok(salaireService.importFromExcel(file));
    }

    /**
     * {@code GET  /salaires/:id} : get the "id" salaire.
     *
     * @param id the id of the salaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salaires/{id}")
    public ResponseEntity<SalaireDTO> getSalaire(@PathVariable Long id) {
        log.debug("REST request to get Salaire : {}", id);
        Optional<SalaireDTO> salaireDTO = salaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaireDTO);
    }

    /**
     * {@code DELETE  /salaires/:id} : delete the "id" salaire.
     *
     * @param id the id of the salaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salaires/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RH')")
    public ResponseEntity<Void> deleteSalaire(@PathVariable Long id) {
        log.debug("REST request to delete Salaire : {}", id);
        salaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
