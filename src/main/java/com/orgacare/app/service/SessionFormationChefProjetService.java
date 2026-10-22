package com.orgacare.app.service;

import com.orgacare.app.domain.SessionFormationChefProjet;
import com.orgacare.app.repository.SessionFormationChefProjetRepository;
import com.orgacare.app.service.dto.SessionFormationChefProjetDTO;
import com.orgacare.app.service.mapper.SessionFormationChefProjetMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionFormationChefProjet}.
 */
@Service
@Transactional
public class SessionFormationChefProjetService {

    private final Logger log = LoggerFactory.getLogger(SessionFormationChefProjetService.class);

    private final SessionFormationChefProjetRepository sessionFormationChefProjetRepository;

    private final SessionFormationChefProjetMapper sessionFormationChefProjetMapper;

    public SessionFormationChefProjetService(
        SessionFormationChefProjetRepository sessionFormationChefProjetRepository,
        SessionFormationChefProjetMapper sessionFormationChefProjetMapper
    ) {
        this.sessionFormationChefProjetRepository = sessionFormationChefProjetRepository;
        this.sessionFormationChefProjetMapper = sessionFormationChefProjetMapper;
    }

    /**
     * Save a sessionFormationChefProjet.
     *
     * @param sessionFormationChefProjetDTO the entity to save.
     * @return the persisted entity.
     */
    public SessionFormationChefProjetDTO save(SessionFormationChefProjetDTO sessionFormationChefProjetDTO) {
        log.debug("Request to save SessionFormationChefProjet : {}", sessionFormationChefProjetDTO);
        SessionFormationChefProjet sessionFormationChefProjet = sessionFormationChefProjetMapper.toEntity(sessionFormationChefProjetDTO);
        sessionFormationChefProjet = sessionFormationChefProjetRepository.save(sessionFormationChefProjet);
        return sessionFormationChefProjetMapper.toDto(sessionFormationChefProjet);
    }

    /**
     * Partially update a sessionFormationChefProjet.
     *
     * @param sessionFormationChefProjetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SessionFormationChefProjetDTO> partialUpdate(SessionFormationChefProjetDTO sessionFormationChefProjetDTO) {
        log.debug("Request to partially update SessionFormationChefProjet : {}", sessionFormationChefProjetDTO);

        return sessionFormationChefProjetRepository
            .findById(sessionFormationChefProjetDTO.getId())
            .map(existingSessionFormationChefProjet -> {
                sessionFormationChefProjetMapper.partialUpdate(existingSessionFormationChefProjet, sessionFormationChefProjetDTO);

                return existingSessionFormationChefProjet;
            })
            .map(sessionFormationChefProjetRepository::save)
            .map(sessionFormationChefProjetMapper::toDto);
    }

    /**
     * Get all the sessionFormationChefProjets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SessionFormationChefProjetDTO> findAll() {
        log.debug("Request to get all SessionFormationChefProjets");
        return sessionFormationChefProjetRepository
            .findAll()
            .stream()
            .map(sessionFormationChefProjetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sessionFormationChefProjet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SessionFormationChefProjetDTO> findOne(Long id) {
        log.debug("Request to get SessionFormationChefProjet : {}", id);
        return sessionFormationChefProjetRepository.findById(id).map(sessionFormationChefProjetMapper::toDto);
    }

    /**
     * Delete the sessionFormationChefProjet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SessionFormationChefProjet : {}", id);
        sessionFormationChefProjetRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Long> findChefIdsBySession(Long sessionId) {
        return sessionFormationChefProjetRepository
            .findBySessionId(sessionId)
            .stream()
            .map(SessionFormationChefProjet::getChefProjetId)
            .collect(Collectors.toList());
    }

    public List<Long> replaceForSession(Long sessionId, List<Long> chefIds) {
        log.debug("Request to replace chefs projet of session {} : {}", sessionId, chefIds);
        Set<Long> unique = new LinkedHashSet<>();
        if (chefIds != null) {
            chefIds.stream().filter(Objects::nonNull).forEach(unique::add);
        }
        sessionFormationChefProjetRepository.deleteBySessionId(sessionId);
        sessionFormationChefProjetRepository.flush();

        List<SessionFormationChefProjet> toSave = unique
            .stream()
            .map(cid -> new SessionFormationChefProjet().sessionId(sessionId).chefProjetId(cid))
            .collect(Collectors.toList());
        sessionFormationChefProjetRepository.saveAll(toSave);
        return new LinkedList<>(unique);
    }
}
