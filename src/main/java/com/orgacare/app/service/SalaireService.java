package com.orgacare.app.service;

import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.Salaire;
import com.orgacare.app.domain.enumeration.AclPermission;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.repository.SalaireRepository;
import com.orgacare.app.service.dto.SalaireDTO;
import com.orgacare.app.service.dto.SalaireImportResultDTO;
import com.orgacare.app.service.mapper.SalaireMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Salaire}.
 */
@Service
@Transactional
public class SalaireService {

    private final Logger log = LoggerFactory.getLogger(SalaireService.class);

    public static final String ACL_TYPE = "SALAIRE";

    private static final String ENTITY_NAME = "orgaCareSalaire";

    private static final int MAX_IMPORT_ROWS = 2000;

    private static final long MAX_IMPORT_FILE_SIZE = 2L * 1024 * 1024; // 2 Mo

    private final SalaireRepository salaireRepository;

    private final SalaireMapper salaireMapper;

    private final AclUtilService aclUtilService;

    private final PersonneRepository personneRepository;

    public SalaireService(
        SalaireRepository salaireRepository,
        SalaireMapper salaireMapper,
        AclUtilService aclUtilService,
        PersonneRepository personneRepository
    ) {
        this.salaireRepository = salaireRepository;
        this.salaireMapper = salaireMapper;
        this.aclUtilService = aclUtilService;
        this.personneRepository = personneRepository;
    }

    /**
     * Save a salaire.
     *
     * @param salaireDTO the entity to save.
     * @return the persisted entity.
     */
    public SalaireDTO save(SalaireDTO salaireDTO) {
        boolean isNew = salaireDTO.getId() == null;

        // Modification : il faut le droit WRITE
        if (!isNew && !aclUtilService.canWrite(ACL_TYPE, salaireDTO.getId())) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier ce salaire");
        }

        // Création : le créateur doit avoir un matricule (sauf ADMIN)
        if (isNew && !aclUtilService.isAdmin() && aclUtilService.getCurrentMatricule().isEmpty()) {
            throw new BadRequestAlertException(
                "Votre compte n'est lié à aucune personne (matricule manquant).",
                ENTITY_NAME,
                "nomatricule"
            );
        }

        log.debug("Request to save Salaire : {}", salaireDTO);
        Salaire salaire = salaireMapper.toEntity(salaireDTO);
        salaire = salaireRepository.save(salaire);
        final Long salaireId = salaire.getId();

        // 1) le créateur : READ + WRITE
        if (isNew && aclUtilService.getCurrentMatricule().isPresent()) {
            aclUtilService.grantOwnerToCurrentUser(ACL_TYPE, salaireId);
        }

