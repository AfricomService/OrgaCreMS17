package com.orgacare.app.web.rest;

import com.orgacare.app.domain.Societe;
import com.orgacare.app.repository.ContratRepository;
import com.orgacare.app.repository.SocieteRepository;
import com.orgacare.app.service.ContratService;
import com.orgacare.app.service.dto.ContratDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.Contrat}.
 */
@RestController
@RequestMapping("/api")
public class ContratResource {

    private final Logger log = LoggerFactory.getLogger(ContratResource.class);

    private static final String ENTITY_NAME = "orgaCareContrat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContratService contratService;
    private final SocieteRepository societeRepository;

    public ContratResource(ContratService contratService, SocieteRepository societeRepository) {
        this.contratService = contratService;
        this.societeRepository = societeRepository;
    }

    /**
     * {@code POST  /contrats} : Create a new contrat.
     *
     * @param contratDTO the contratDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contratDTO, or with status {@code 400 (Bad Request)} if the contrat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contrats")
    public ResponseEntity<ContratDTO> createContrat(@RequestBody ContratDTO contratDTO) throws URISyntaxException {
        log.debug("REST request to save Contrat : {}", contratDTO);
        if (contratDTO.getId() != null) {
            throw new BadRequestAlertException("A new contrat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContratDTO result = contratService.save(contratDTO);
        return ResponseEntity
            .created(new URI("/api/contrats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contrats} : Updates an existing contrat.
     *
     * @param contratDTO the contratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contratDTO,
     * or with status {@code 400 (Bad Request)} if the contratDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contrats")
    public ResponseEntity<ContratDTO> updateContrat(@RequestBody ContratDTO contratDTO) throws URISyntaxException {
        log.debug("REST request to update Contrat : {}", contratDTO);
        if (contratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContratDTO result = contratService.save(contratDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contratDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contrats} : get all the contrats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contrats in body.
     */
    @GetMapping("/contrats")
    public ResponseEntity<List<ContratDTO>> getAllContrats(Pageable pageable) {
        log.debug("REST request to get a page of Contrats");
        Page<ContratDTO> page = contratService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contrats/:id} : get the "id" contrat.
     *
     * @param id the id of the contratDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contratDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contrats/{id}")
    public ResponseEntity<ContratDTO> getContrat(@PathVariable Long id) {
        log.debug("REST request to get Contrat : {}", id);
        Optional<ContratDTO> contratDTO = contratService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contratDTO);
    }

    /**
     * {@code DELETE  /contrats/:id} : delete the "id" contrat.
     *
     * @param id the id of the contratDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contrats/{id}")
    public ResponseEntity<Void> deleteContrat(@PathVariable Long id) {
        log.debug("REST request to delete Contrat : {}", id);
        contratService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/contrats/by-personne/{personneId}")
    public ResponseEntity<List<ContratDTO>> getContratsBypersonneId(@PathVariable Long personneId) {
        List<ContratDTO> contrats = contratService.findBypersonneId(personneId);
        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/contrats/search")
    public ResponseEntity<List<ContratDTO>> searchContratsByDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        log.debug("REST request to search Contrats by dates: {} and {}", startDate, endDate);
        List<ContratDTO> contrats = contratService.findByDates(startDate, endDate);
        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/contrats/search1")
    public ResponseEntity<List<ContratDTO>> searchContrats(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        log.debug("REST request to search Contrats with keyword: {}", keyword);

        if (keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        Page<ContratDTO> page = contratService.searchByKeyword(pageable, keyword);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/testTypeContrat")
    public ResponseEntity<List<ContratDTO>> getContratByTypeContrat(@RequestParam List<Long> typeContratIds) {
        //        log.debug("REST request to search Contrats by dates: {} and {}", startDate, endDate);
        List<ContratDTO> contrats = contratService.getContratByTypeContrat(typeContratIds);
        return ResponseEntity.ok().body(contrats);
    }

    @GetMapping("/contrats/filter")
    public ResponseEntity<List<ContratDTO>> filterContrats(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        @RequestParam(required = false) String criteria,
        @RequestParam(required = false) Long typeContratId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long societeId
    ) {
        log.debug(
            "REST request to filter Contrats by criteria: dateDebut={}, dateFin={}, criteria={}, typeContratId={}, status={}, societeId={}",
            dateDebut,
            dateFin,
            criteria,
            typeContratId,
            status,
            societeId
        );

        List<ContratDTO> filteredContrats = contratService.filterContrats(dateDebut, dateFin, criteria, typeContratId, status, societeId);
        dateDebut = null;
        dateFin = null;
        criteria = null;
        typeContratId = null;
        status = null;
        societeId = null;

        return ResponseEntity.ok(filteredContrats);
    }

    @PutMapping("/contrats/{id}/passif")
    public ResponseEntity<ContratDTO> setContratToPassif(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestAlertException("L'ID du contrat est invalide ou manquant", "contrat", "idnull");
        }

        log.debug("REST request to set Contract {} to passif", id);

        ContratDTO result = contratService.updateEtatToPassif(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    @PostMapping("/contrats/importer")
    public ResponseEntity<?> importerContrats(@RequestParam("file") MultipartFile file, @RequestParam("societeId") Long societeId) {
        log.info("Fichier reçu : {}", file.getOriginalFilename());
        log.info("societeId reçu : {}", societeId);

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est vide. Veuillez fournir un fichier Excel valide.");
        }

        try {
            Societe societe = societeRepository
                .findById(societeId)
                .orElseThrow(() -> new IllegalArgumentException("Société introuvable avec l'ID : " + societeId));

            File excelFile = convertMultipartFileToFile(file);
            File fichierAvecErreurs = contratService.importerContrats(excelFile, societeId);

            if (fichierAvecErreurs != null) {
                byte[] fileContent = Files.readAllBytes(fichierAvecErreurs.toPath());
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fichierAvecErreurs.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileContent);
            }

            return ResponseEntity.ok(Collections.singletonMap("message", "Importation réussie !"));
        } catch (Exception e) {
            log.error("Erreur lors de l'importation des contrats : ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("message", "Une erreur s'est produite : " + e.getMessage()));
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("import", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
    //    @PostMapping("/contrats/import-excell")
    //    public ResponseEntity<?> importFromExcel(@RequestParam("file") MultipartFile file) {
    //        if (file == null || file.isEmpty()) {
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                .body("Le fichier fourni est vide ou invalide.");
    //        }
    //
    //        try {
    //            // Charge les sociétés, les personnes et les types de contrats dans des maps
    //            Map<String, Long> societeMap = contratService.loadSocieteMap();
    //            Map<String, Long> personneMap = contratService.loadPersonneMap();
    //            Map<String, Long> typeContratMap = contratService.loadTypeContratMap();
    //
    //            // Appel au service pour importer les contrats en utilisant les maps
    //            List<ContratDTO> importedContracts = contratService.importFromExcel(file, societeMap, personneMap, typeContratMap);
    //
    //            if (importedContracts.isEmpty()) {
    //                return ResponseEntity.status(HttpStatus.NO_CONTENT)
    //                    .body("Aucun contrat valide n'a été importé depuis le fichier.");
    //            }
    //
    //            return ResponseEntity.ok(importedContracts);
    //        } catch (IOException e) {
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                .body("Erreur lors de la lecture du fichier : " + e.getMessage());
    //        } catch (Exception e) {
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                .body("Erreur inattendue : " + e.getMessage());
    //        }
    //    }

}
