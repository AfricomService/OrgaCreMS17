package com.orgacare.app.service;

import com.orgacare.app.domain.Contrat;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.domain.TypeContrat;
import com.orgacare.app.domain.enumeration.AclPermission;
import com.orgacare.app.repository.ContratRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.repository.SocieteRepository;
import com.orgacare.app.repository.TypeContratRepository;
import com.orgacare.app.service.dto.ContratDTO;
import com.orgacare.app.service.mapper.ContratMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContratService {

    private static final String ENTITY_NAME = "contrat";
    public static final String ACL_TYPE = "CONTRAT";

    private final Logger log = LoggerFactory.getLogger(ContratService.class);
    private final ContratRepository contratRepository;
    private final ContratMapper contratMapper;
    private final PersonneRepository personneRepository;
    private final SocieteRepository societeRepository;
    private final TypeContratRepository typeContratRepository;
    private final AclUtilService aclUtilService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    private final PersonneService personneService;

    public ContratService(
        @Lazy PersonneService personneService,
        ContratRepository contratRepository,
        ContratMapper contratMapper,
        PersonneRepository personneRepository,
        SocieteRepository societeRepository,
        TypeContratRepository typeContratRepository,
        AclUtilService aclUtilService
    ) {
        this.contratRepository = contratRepository;
        this.contratMapper = contratMapper;
        this.personneRepository = personneRepository;
        this.societeRepository = societeRepository;
        this.typeContratRepository = typeContratRepository;
        this.personneService = personneService;
        this.aclUtilService = aclUtilService;
    }

    public ContratDTO save(ContratDTO contratDTO) {
        boolean isNew = contratDTO.getId() == null;

        // Modification : il faut le droit WRITE
        if (!isNew && !aclUtilService.canWrite(ACL_TYPE, contratDTO.getId())) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier ce contrat");
        }

        if (hasDateOverlap(contratDTO.getId(), contratDTO.getPersonneId(), contratDTO.getDateDebut(), contratDTO.getDateFin())) {
            throw new BadRequestAlertException("Le contrat chevauche un autre contrat actif.", ENTITY_NAME, "dateoverlap");
        }

        // Création : le créateur doit avoir un matricule (sauf ADMIN)
        if (isNew && !aclUtilService.isAdmin() && aclUtilService.getCurrentMatricule().isEmpty()) {
            throw new BadRequestAlertException(
                "Votre compte n'est lié à aucune personne (matricule manquant).",
                ENTITY_NAME,
                "nomatricule"
            );
        }

        log.debug("Request to save Contrat : {}", contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);
        contrat = contratRepository.save(contrat);
        final Long contratId = contrat.getId();

        // 1) le créateur : READ + WRITE
        if (isNew && aclUtilService.getCurrentMatricule().isPresent()) {
            aclUtilService.grantOwnerToCurrentUser(ACL_TYPE, contratId);
        }

        // 2) la personne du contrat : READ (SID = son matricule), idempotent
        if (contratDTO.getPersonneId() != null) {
            personneRepository
                .findById(contratDTO.getPersonneId())
                .map(Personne::getMatricule)
                .filter(m -> !m.isBlank())
                .ifPresent(m -> aclUtilService.grantReadToMatricule(ACL_TYPE, contratId, m));
        }
        return contratMapper.toDto(contrat);
    }

    public Page<ContratDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contrats");
        Set<Long> readable = readableIds();
        if (readable == null) {
            return contratRepository.findAll(pageable).map(this::mapToDtoWithContrats);
        }
        if (readable.isEmpty()) {
            return Page.empty(pageable);
        }
        return contratRepository.findByIdIn(readable, pageable).map(this::mapToDtoWithContrats);
    }

    public Optional<ContratDTO> findOne(Long id) {
        log.debug("Request to get Contrat : {}", id);
        if (!aclUtilService.canRead(ACL_TYPE, id)) {
            throw new AccessDeniedException("Accès refusé à ce contrat");
        }
        return contratRepository.findById(id).map(this::mapToDtoWithContrats);
    }

    public void delete(Long id) {
        log.debug("Request to delete Contrat : {}", id);
        if (!aclUtilService.canWrite(ACL_TYPE, id)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de supprimer ce contrat");
        }
        contratRepository.deleteById(id);
        aclUtilService.deleteAcl(ACL_TYPE, id);
    }

    public List<ContratDTO> findBypersonneId(Long personneId) {
        final Set<Long> readable = readableIds();
        return contratRepository
            .findByPersonneId(personneId)
            .stream()
            .filter(c -> isReadable(readable, c.getId()))
            .map(this::mapToDtoWithContrats)
            .collect(Collectors.toList());
    }

    /** null = ADMIN (aucun filtre) ; sinon ids des contrats lisibles par l'utilisateur connecté. */
    private Set<Long> readableIds() {
        return aclUtilService.isAdmin() ? null : aclUtilService.getAccessibleObjectIds(ACL_TYPE, AclPermission.READ);
    }

    private boolean isReadable(Set<Long> readable, Long id) {
        return readable == null || readable.contains(id);
    }

    private ContratDTO mapToDtoWithContrats(Contrat contrat) {
        ContratDTO contratDTO = contratMapper.toDto(contrat);
        if (contrat.getPersonne() != null) {
            personneRepository
                .findById(contrat.getPersonne().getId())
                .ifPresent(p -> {
                    contratDTO.setNomPersonne(p.getNomPrenom());
                    contratDTO.setMatriculePersonne(p.getMatricule());
                });
        }
        if (contrat.getSociete() != null) {
            societeRepository
                .findById(contrat.getSociete().getId())
                .ifPresent(s -> contratDTO.setRaisonSocialeSociete(s.getRaisonSociale()));
        }
        if (contrat.getTypeContrat() != null) {
            typeContratRepository.findById(contrat.getTypeContrat().getId()).ifPresent(tc -> contratDTO.setNomTypeContrat(tc.getNom()));
        }
        return contratDTO;
    }

    public Optional<ContratDTO> findContratActifByPersonneId(Long personneId) {
        return contratRepository
            .findByPersonneIdAndStatus(personneId, "ACTIF")
            .stream()
            .findFirst()
            .map(contrat -> {
                ContratDTO dto = contratMapper.toDto(contrat);
                if (contrat.getPersonne() != null) {
                    personneRepository.findById(contrat.getPersonne().getId()).ifPresent(p -> dto.setNomPersonne(p.getNomPrenom()));
                }
                if (contrat.getSociete() != null) {
                    societeRepository
                        .findById(contrat.getSociete().getId())
                        .ifPresent(s -> dto.setRaisonSocialeSociete(s.getRaisonSociale()));
                }
                if (contrat.getTypeContrat() != null) {
                    typeContratRepository.findById(contrat.getTypeContrat().getId()).ifPresent(tc -> dto.setNomTypeContrat(tc.getNom()));
                }
                return dto;
            });
    }

    @Transactional(readOnly = true)
    public Page<ContratDTO> searchByKeyword(Pageable pageable, String keyword) {
        final Set<Long> readable = readableIds();
        if (readable != null && readable.isEmpty()) {
            return Page.empty(pageable);
        }
        Specification<Contrat> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (readable != null) {
                predicates.add(root.get("id").in(readable));
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                String like = "%" + keyword.toLowerCase() + "%";
                Join<Contrat, Personne> pJoin = root.join("personne", JoinType.LEFT);
                Join<Contrat, TypeContrat> tcJoin = root.join("typeContrat", JoinType.LEFT);
                Join<Contrat, Societe> sJoin = root.join("societe", JoinType.LEFT);
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(pJoin.get("nomPrenom")), like),
                        cb.like(cb.lower(tcJoin.get("nom")), like),
                        cb.like(cb.lower(sJoin.get("raisonSociale")), like)
                    )
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return contratRepository
            .findAll(spec, pageable)
            .map(contrat -> {
                ContratDTO dto = contratMapper.toDto(contrat);
                dto.setNomPersonne(contrat.getPersonne() != null ? contrat.getPersonne().getNomPrenom() : null);
                dto.setNomTypeContrat(contrat.getTypeContrat() != null ? contrat.getTypeContrat().getNom() : null);
                dto.setRaisonSocialeSociete(contrat.getSociete() != null ? contrat.getSociete().getRaisonSociale() : null);
                return dto;
            });
    }

    private boolean datesSeChevauchent(ZonedDateTime d1, ZonedDateTime f1, ZonedDateTime d2, ZonedDateTime f2) {
        return (d1.isBefore(f2) || d1.isEqual(f2)) && (f1.isAfter(d2) || f1.isEqual(d2));
    }

    // ── Import Excel ────────────────────────────────────────────────────────────

    public File importerContrats(File excelFile, Long societeId) throws IOException {
        List<String> erreursGlobales = new ArrayList<>();
        List<ContratDTO> contrats = lireFichierExcel(excelFile);
        File fichierMisAJour = new File("contrats_avec_erreurs.xlsx");

        try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = WorkbookFactory.create(fis)) { // ← POI Workbook
            Sheet sheet = workbook.getSheetAt(0);
            int erreurColumnIndex = sheet.getRow(0).getLastCellNum();

            Row headerRow = sheet.getRow(0); // ← POI Row
            Cell erreurHeader = headerRow.createCell(erreurColumnIndex); // ← POI Cell
            erreurHeader.setCellValue("Résultat");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i); // ← POI Row
                ContratDTO contratDTO = contrats.get(i - 1);
                StringBuilder erreurLigne = new StringBuilder();

                Optional<Personne> optPersonne = personneRepository.findByMatricule(contratDTO.getMatriculePersonne());

                if (!optPersonne.isPresent()) {
                    erreurLigne.append("Personne introuvable pour le matricule ").append(contratDTO.getMatriculePersonne()).append(". ");
                } else {
                    Personne personne = optPersonne.get();
                    List<Contrat> contratsExistants = contratRepository.findByPersonneId(personne.getId());
                    ZonedDateTime debutNouv = convertirEnZonedDateTime(contratDTO.getDateDebut());
                    ZonedDateTime finNouv = convertirEnZonedDateTime(contratDTO.getDateFin());

                    for (Contrat existant : contratsExistants) {
                        if (datesSeChevauchent(debutNouv, finNouv, existant.getDateDebut(), existant.getDateFin())) {
                            erreurLigne
                                .append("Chevauchement avec un contrat existant (")
                                .append(existant.getDateDebut())
                                .append(" - ")
                                .append(existant.getDateFin())
                                .append("). ");
                            break;
                        }
                    }
                }

                Cell erreurCell = row.createCell(erreurColumnIndex); // ← POI Cell
                erreurCell.setCellValue(erreurLigne.length() > 0 ? erreurLigne.toString() : "OK");
                if (erreurLigne.length() > 0) erreursGlobales.add(erreurLigne.toString());
            }

            try (FileOutputStream fos = new FileOutputStream(fichierMisAJour)) {
                workbook.write(fos);
            }
        } finally {
            if (excelFile != null && excelFile.exists()) excelFile.delete();
        }

        return erreursGlobales.isEmpty() ? null : fichierMisAJour;
    }

    private ZonedDateTime convertirEnZonedDateTime(Object date) {
        if (date == null) return null;
        if (date instanceof ZonedDateTime) return (ZonedDateTime) date;
        if (date instanceof String) return ZonedDateTime.parse((String) date);
        if (date instanceof Date) {
            LocalDateTime ldt = ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return ldt.atZone(ZoneId.systemDefault());
        }
        return null;
    }

    private List<ContratDTO> lireFichierExcel(File file) throws IOException {
        List<ContratDTO> contrats = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean first = true;
            for (Row row : sheet) {
                if (first) {
                    first = false;
                    continue;
                }
                ContratDTO dto = new ContratDTO();
                dto.setMatriculePersonne(getCellValueAsString(row.getCell(0)));
                dto.setNomPersonne(getCellValueAsString(row.getCell(1)));
                dto.setNomTypeContrat(getCellValueAsString(row.getCell(2)));
                dto.setDateDebut(getZonedDateTimeFromCell(row.getCell(3)));
                dto.setDateFin(getZonedDateTimeFromCell(row.getCell(4)));
                contrats.add(dto);
            }
        }
        return contrats;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) { // ← switch expression Java 14+
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    private ZonedDateTime getZonedDateTimeFromCell(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            LocalDateTime ldt = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return ldt.atZone(ZoneId.systemDefault());
        }
        return null;
    }

    // ── Autres méthodes ─────────────────────────────────────────────────────────

    public List<ContratDTO> findByDates(LocalDate startDate, LocalDate endDate) {
        // Conversion LocalDate → ZonedDateTime pour correspondre au type de la colonne
        ZonedDateTime start = startDate.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime end = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault());
        final Set<Long> readable = readableIds();
        return contratRepository
            .findByDateDebutBetween(start, end)
            .stream()
            .filter(c -> isReadable(readable, c.getId()))
            .map(contratMapper::toDto)
            .collect(Collectors.toList());
    }

    public boolean hasDateOverlap(Long contratId, Long personneId, ZonedDateTime dateDebut, ZonedDateTime dateFin) {
        List<Contrat> actifs = contratRepository.findByPersonneIdAndStatus(personneId, "ACTIF");
        for (Contrat c : actifs) {
            if (!Objects.equals(c.getId(), contratId)) {
                boolean overlap =
                    (dateDebut.isBefore(c.getDateFin()) || dateDebut.isEqual(c.getDateFin())) &&
                    (dateFin == null || dateFin.isAfter(c.getDateDebut()));
                if (overlap) return true;
            }
        }
        return false;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateActiveContracts() {
        log.debug("Mise à jour des contrats actifs");
        contratRepository
            .findByStatus("ACTIF")
            .forEach(contrat -> {
                if (LocalDate.now().isAfter(contrat.getDateFin().toLocalDate())) {
                    contrat.setStatus("EXPIRE");
                    contratRepository.save(contrat);
                    personneService.updatePersonStatus(contrat.getPersonne().getId());
                }
            });
    }

    public List<ContratDTO> getContratByTypeContrat(List<Long> typeContratIds) {
        final Set<Long> readable = readableIds();
        return contratRepository
            .findAllByTypeContratIdIn(typeContratIds)
            .stream()
            .filter(c -> isReadable(readable, c.getId()))
            .map(contrat -> {
                ContratDTO dto = new ContratDTO();
                dto.setId(contrat.getId());
                dto.setDateDebut(contrat.getDateDebut());
                dto.setType(contrat.getType());
                dto.setDateFin(contrat.getDateFin());
                dto.setStatus(contrat.getStatus());
                dto.setTypeContratId(contrat.getTypeContrat() != null ? contrat.getTypeContrat().getId() : null);
                dto.setPersonneId(contrat.getPersonne() != null ? contrat.getPersonne().getId() : null);
                dto.setSocieteId(contrat.getSociete() != null ? contrat.getSociete().getId() : null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<ContratDTO> filterContrats(
        LocalDate dateDebut,
        LocalDate dateFin,
        String criteria,
        Long typeContratId,
        String status,
        Long societeId
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrat> query = cb.createQuery(Contrat.class);
        Root<Contrat> root = query.from(Contrat.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dateDebut != null && criteria != null) {
            ZonedDateTime s = dateDebut.atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime e = dateDebut.atTime(23, 59, 59, 999999999).atZone(ZoneId.of("UTC"));
            switch (criteria.toLowerCase()) {
                case "before" -> predicates.add(cb.lessThan(root.get("dateDebut"), s));
                case "after" -> predicates.add(cb.greaterThan(root.get("dateDebut"), e));
                case "equal" -> predicates.add(cb.between(root.get("dateDebut"), s, e));
                default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
            }
        }
        if (dateFin != null && criteria != null) {
            ZonedDateTime s = dateFin.atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime e = dateFin.atTime(23, 59, 59, 999999999).atZone(ZoneId.of("UTC"));
            switch (criteria.toLowerCase()) {
                case "before" -> predicates.add(cb.lessThan(root.get("dateFin"), s));
                case "after" -> predicates.add(cb.greaterThan(root.get("dateFin"), e));
                case "equal" -> predicates.add(cb.between(root.get("dateFin"), s, e));
                default -> throw new IllegalArgumentException("Invalid criteria: " + criteria);
            }
        }
        if (typeContratId != null) predicates.add(cb.equal(root.get("typeContrat").get("id"), typeContratId));
        if (status != null && !status.isEmpty()) predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
        if (societeId != null) predicates.add(cb.equal(root.get("societe").get("id"), societeId));

        Set<Long> readable = readableIds();
        if (readable != null) {
            if (readable.isEmpty()) {
                return Collections.emptyList();
            }
            predicates.add(root.get("id").in(readable));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList().stream().map(contratMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ContratDTO updateEtatToPassif(Long id) {
        if (!aclUtilService.canWrite(ACL_TYPE, id)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier ce contrat");
        }
        Contrat contrat = contratRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable avec l'ID : " + id));
        contrat.setStatus("PASSIF");
        return contratMapper.toDto(contratRepository.save(contrat));
    }
}
