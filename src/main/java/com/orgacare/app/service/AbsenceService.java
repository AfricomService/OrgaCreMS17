package com.orgacare.app.service;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.orgacare.app.domain.Absence;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.enumeration.AclPermission;
import com.orgacare.app.repository.AbsenceRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.service.dto.AbsenceDTO;
import com.orgacare.app.service.mapper.AbsenceMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Absence}.
 */
@Service
@Transactional
public class AbsenceService {

    private final Logger log = LoggerFactory.getLogger(AbsenceService.class);

    public static final String ACL_TYPE = "ABSENCE";

    private final AbsenceRepository absenceRepository;

    private final AbsenceMapper absenceMapper;

    private final AclUtilService aclUtilService;

    private final PersonneRepository personneRepository;

    public AbsenceService(
        AbsenceRepository absenceRepository,
        AbsenceMapper absenceMapper,
        AclUtilService aclUtilService,
        PersonneRepository personneRepository
    ) {
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
        this.aclUtilService = aclUtilService;
        this.personneRepository = personneRepository;
    }

    /**
     * Save a absence.
     *
     * @param absenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsenceDTO save(AbsenceDTO absenceDTO) {
        boolean isNew = absenceDTO.getId() == null;

        // Modification : il faut le droit WRITE
        if (!isNew && !aclUtilService.canWrite(ACL_TYPE, absenceDTO.getId())) {
            throw new AccessDeniedException("Vous n'avez pas le droit de modifier cette absence");
        }

        if (hasDateOverlap(absenceDTO.getId(), absenceDTO.getPersonneAbscentId(), absenceDTO.getDateDebut(), absenceDTO.getDateFin())) {
            throw new BadRequestAlertException("L'absence chevauche une autre absence.", ENTITY_NAME, "dateoverlap");
        }
        log.debug("Request to save Absence : {}", absenceDTO);

        // Création : on vérifie AVANT d'enregistrer que le créateur a un matricule
        if (isNew && !aclUtilService.isAdmin() && aclUtilService.getCurrentMatricule().isEmpty()) {
            throw new BadRequestAlertException(
                "Votre compte n'est lié à aucune personne (matricule manquant).",
                ENTITY_NAME,
                "nomatricule"
            );
        }

        Absence absence = absenceMapper.toEntity(absenceDTO);
        absence = absenceRepository.save(absence);
        final Long absenceId = absence.getId();

        if (isNew && aclUtilService.getCurrentMatricule().isPresent()) {
            // 1) le créateur : READ + WRITE (SID = son matricule)
            aclUtilService.grantOwnerToCurrentUser(ACL_TYPE, absenceId);
        }

        // 2) la personne absente : READ (SID = son matricule), idempotent
        if (absenceDTO.getPersonneAbscentId() != null) {
            personneRepository
                .findById(absenceDTO.getPersonneAbscentId())
                .map(Personne::getMatricule)
                .filter(m -> !m.isBlank())
                .ifPresent(m -> aclUtilService.grantReadToMatricule(ACL_TYPE, absenceId, m));
        }
        return absenceMapper.toDto(absence);
    }

    /**
     * Get all the absences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AbsenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Absences");
        if (aclUtilService.isAdmin()) {
            return absenceRepository.findAll(pageable).map(absenceMapper::toDto);
        }
        Set<Long> allowedIds = aclUtilService.getAccessibleObjectIds(ACL_TYPE, AclPermission.READ);
        if (allowedIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return absenceRepository.findByIdIn(allowedIds, pageable).map(absenceMapper::toDto);
    }

    /**
     * Get one absence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbsenceDTO> findOne(Long id) {
        log.debug("Request to get Absence : {}", id);
        if (!aclUtilService.canRead(ACL_TYPE, id)) {
            throw new AccessDeniedException("Accès refusé à cette absence");
        }
        return absenceRepository.findById(id).map(absenceMapper::toDto);
    }

    /**
     * Delete the absence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Absence : {}", id);
        if (!aclUtilService.canWrite(ACL_TYPE, id)) {
            throw new AccessDeniedException("Vous n'avez pas le droit de supprimer cette absence");
        }
        absenceRepository.deleteById(id);
        aclUtilService.deleteAcl(ACL_TYPE, id);
    }

    public List<Absence> findBypersonneAbscentId(Long personneAbscentId) {
        return absenceRepository.findByPersonneAbscentId(personneAbscentId);
    }

    public boolean hasDateOverlap(Long AbsenceId, Long personneAbscentId, ZonedDateTime dateDebut, ZonedDateTime dateFin) {
        List<Absence> absencesActifs = findBypersonneAbscentId(personneAbscentId);
        for (Absence absence : absencesActifs) {
            if (!Objects.equals(absence.getId(), AbsenceId)) {
                log.debug("CONTRAT {} - {}", absence.getDateDebut(), absence.getDateFin());

                boolean overlap =
                    (absence.getDateFin() == null || !dateDebut.isAfter(absence.getDateFin())) &&
                    (dateFin == null || dateFin.isAfter(absence.getDateDebut()));

                if (overlap) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<AbsenceDTO> findAbsencesByPersonneId(Long personneId) {
        final boolean admin = aclUtilService.isAdmin();
        Set<Long> allowedIds = admin ? Collections.emptySet() : aclUtilService.getAccessibleObjectIds(ACL_TYPE, AclPermission.READ);
        List<Absence> absences = absenceRepository.findByPersonneAbscentId(personneId);
        return absences
            .stream()
            .filter(a -> admin || allowedIds.contains(a.getId()))
            .map(absence -> {
                AbsenceDTO dto = new AbsenceDTO();
                dto.setId(absence.getId());
                dto.setDateDebut(absence.getDateDebut());
                dto.setMotif(absence.getMotif());
                dto.setDateFin(absence.getDateFin());
                dto.setEtat(absence.getEtat());
                dto.setPersonneAbscentId(absence.getPersonneAbscent() != null ? absence.getPersonneAbscent().getId() : null);
                dto.setPersonneRemplacantId(absence.getPersonneRemplacant() != null ? absence.getPersonneRemplacant().getId() : null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AbsenceDTO> findBypersonneId(Long personneAbscentId) {
        final boolean admin = aclUtilService.isAdmin();
        Set<Long> allowedIds = admin ? Collections.emptySet() : aclUtilService.getAccessibleObjectIds(ACL_TYPE, AclPermission.READ);
        return absenceRepository
            .findByPersonneAbscentId(personneAbscentId)
            .stream()
            .filter(a -> admin || allowedIds.contains(a.getId()))
            .map(absence -> {
                AbsenceDTO dto = new AbsenceDTO();
                dto.setId(absence.getId());
                dto.setDateDebut(absence.getDateDebut());
                dto.setMotif(absence.getMotif());
                dto.setDateFin(absence.getDateFin());
                dto.setEtat(absence.getEtat());
                dto.setPersonneAbscentId(absence.getPersonneAbscent() != null ? absence.getPersonneAbscent().getId() : null);
                dto.setPersonneRemplacantId(absence.getPersonneRemplacant() != null ? absence.getPersonneRemplacant().getId() : null);

                return dto;
            })
            .collect(Collectors.toList());
    }
}
