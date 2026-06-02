package com.orgacare.app.service;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.orgacare.app.domain.Absence;
import com.orgacare.app.repository.AbsenceRepository;
import com.orgacare.app.service.dto.AbsenceDTO;
import com.orgacare.app.service.mapper.AbsenceMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Absence}.
 */
@Service
@Transactional
public class AbsenceService {

    private final Logger log = LoggerFactory.getLogger(AbsenceService.class);

    private final AbsenceRepository absenceRepository;

    private final AbsenceMapper absenceMapper;

    public AbsenceService(AbsenceRepository absenceRepository, AbsenceMapper absenceMapper) {
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
    }

    /**
     * Save a absence.
     *
     * @param absenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AbsenceDTO save(AbsenceDTO absenceDTO) {
        if (hasDateOverlap(absenceDTO.getId(), absenceDTO.getPersonneAbscentId(), absenceDTO.getDateDebut(), absenceDTO.getDateFin())) {
            throw new BadRequestAlertException("Le contrat chevauche un autre contrat actif.", ENTITY_NAME, "dateoverlap");
        }
        log.debug("Request to save Absence : {}", absenceDTO);
        Absence absence = absenceMapper.toEntity(absenceDTO);
        absence = absenceRepository.save(absence);
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
        return absenceRepository.findAll(pageable).map(absenceMapper::toDto);
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
        return absenceRepository.findById(id).map(absenceMapper::toDto);
    }

    /**
     * Delete the absence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Absence : {}", id);
        absenceRepository.deleteById(id);
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
                    (dateDebut.isBefore(absence.getDateFin()) || dateDebut.isEqual(absence.getDateFin())) &&
                    (dateFin == null || dateFin.isAfter(absence.getDateDebut()));

                if (overlap) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<AbsenceDTO> findAbsencesByPersonneId(Long personneId) {
        List<Absence> absences = absenceRepository.findByPersonneAbscentId(personneId);
        return absences
            .stream()
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

    public List<AbsenceDTO> findBypersonneId(Long personneAbscentId) {
        return absenceRepository
            .findByPersonneAbscentId(personneAbscentId)
            .stream()
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
