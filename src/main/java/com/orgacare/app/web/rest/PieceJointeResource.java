package com.orgacare.app.web.rest;

import com.orgacare.app.repository.PieceJointeRepository;
import com.orgacare.app.service.PieceJointeService;
import com.orgacare.app.service.dto.PieceJointeDTO;
import com.orgacare.app.service.dto.PieceJointeRenameDTO;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orgacare.app.domain.PieceJointe}.
 */
@RestController
@RequestMapping("/api")
public class PieceJointeResource {

    private final Logger log = LoggerFactory.getLogger(PieceJointeResource.class);

    private static final String ENTITY_NAME = "orgaCarePieceJointe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PieceJointeService pieceJointeService;

    private final PieceJointeRepository pieceJointeRepository;

    public PieceJointeResource(PieceJointeService pieceJointeService, PieceJointeRepository pieceJointeRepository) {
        this.pieceJointeService = pieceJointeService;
        this.pieceJointeRepository = pieceJointeRepository;
    }

    /**
     * {@code POST  /piece-jointes} : Create a new pieceJointe.
     *
     * @param pieceJointeDTO the pieceJointeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pieceJointeDTO, or with status {@code 400 (Bad Request)} if the pieceJointe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/piece-jointes")
    public ResponseEntity<PieceJointeDTO> createPieceJointe(@RequestBody PieceJointeDTO pieceJointeDTO) throws URISyntaxException {
        log.debug("REST request to save PieceJointe : {}", pieceJointeDTO);
        if (pieceJointeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pieceJointe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PieceJointeDTO result = pieceJointeService.save(pieceJointeDTO);
        return ResponseEntity
            .created(new URI("/api/piece-jointes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /piece-jointes/:id} : Updates an existing pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to save.
     * @param pieceJointeDTO the pieceJointeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointeDTO,
     * or with status {@code 400 (Bad Request)} if the pieceJointeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/piece-jointes/{id}")
    public ResponseEntity<PieceJointeDTO> updatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PieceJointeDTO pieceJointeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PieceJointe : {}, {}", id, pieceJointeDTO);
        if (pieceJointeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PieceJointeDTO result = pieceJointeService.save(pieceJointeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /piece-jointes/:id} : Partial updates given fields of an existing pieceJointe, field will ignore if it is null
     *
     * @param id the id of the pieceJointeDTO to save.
     * @param pieceJointeDTO the pieceJointeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointeDTO,
     * or with status {@code 400 (Bad Request)} if the pieceJointeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pieceJointeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/piece-jointes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PieceJointeDTO> partialUpdatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PieceJointeDTO pieceJointeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PieceJointe partially : {}, {}", id, pieceJointeDTO);
        if (pieceJointeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PieceJointeDTO> result = pieceJointeService.partialUpdate(pieceJointeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /piece-jointes} : get all the pieceJointes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pieceJointes in body.
     */
    @GetMapping("/piece-jointes")
    public List<PieceJointeDTO> getAllPieceJointes() {
        log.debug("REST request to get all PieceJointes");
        return pieceJointeService.findAll();
    }

    /**
     * {@code GET  /piece-jointes/:id} : get the "id" pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pieceJointeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/piece-jointes/{id}")
    public ResponseEntity<PieceJointeDTO> getPieceJointe(@PathVariable Long id) {
        log.debug("REST request to get PieceJointe : {}", id);
        Optional<PieceJointeDTO> pieceJointeDTO = pieceJointeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pieceJointeDTO);
    }

    /**
     * {@code DELETE  /piece-jointes/:id} : delete the "id" pieceJointe.
     *
     * @param id the id of the pieceJointeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/piece-jointes/{id}")
    public ResponseEntity<Void> deletePieceJointe(@PathVariable Long id) {
        log.debug("REST request to delete PieceJointe : {}", id);
        pieceJointeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    // ═══════════ Pièces jointes d'une SessionFormation ═══════════

    @PostMapping("/piece-jointes/upload-session")
    public ResponseEntity<PieceJointeDTO> uploadPieceJointeSession(
        @RequestParam("file") MultipartFile file,
        @RequestParam("sessionId") Long sessionId
    ) {
        log.debug("REST request to upload PieceJointe for SessionFormation : {}", sessionId);
        try {
            return ResponseEntity.ok(pieceJointeService.uploadForSession(file, sessionId));
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidupload");
        } catch (IOException e) {
            log.error("Upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/piece-jointes/by-session/{sessionId}")
    public List<PieceJointeDTO> getPieceJointesBySession(@PathVariable Long sessionId) {
        log.debug("REST request to get PieceJointes by SessionFormation : {}", sessionId);
        return pieceJointeService.findBySessionId(sessionId);
    }

    // ═══════════ Pièces jointes d'une Formation ═══════════

    @PostMapping("/piece-jointes/upload-formation")
    public ResponseEntity<PieceJointeDTO> uploadPieceJointeFormation(
        @RequestParam("file") MultipartFile file,
        @RequestParam("formationId") Long formationId
    ) {
        log.debug("REST request to upload PieceJointe for Formation : {}", formationId);
        try {
            return ResponseEntity.ok(pieceJointeService.uploadForFormation(file, formationId));
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidupload");
        } catch (IOException e) {
            log.error("Upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/piece-jointes/by-formation/{formationId}")
    public List<PieceJointeDTO> getPieceJointesByFormation(@PathVariable Long formationId) {
        log.debug("REST request to get PieceJointes by Formation : {}", formationId);
        return pieceJointeService.findByFormationId(formationId);
    }

    @GetMapping("/piece-jointes/getFile")
    public ResponseEntity<org.springframework.core.io.Resource> getFile(@RequestParam(name = "id") Long id) {
        return pieceJointeService.getFile(id);
    }

    /**
     * {@code PATCH /piece-jointes/:id/rename} : renomme une pièce jointe.
     */
    @PatchMapping("/piece-jointes/{id}/rename")
    public ResponseEntity<PieceJointeDTO> renamePieceJointe(@PathVariable Long id, @Valid @RequestBody PieceJointeRenameDTO renameDTO) {
        log.debug("REST request to rename PieceJointe {} : {}", id, renameDTO.getNomFichier());

        if (pieceJointeService.findOne(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PieceJointeDTO result = pieceJointeService.renamePieceJointe(id, renameDTO.getNomFichier());
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }
}
