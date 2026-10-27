package com.orgacare.app.service;

import com.orgacare.app.domain.Formation;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.SessionFormation;
import com.orgacare.app.domain.SessionFormationChefProjet;
import com.orgacare.app.domain.SessionFormationPersonne;
import com.orgacare.app.domain.ThemeFormation;
import com.orgacare.app.repository.FormationRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.repository.SessionFormationChefProjetRepository;
import com.orgacare.app.repository.SessionFormationPersonneRepository;
import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.repository.ThemeFormationRepository;
import com.orgacare.app.security.SecurityUtils;
import com.orgacare.app.service.dto.FormationDTO;
import com.orgacare.app.service.mapper.FormationMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service Implementation for managing {@link Formation}.
 */
@Service
@Transactional
public class FormationService {

    private final Logger log = LoggerFactory.getLogger(FormationService.class);

    private final FormationRepository formationRepository;

    private final FormationMapper formationMapper;

    private final SessionFormationPersonneRepository sessionFormationPersonneRepository;

    private final SessionFormationChefProjetRepository sessionFormationChefProjetRepository;

    private final PersonneRepository personneRepository;

    private final SessionFormationRepository sessionFormationRepository;

    private final ThemeFormationRepository themeFormationRepository;

    public FormationService(
        FormationRepository formationRepository,
        FormationMapper formationMapper,
        SessionFormationPersonneRepository sessionFormationPersonneRepository,
        SessionFormationChefProjetRepository sessionFormationChefProjetRepository,
        PersonneRepository personneRepository,
        SessionFormationRepository sessionFormationRepository,
        ThemeFormationRepository themeFormationRepository
    ) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
        this.sessionFormationPersonneRepository = sessionFormationPersonneRepository;
        this.sessionFormationChefProjetRepository = sessionFormationChefProjetRepository;
        this.personneRepository = personneRepository;
        this.sessionFormationRepository = sessionFormationRepository;
        this.themeFormationRepository = themeFormationRepository;
    }

    /** true si l'utilisateur connecté a le rôle ADMIN ou RH. */
    private boolean isAdminOrRh() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_RH");
    }

    /** Id de la Personne dont le matricule = login de l'utilisateur connecté. */
    private Optional<Long> currentPersonneId() {
        return SecurityUtils.getCurrentUserLogin().flatMap(personneRepository::findByMatriculeIgnoreCase).map(Personne::getId);
    }

    /**
     * Save a formation.
     *
     * @param formationDTO the entity to save.
     * @return the persisted entity.
     */
    public FormationDTO save(FormationDTO formationDTO) {
        log.debug("Request to save Formation : {}", formationDTO);
        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

    /**
     * Partially update a formation.
     *
     * @param formationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormationDTO> partialUpdate(FormationDTO formationDTO) {
        log.debug("Request to partially update Formation : {}", formationDTO);

        return formationRepository
            .findById(formationDTO.getId())
            .map(existingFormation -> {
                formationMapper.partialUpdate(existingFormation, formationDTO);

                return existingFormation;
            })
            .map(formationRepository::save)
            .map(formationMapper::toDto);
    }

    /**
     * Get all the formations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formations");
        return formationRepository.findAll(pageable).map(formationMapper::toDto);
    }

    /**
     * Recherche paginée avec filtres optionnels.
     *
     * @param keyword    texte cherché dans le titre de la session et dans le nom / matricule du bénéficiaire.
     * @param personneId bénéficiaire exact.
     * @param formateurId formateur de la session.
     */
    @Transactional(readOnly = true)
    public Page<FormationDTO> search(Pageable pageable, String keyword, Long personneId, Long formateurId) {
        log.debug("Request to search Formations : keyword={}, personneId={}, formateurId={}", keyword, personneId, formateurId);

        log.debug(
            "Authorities vues par le microservice : {}",
            SecurityContextHolder.getContext().getAuthentication() == null
                ? "aucune authentification"
                : SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        );

        // Un utilisateur sans rôle ADMIN/RH ne voit que ses propres formations
        Long effectivePersonneId = personneId;
        if (!isAdminOrRh()) {
            Optional<Long> own = currentPersonneId();
            if (!own.isPresent()) {
                return Page.empty(pageable);
            }
            effectivePersonneId = own.get();
        }
        final Long filterPersonneId = effectivePersonneId;

        Specification<Formation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterPersonneId != null) {
                predicates.add(cb.equal(root.get("personneId"), filterPersonneId));
            }

            if (formateurId != null) {
                Subquery<Long> sessionsOfFormateur = query.subquery(Long.class);
                Root<SessionFormation> s = sessionsOfFormateur.from(SessionFormation.class);
                sessionsOfFormateur.select(s.<Long>get("id")).where(cb.equal(s.get("formateurId"), formateurId));
                predicates.add(root.get("sessionId").in(sessionsOfFormateur));
            }

            if (StringUtils.hasText(keyword)) {
                String like = "%" + keyword.trim().toLowerCase() + "%";

                Subquery<Long> sessionsByTitle = query.subquery(Long.class);
                Root<SessionFormation> s = sessionsByTitle.from(SessionFormation.class);
                sessionsByTitle.select(s.<Long>get("id")).where(cb.like(cb.lower(s.get("title")), like));

                Subquery<Long> personnesByName = query.subquery(Long.class);
                Root<Personne> p = personnesByName.from(Personne.class);
                personnesByName
                    .select(p.<Long>get("id"))
                    .where(cb.or(cb.like(cb.lower(p.get("nomPrenom")), like), cb.like(cb.lower(p.get("matricule")), like)));

                predicates.add(cb.or(root.get("sessionId").in(sessionsByTitle), root.get("personneId").in(personnesByName)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return formationRepository.findAll(spec, pageable).map(formationMapper::toDto);
    }

    /** Ids des bénéficiaires ayant au moins une formation. */
    @Transactional(readOnly = true)
    public List<Long> findBeneficiaireIds() {
        if (!isAdminOrRh()) {
            return currentPersonneId().map(Collections::singletonList).orElse(Collections.emptyList());
        }
        return formationRepository.findDistinctPersonneIds();
    }

    /**
     * Get one formation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormationDTO> findOne(Long id) {
        log.debug("Request to get Formation : {}", id);
        Optional<Formation> formation = formationRepository.findById(id);
        if (!isAdminOrRh()) {
            Optional<Long> own = currentPersonneId();
            formation = formation.filter(f -> own.isPresent() && own.get().equals(f.getPersonneId()));
        }
        return formation.map(formationMapper::toDto);
    }

    /**
     * Delete the formation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Formation : {}", id);
        formationRepository.deleteById(id);
    }

    /**
     * Synchronise les formations d'une session avec ses participants + chefs de projet.
     * N'agit que si la session est au statut "Exécuté".
     * - crée une formation par personne qui n'en a pas encore ;
     * - supprime celles des personnes retirées, si elles n'ont ni note ni certificat.
     */
    public void syncForSession(Long sessionId) {
        log.debug("Request to sync Formations of session {}", sessionId);

        // Les formations ne sont générées que pour une session au statut "Exécuté"
        Optional<SessionFormation> sessionOpt = sessionFormationRepository.findById(sessionId);
        boolean executee = sessionOpt.map(s -> SessionFormationService.STATUT_EXECUTE.equals(s.getStatut())).orElse(false);
        if (!executee) {
            log.debug("Session {} non exécutée : aucune formation générée", sessionId);
            return;
        }
        SessionFormation session = sessionOpt.get();

        // Dates de certificat : obtention = fin d'exécution de la session, expiration = obtention + durée de validité du thème (en mois)
        final ZonedDateTime dateObtention = session.getDateExecutionAu();
        final ZonedDateTime dateExpiration = computeDateExpiration(session, dateObtention);

        Set<Long> expected = new LinkedHashSet<>();
        sessionFormationPersonneRepository
            .findBySessionId(sessionId)
            .stream()
            .map(SessionFormationPersonne::getPersonneId)
            .filter(Objects::nonNull)
            .forEach(expected::add);
        sessionFormationChefProjetRepository
            .findBySessionId(sessionId)
            .stream()
            .map(SessionFormationChefProjet::getChefProjetId)
            .filter(Objects::nonNull)
            .forEach(expected::add);

        List<Formation> existing = formationRepository.findBySessionId(sessionId);
        Set<Long> existingPersonneIds = existing
            .stream()
            .map(Formation::getPersonneId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // Création des lignes manquantes
        List<Formation> toCreate = expected
            .stream()
            .filter(pid -> !existingPersonneIds.contains(pid))
            .map(pid ->
                new Formation()
                    .sessionId(sessionId)
                    .personneId(pid)
                    .statut("Exécuté")
                    .notificationEnvoyee(false)
                    .dateObtentionCertificat(dateObtention)
                    .dateExpirationCertificat(dateExpiration)
            )
            .collect(Collectors.toList());
        formationRepository.saveAll(toCreate);

        // Suppression des lignes obsolètes (non commencées)
        List<Formation> toDelete = existing
            .stream()
            .filter(f -> f.getPersonneId() != null && !expected.contains(f.getPersonneId()))
            .filter(f -> f.getNote() == null && f.getDateObtentionCertificat() == null)
            .collect(Collectors.toList());
        formationRepository.deleteAll(toDelete);
    }

    /**
     * Date d'expiration = date d'obtention + duree_validite_certificat (mois) du thème de la session.
     * Retourne null si la date d'obtention, le thème ou la durée est absent(e) ou nul(le).
     */
    private ZonedDateTime computeDateExpiration(SessionFormation session, ZonedDateTime dateObtention) {
        if (dateObtention == null || session.getThemeId() == null) {
            return null;
        }
        Integer mois = themeFormationRepository
            .findById(session.getThemeId())
            .map(ThemeFormation::getDuree_validite_certificat)
            .orElse(null);
        if (mois == null || mois <= 0) {
            return null;
        }
        return dateObtention.plusMonths(mois);
    }
}
