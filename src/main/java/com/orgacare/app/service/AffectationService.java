package com.orgacare.app.service;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.Departement;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.repository.DepartementRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.service.dto.AffectationDTO;
import com.orgacare.app.service.dto.PersonneDTO;
import com.orgacare.app.service.mapper.AffectationMapper;
import com.orgacare.app.web.rest.errors.BadRequestAlertException;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Affectation}.
 */
@Service
@Transactional
public class AffectationService {

    private final Logger log = LoggerFactory.getLogger(AffectationService.class);
    private final DepartementRepository departementRepository;
    private final AffectationRepository affectationRepository;

    private final AffectationMapper affectationMapper;
    private final PersonneService personneService;
    private final DepartementService departementService;
    private final PersonneRepository personneRepository;

    public AffectationService(
        AffectationRepository affectationRepository,
        AffectationMapper affectationMapper,
        DepartementRepository departementRepository,
        DepartementService departementService,
        PersonneService personneService,
        PersonneRepository personneRepository
    ) {
        this.affectationRepository = affectationRepository;
        this.affectationMapper = affectationMapper;
        this.departementRepository = departementRepository;
        this.departementService = departementService;
        this.personneService = personneService;
        this.personneRepository = personneRepository;
    }

    /**
     * Save a affectation.
     *
     * @param affectationDTO the entity to save.
     * @return the persisted entity.
     */
    public AffectationDTO save(AffectationDTO affectationDTO) {
        log.debug("Request to save Affectation : {}", affectationDTO);
        Affectation affectation = affectationMapper.toEntity(affectationDTO);
        affectation = affectationRepository.save(affectation);
        return affectationMapper.toDto(affectation);
    }

    /**
     * Get all the affectations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AffectationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Affectations");
        return affectationRepository.findAll(pageable).map(affectationMapper::toDto);
    }

    /**
     * Get one affectation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AffectationDTO> findOne(Long id) {
        log.debug("Request to get Affectation : {}", id);
        return affectationRepository.findById(id).map(affectationMapper::toDto);
    }

    /**
     * Delete the affectation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Affectation : {}", id);
        affectationRepository.deleteById(id);
    }

    public List<AffectationDTO> findByDepartementId(Long departementId) {
        List<Affectation> affectations = affectationRepository.findByDepartementId(departementId);
        return affectations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AffectationDTO> findByPersonneId(Long personneId) {
        List<Affectation> affectations = affectationRepository.findByPersonneId(personneId);
        return affectations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private Personne getPersonneByAffectationId(Long affectationId) {
        return personneRepository.findByAffectationId(affectationId).orElse(null);
    }

    private AffectationDTO convertToDTO(Affectation affectation) {
        AffectationDTO dto = new AffectationDTO();
        dto.setId(affectation.getId());
        dto.setType(affectation.getType());
        dto.setDateCreation(affectation.getDateCreation());
        dto.setDateAction(affectation.getDateAction());
        dto.setDateFin(affectation.getDateFin());
        dto.setEtat(affectation.getEtat());
        Personne personne = getPersonneByAffectationId(affectation.getId());
        dto.setPersonneId(personne != null ? personne.getId() : null);
        dto.setDepartementId(affectation.getDepartement() != null ? affectation.getDepartement().getId() : null);
        dto.setGroupeId(affectation.getGroupe() != null ? affectation.getGroupe().getId() : null);
        dto.setSocieteId(affectation.getSociete() != null ? affectation.getSociete().getId() : null);

        // Utiliser l'ID du département pour obtenir la hiérarchie
        List<Map<String, Object>> hierarchy = affectation.getDepartement() != null
            ? getDepartmentHierarchy(affectation.getDepartement().getId())
            : new ArrayList<>();
        dto.setTotalHierarchy(hierarchy);

        return dto;
    }

    public List<Map<String, Object>> getDepartmentHierarchy(Long departementId) {
        Departement departement = departementRepository
            .findById(departementId)
            .orElseThrow(() -> new BadRequestAlertException("Departement not found", "departement", "notfound"));
        return calculateDepartmentHierarchy(departement);
    }

    private List<Map<String, Object>> calculateDepartmentHierarchy(Departement departement) {
        List<Map<String, Object>> hierarchy = new ArrayList<>();
        Departement currentDepartment = departement;

        while (currentDepartment != null) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("departementId", currentDepartment.getId());
            dto.put("nom", currentDepartment.getNom());
            dto.put(
                "departementParentId",
                currentDepartment.getDepartementParent() != null ? currentDepartment.getDepartementParent().getId() : null
            );

            hierarchy.add(dto);

            currentDepartment = currentDepartment.getDepartementParent();
        }

        return hierarchy;
    }

    @Transactional(readOnly = true)
    public Optional<List<AffectationDTO>> findAffectationPersonneByUserId(Long userId) {
        log.debug("Recherche des affectations de la personne liée à l'userId {}", userId);
        try {
            Optional<PersonneDTO> personneDtoOpt = personneService.findByUserId(userId);

            // Si aucun DTO retourné -> renvoyer liste vide (pas d'erreur)
            if (!personneDtoOpt.isPresent()) {
                log.debug("Aucune personne trouvée pour userId {}", userId);
                return Optional.of(Collections.emptyList());
            }

            PersonneDTO personneDTO = personneDtoOpt.get();
            Long personneId = personneDTO.getId();

            // Si l'id est null mais que le matricule existe, tenter de récupérer l'entité par matricule
            if (personneId == null && personneDTO.getMatricule() != null && !personneDTO.getMatricule().trim().isEmpty()) {
                log.debug(
                    "Personne DTO sans id mais avec matricule ({}). Recherche par matricule pour récupérer l'id.",
                    personneDTO.getMatricule()
                );
                Optional<PersonneDTO> byMatricule = personneService.findByMatricule(personneDTO.getMatricule().trim());
                if (byMatricule.isPresent()) {
                    personneId = byMatricule.get().getId();
                    log.debug("ID retrouvé par matricule : {}", personneId);
                }
            }

            // Si toujours pas d'id -> renvoyer liste vide (pas de correspondance exploitable)
            if (personneId == null) {
                log.debug("Personne trouvée pour userId {} mais aucun id disponible (ni via matricule).", userId);
                return Optional.of(Collections.emptyList());
            }

            // Récupère les affectations par personneId (réutilise la méthode existante)
            List<AffectationDTO> affectations = this.findByPersonneId(personneId);
            if (affectations == null) {
                affectations = Collections.emptyList();
            }
            return Optional.of(affectations);
        } catch (Exception ex) {
            // Si une exception survient on loggue et on renvoie une liste vide pour éviter NPE côté appelant.
            log.error("Erreur inattendue lors de findAffectationPersonneByUserId {} : {}", userId, ex.getMessage(), ex);
            return Optional.of(Collections.emptyList());
        }
    }

    @Transactional(readOnly = true)
    public List<String> findEmailsOfPersonnesByDepartementIdAndType(Long departementId, TypeAffectation type) {
        return affectationRepository
            .findByDepartementIdAndType(departementId, type)
            .stream()
            .filter(a -> a.getEtat() != Etat.CANCELED)
            .map(a -> personneRepository.findByAffectationId(a.getId()).orElse(null))
            .filter(Objects::nonNull)
            .map(Personne::getEmail)
            .filter(email -> email != null && !email.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList());
    }
}
