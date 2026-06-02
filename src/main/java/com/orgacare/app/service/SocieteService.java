package com.orgacare.app.service;

import com.orgacare.app.client.ConfigManageRestClient;
import com.orgacare.app.client.OrgacareFeignDTO;
import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.Departement;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.repository.PersonneRepository;
import com.orgacare.app.repository.SocieteRepository;
import com.orgacare.app.service.dto.SocieteDTO;
import com.orgacare.app.service.mapper.SocieteMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Societe}.
 */
@Service
@Transactional
public class SocieteService {

    private final Logger log = LoggerFactory.getLogger(SocieteService.class);

    private final SocieteRepository societeRepository;

    private final SocieteMapper societeMapper;

    private final PersonneRepository personneRepository;

    private final AffectationRepository affectationRepository;

    private final ConfigManageRestClient configManageRestClient;

    public SocieteService(
        SocieteRepository societeRepository,
        SocieteMapper societeMapper,
        PersonneRepository personneRepository,
        AffectationRepository affectationRepository,
        ConfigManageRestClient configManageRestClient
    ) {
        this.societeRepository = societeRepository;
        this.societeMapper = societeMapper;
        this.personneRepository = personneRepository;
        this.affectationRepository = affectationRepository;
        this.configManageRestClient = configManageRestClient;
    }

    /**
     * Save a societe.
     *
     * @param societeDTO the entity to save.
     * @return the persisted entity.
     */
    public SocieteDTO save(SocieteDTO societeDTO) {
        log.debug("Request to save Societe : {}", societeDTO);
        Societe societe = societeMapper.toEntity(societeDTO);
        societe = societeRepository.save(societe);
        return societeMapper.toDto(societe);
    }

    /**
     * Get all the societes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocieteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Societes");
        return societeRepository.findAll(pageable).map(societeMapper::toDto);
    }

    /**
     * Get one societe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocieteDTO> findOne(Long id) {
        log.debug("Request to get Societe : {}", id);
        return societeRepository.findById(id).map(societeMapper::toDto);
    }

    /**
     * Delete the societe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Societe : {}", id);
        societeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findSocietesByPersonneMatricule(String matricule) {
        log.debug("Request to get Societes by matricule : {}", matricule);

        Optional<Personne> personneOpt = personneRepository.findByMatriculeIgnoreCase(matricule.trim());
        if (!personneOpt.isPresent()) {
            return Collections.emptyList();
        }

        Long personneId = personneOpt.get().getId();

        List<Affectation> affectations = affectationRepository.findByPersonneId(personneId);

        if (affectations == null || affectations.isEmpty()) {
            return Collections.emptyList();
        }

        return affectations
            .stream()
            .filter(a -> a.getEtat() != Etat.CANCELED)
            .map(Affectation::getDepartement)
            .filter(Objects::nonNull)
            .map(dep -> dep.getOrganigramme() != null ? dep.getOrganigramme().getSociete() : null)
            .filter(Objects::nonNull)
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toMap(Societe::getId, s -> s, (existing, replacement) -> existing),
                    map ->
                        map
                            .values()
                            .stream()
                            .map(s -> {
                                Map<String, Object> result = new LinkedHashMap<>();
                                result.put("id", s.getId());
                                result.put("raisonSociale", s.getRaisonSociale());
                                result.put("codeSociete", s.getCodeSociete());
                                result.put("codeOrganigramme", s.getCodeOrganigramme());
                                return result;
                            })
                            .collect(Collectors.toList())
                )
            );
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAllIdAndRaisonSociale() {
        log.debug("Request to get all Societes (id + raisonSociale only)");
        return societeRepository
            .findAll()
            .stream()
            .map(s -> {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("id", s.getId());
                result.put("raisonSociale", s.getRaisonSociale());
                result.put("codeSociete", s.getCodeSociete());
                result.put("codeOrganigramme", s.getCodeOrganigramme());
                return result;
            })
            .collect(Collectors.toList());
    }

    public List<OrgacareFeignDTO> getAllOrganigrammesCodes() {
        log.debug("Request to get all Organigrammes Codes from ConfigManage");
        return configManageRestClient.getAllOrganigrammesCodes();
    }
}
