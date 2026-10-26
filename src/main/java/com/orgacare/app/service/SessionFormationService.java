package com.orgacare.app.service;

import com.orgacare.app.domain.SessionFormation;
import com.orgacare.app.repository.FormationRepository;
import com.orgacare.app.repository.PieceJointeRepository;
import com.orgacare.app.repository.SessionFormationChefProjetRepository;
import com.orgacare.app.repository.SessionFormationPersonneRepository;
import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.service.dto.SessionFormationDTO;
import com.orgacare.app.service.mapper.SessionFormationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing {@link SessionFormation}.
 */
@Service
@Transactional
public class SessionFormationService {

    private final Logger log = LoggerFactory.getLogger(SessionFormationService.class);

    private final SessionFormationRepository sessionFormationRepository;

    private final SessionFormationMapper sessionFormationMapper;

    private final SessionFormationPersonneRepository sessionFormationPersonneRepository;

    private final SessionFormationChefProjetRepository sessionFormationChefProjetRepository;

    public static final String STATUT_PLANIFIE = "Planifié";
    public static final String STATUT_EXECUTE = "Exécuté";

    private final FormationRepository formationRepository;

    private final PieceJointeRepository pieceJointeRepository;

    public SessionFormationService(
        SessionFormationRepository sessionFormationRepository,
        SessionFormationMapper sessionFormationMapper,
        SessionFormationPersonneRepository sessionFormationPersonneRepository,
        SessionFormationChefProjetRepository sessionFormationChefProjetRepository,
        FormationRepository formationRepository,
        PieceJointeRepository pieceJointeRepository
    ) {
        this.sessionFormationRepository = sessionFormationRepository;
        this.sessionFormationMapper = sessionFormationMapper;
        this.sessionFormationPersonneRepository = sessionFormationPersonneRepository;
        this.sessionFormationChefProjetRepository = sessionFormationChefProjetRepository;
        this.formationRepository = formationRepository;
        this.pieceJointeRepository = pieceJointeRepository;
    }

    /**
     * Save a sessionFormation.
     *
     * @param sessionFormationDTO the entity to save.
     * @return the persisted entity.
     */
    public SessionFormationDTO save(SessionFormationDTO sessionFormationDTO) {
        log.debug("Request to save SessionFormation : {}", sessionFormationDTO);
        SessionFormation sessionFormation = sessionFormationMapper.toEntity(sessionFormationDTO);
        sessionFormation = sessionFormationRepository.save(sessionFormation);
        return sessionFormationMapper.toDto(sessionFormation);
    }

    /**
     * Création : le statut est toujours "Planifié" et la date de création est la date actuelle.
     */
    public SessionFormationDTO create(SessionFormationDTO sessionFormationDTO) {
        sessionFormationDTO.setStatut(STATUT_PLANIFIE);
        sessionFormationDTO.setDateCreation(ZonedDateTime.now());
        return save(sessionFormationDTO);
    }

    /**
     * Mise à jour : le statut et la date de création existants sont conservés
     * (le statut ne change que via executer()).
     */
    public SessionFormationDTO update(SessionFormationDTO sessionFormationDTO) {
        sessionFormationRepository
            .findById(sessionFormationDTO.getId())
            .ifPresent(existing -> {
                sessionFormationDTO.setStatut(existing.getStatut());
                sessionFormationDTO.setDateCreation(existing.getDateCreation());
            });
        return save(sessionFormationDTO);
    }

    /**
     * Passe la session au statut "Exécuté".
     */
    public Optional<SessionFormationDTO> executer(Long id) {
        log.debug("Request to execute SessionFormation : {}", id);
        return sessionFormationRepository
            .findById(id)
            .map(session -> {
                session.setStatut(STATUT_EXECUTE);
                return session;
            })
            .map(sessionFormationRepository::save)
            .map(sessionFormationMapper::toDto);
    }

    /**
     * Partially update a sessionFormation.
     *
     * @param sessionFormationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SessionFormationDTO> partialUpdate(SessionFormationDTO sessionFormationDTO) {
        log.debug("Request to partially update SessionFormation : {}", sessionFormationDTO);

        return sessionFormationRepository
            .findById(sessionFormationDTO.getId())
            .map(existingSessionFormation -> {
                sessionFormationMapper.partialUpdate(existingSessionFormation, sessionFormationDTO);

                return existingSessionFormation;
            })
            .map(sessionFormationRepository::save)
            .map(sessionFormationMapper::toDto);
    }

    /**
     * Get all the sessionFormations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SessionFormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SessionFormations");
        return sessionFormationRepository.findAll(pageable).map(sessionFormationMapper::toDto);
    }

    /**
     * Recherche paginée avec filtres optionnels.
     *
     * @param keyword     texte cherché dans le titre.
     * @param formateurId formateur exact.
     * @param statut      statut exact (Exécuter / Planifier).
     */
    @Transactional(readOnly = true)
    public Page<SessionFormationDTO> search(
        Pageable pageable,
        String keyword,
        Long formateurId,
        String statut,
        LocalDate datePlanificationDebut,
        LocalDate datePlanificationFin,
        LocalDate dateExecutionDebut,
        LocalDate dateExecutionFin
    ) {
        log.debug(
            "Request to search SessionFormations : keyword={}, formateurId={}, statut={}, planif=[{}..{}], exec=[{}..{}]",
            keyword,
            formateurId,
            statut,
            datePlanificationDebut,
            datePlanificationFin,
            dateExecutionDebut,
            dateExecutionFin
        );

        // Bornes inclusives : début de journée pour "du", fin de journée pour "au"
        final ZonedDateTime planifDebut = startOfDay(datePlanificationDebut);
        final ZonedDateTime planifFin = endOfDay(datePlanificationFin);
        final ZonedDateTime execDebut = startOfDay(dateExecutionDebut);
        final ZonedDateTime execFin = endOfDay(dateExecutionFin);

        Specification<SessionFormation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + keyword.trim().toLowerCase() + "%"));
            }
            if (formateurId != null) {
                predicates.add(cb.equal(root.get("formateurId"), formateurId));
            }
            if (StringUtils.hasText(statut)) {
                predicates.add(cb.equal(root.get("statut"), statut.trim()));
            }
            if (planifDebut != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<ZonedDateTime>get("datePlanificationDu"), planifDebut));
            }
            if (planifFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.<ZonedDateTime>get("datePlanificationDu"), planifFin));
            }
            if (execDebut != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<ZonedDateTime>get("dateExecutionDu"), execDebut));
            }
            if (execFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.<ZonedDateTime>get("dateExecutionDu"), execFin));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return sessionFormationRepository.findAll(spec, pageable).map(sessionFormationMapper::toDto);
    }

    private static ZonedDateTime startOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay(ZoneId.systemDefault());
    }

    private static ZonedDateTime endOfDay(LocalDate date) {
        return date == null ? null : date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);
    }

    /**
     * Get one sessionFormation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SessionFormationDTO> findOne(Long id) {
        log.debug("Request to get SessionFormation : {}", id);
        return sessionFormationRepository.findById(id).map(sessionFormationMapper::toDto);
    }

    /**
     * Delete the sessionFormation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SessionFormation : {}", id);

        // Nettoyage des données liées à la session
        formationRepository.deleteAll(formationRepository.findBySessionId(id));
        pieceJointeRepository.deleteAll(pieceJointeRepository.findAllBySessionId(id));
        sessionFormationPersonneRepository.deleteBySessionId(id);
        sessionFormationChefProjetRepository.deleteBySessionId(id);

        sessionFormationRepository.deleteById(id);
    }
}