        // 2) la personne concernée : READ (SID = son matricule), idempotent
        //    -> supprime ce bloc si la personne ne doit pas voir son salaire
        if (salaire.getPersonneId() != null) {
            personneRepository
                .findById(salaire.getPersonneId())
                .map(Personne::getMatricule)
                .filter(m -> !m.isBlank())
                .ifPresent(m -> aclUtilService.grantReadToMatricule(ACL_TYPE, salaireId, m));
        }
        return salaireMapper.toDto(salaire);
    }

    /**
     * Partially update a salaire.
     *
     * @param salaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalaireDTO> partialUpdate(SalaireDTO salaireDTO) {
        log.debug("Request to partially update Salaire : {}", salaireDTO);

        if (!aclUtilService.canWrite(ACL_TYPE, salaireDTO.getId())) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier ce salaire");
        }

        return salaireRepository
            .findById(salaireDTO.getId())
            .map(existingSalaire -> {
                salaireMapper.partialUpdate(existingSalaire, salaireDTO);

                return existingSalaire;
            })
            .map(salaireRepository::save)
            .map(salaireMapper::toDto);
    }

    /**
     * Get all the salaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalaireDTO> findAll() {
        log.debug("Request to get all Salaires");
        Set<Long> readable = readableIds();
        if (readable == null) {
            return salaireRepository.findAll().stream().map(salaireMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        }
        if (readable.isEmpty()) {
            return new LinkedList<>();
        }
        return salaireRepository
            .findAllByIdIn(readable)
            .stream()
            .map(salaireMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Historique des salaires d'une personne (du plus récent au plus ancien).
     */
    @Transactional(readOnly = true)
    public List<SalaireDTO> findByPersonneId(Long personneId) {
        log.debug("Request to get Salaires by Personne : {}", personneId);
        final Set<Long> readable = readableIds();
        return salaireRepository
            .findAllByPersonneIdOrderByDateDesc(personneId)
            .stream()
            .filter(s -> readable == null || readable.contains(s.getId()))
            .map(salaireMapper::toDto)
            .collect(Collectors.toList());
    }

    /** null = ADMIN (aucun filtre) ; sinon ids des salaires lisibles par l'utilisateur connecté. */
    private Set<Long> readableIds() {
        return aclUtilService.isAdmin() ? null : aclUtilService.getAccessibleObjectIds(ACL_TYPE, AclPermission.READ);
    }

    /**
     * Get one salaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalaireDTO> findOne(Long id) {
        log.debug("Request to get Salaire : {}", id);
        if (!aclUtilService.canRead(ACL_TYPE, id)) {
            throw new AccessDeniedException("Accès refusé à ce salaire");
        }
        return salaireRepository.findById(id).map(salaireMapper::toDto);
    }

    /**
     * Génère le modèle Excel (colonnes Matricule ; Salaire).
     */
    @Transactional(readOnly = true)
    public byte[] generateImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Salaires");

            // Colonne Matricule en format texte (évite la perte des zéros initiaux)
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
            sheet.setDefaultColumnStyle(0, textStyle);

            CellStyle headerStyle = workbook.createCellStyle();
            Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            Row header = sheet.createRow(0);
            Cell c0 = header.createCell(0);
            c0.setCellValue("Matricule");
            c0.setCellStyle(headerStyle);
            Cell c1 = header.createCell(1);
            c1.setCellValue("Salaire");
            c1.setCellStyle(headerStyle);

            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 5000);

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de générer le modèle Excel", e);
        }
    }

    /**
     * Import en masse de salaires depuis un fichier Excel (.xlsx).
     * Colonnes : Matricule | Salaire. La date du salaire = date du jour.
     * Import partiel : les lignes valides sont enregistrées, les autres sont listées dans les erreurs.
     */
    public SalaireImportResultDTO importFromExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestAlertException("Fichier vide ou manquant", ENTITY_NAME, "fileempty");
        }
        String filename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!filename.endsWith(".xlsx")) {
            throw new BadRequestAlertException("Format invalide : seul .xlsx est accepté", ENTITY_NAME, "fileformat");
        }
        if (file.getSize() > MAX_IMPORT_FILE_SIZE) {
            throw new BadRequestAlertException("Fichier trop volumineux (max 2 Mo)", ENTITY_NAME, "filetoolarge");
        }

        // 1) Lecture du fichier
        final class RowData {

            final int line;
            final String matricule;
            final Double valeur;
            final String parseError;

            RowData(int line, String matricule, Double valeur, String parseError) {
                this.line = line;
                this.matricule = matricule;
                this.valeur = valeur;
                this.parseError = parseError;
            }
        }

        List<RowData> rows = new ArrayList<>();
        try (InputStream in = file.getInputStream(); Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() > MAX_IMPORT_ROWS) {
                throw new BadRequestAlertException("Trop de lignes (max " + MAX_IMPORT_ROWS + ")", ENTITY_NAME, "toomanyrows");
            }
            DataFormatter formatter = new DataFormatter();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                String matricule = formatter.formatCellValue(row.getCell(0)).trim();
                Cell salaireCell = row.getCell(1);
                String salaireRaw = formatter.formatCellValue(salaireCell).trim();
                if (matricule.isEmpty() && salaireRaw.isEmpty()) {
                    continue; // ligne vide
                }
                Double valeur = null;
                String parseError = null;
                try {
                    valeur = parseValeur(salaireCell, salaireRaw);
                } catch (NumberFormatException e) {
                    parseError = "salaire invalide (" + salaireRaw + ")";
                }
                rows.add(new RowData(r + 1, matricule, valeur, parseError));
            }
        } catch (IOException e) {
            throw new BadRequestAlertException("Fichier Excel illisible", ENTITY_NAME, "fileunreadable");
        }

        SalaireImportResultDTO result = new SalaireImportResultDTO();
        result.setTotal(rows.size());
        if (rows.isEmpty()) {
            return result;
        }

        // 2) Recherche des personnes par matricule (une seule requête)
        Set<String> matricules = new HashSet<>();
        rows.stream().map(rd -> rd.matricule).filter(m -> !m.isEmpty()).forEach(matricules::add);
        Map<String, List<Personne>> personnesByMatricule = personneRepository
            .findAllByMatriculeIn(matricules)
            .stream()
            .collect(Collectors.groupingBy(Personne::getMatricule));

        // 3) Validation + enregistrement ligne par ligne
        Set<String> alreadyProcessed = new HashSet<>();
        for (RowData rd : rows) {
            String prefix = "Ligne " + rd.line + " : ";
            if (rd.matricule.isEmpty()) {
                result.getErrors().add(prefix + "matricule manquant");
                continue;
            }
            if (rd.parseError != null) {
                result.getErrors().add(prefix + rd.parseError);
                continue;
            }
            if (rd.valeur == null || rd.valeur.isNaN() || rd.valeur.isInfinite() || rd.valeur <= 0) {
                result.getErrors().add(prefix + "le salaire doit être un nombre supérieur à 0");
                continue;
            }
            if (!alreadyProcessed.add(rd.matricule)) {
                result.getErrors().add(prefix + "matricule en doublon dans le fichier (" + rd.matricule + ")");
                continue;
            }
            List<Personne> found = personnesByMatricule.get(rd.matricule);
            if (found == null || found.isEmpty()) {
                result.getErrors().add(prefix + "aucune personne avec le matricule " + rd.matricule);
                continue;
            }
            if (found.size() > 1) {
                result.getErrors().add(prefix + "matricule ambigu (plusieurs personnes) : " + rd.matricule);
                continue;
            }

            SalaireDTO dto = new SalaireDTO();
            dto.setValeur(rd.valeur);
            dto.setPersonneId(found.get(0).getId());
            dto.setDate(ZonedDateTime.now());
            try {
                save(dto); // réutilise la logique existante (ACL créateur + personne concernée)
                result.setImported(result.getImported() + 1);
            } catch (BadRequestAlertException | AccessDeniedException e) {
                result.getErrors().add(prefix + e.getMessage());
            }
        }
        return result;
    }

    private Double parseValeur(Cell cell, String raw) {
        if (cell == null || raw.isEmpty()) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return Double.valueOf(raw.replace(" ", "").replace(',', '.'));
    }

    /**
     * Delete the salaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Salaire : {}", id);
        if (!aclUtilService.canWrite(ACL_TYPE, id)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de supprimer ce salaire");
        }
        salaireRepository.deleteById(id);
        aclUtilService.deleteAcl(ACL_TYPE, id);
    }
}
