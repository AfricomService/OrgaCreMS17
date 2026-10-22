package com.orgacare.app.service;

import com.orgacare.app.domain.SessionFormationPersonne;
import com.orgacare.app.repository.SessionFormationPersonneRepository;
import com.orgacare.app.service.dto.SessionFormationPersonneDTO;
import com.orgacare.app.service.mapper.SessionFormationPersonneMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionFormationPersonne}.
 */
@Service
@Transactional
public class SessionFormationPersonneService {

    private final Logger log = LoggerFactory.getLogger(SessionFormationPersonneService.class);

    private final SessionFormationPersonneRepository sessionFormationPersonneRepository;

    private final SessionFormationPersonneMapper sessionFormationPersonneMapper;

    public SessionFormationPersonneService(
        SessionFormationPersonneRepository sessionFormationPersonneRepository,
        SessionFormationPersonneMapper sessionFormationPersonneMapper
    ) {
        this.sessionFormationPersonneRepository = sessionFormationPersonneRepository;
        this.sessionFormationPersonneMapper = sessionFormationPersonneMapper;
    }

    /**
     * Save a sessionFormationPersonne.
     *
     * @param sessionFormationPersonneDTO the entity to save.
     * @return the persisted entity.
     */
    public SessionFormationPersonneDTO save(SessionFormationPersonneDTO sessionFormationPersonneDTO) {
        log.debug("Request to save SessionFormationPersonne : {}", sessionFormationPersonneDTO);
        SessionFormationPersonne sessionFormationPersonne = sessionFormationPersonneMapper.toEntity(sessionFormationPersonneDTO);
        sessionFormationPersonne = sessionFormationPersonneRepository.save(sessionFormationPersonne);
        return sessionFormationPersonneMapper.toDto(sessionFormationPersonne);
    }

    /**
     * Partially update a sessionFormationPersonne.
     *
     * @param sessionFormationPersonneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SessionFormationPersonneDTO> partialUpdate(SessionFormationPersonneDTO sessionFormationPersonneDTO) {
        log.debug("Request to partially update SessionFormationPersonne : {}", sessionFormationPersonneDTO);

        return sessionFormationPersonneRepository
            .findById(sessionFormationPersonneDTO.getId())
            .map(existingSessionFormationPersonne -> {
                sessionFormationPersonneMapper.partialUpdate(existingSessionFormationPersonne, sessionFormationPersonneDTO);

                return existingSessionFormationPersonne;
            })
            .map(sessionFormationPersonneRepository::save)
            .map(sessionFormationPersonneMapper::toDto);
    }

    /**
     * Get all the sessionFormationPersonnes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SessionFormationPersonneDTO> findAll() {
        log.debug("Request to get all SessionFormationPersonnes");
        return sessionFormationPersonneRepository
            .findAll()
            .stream()
            .map(sessionFormationPersonneMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sessionFormationPersonne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SessionFormationPersonneDTO> findOne(Long id) {
        log.debug("Request to get SessionFormationPersonne : {}", id);
        return sessionFormationPersonneRepository.findById(id).map(sessionFormationPersonneMapper::toDto);
    }

    /**
     * Delete the sessionFormationPersonne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SessionFormationPersonne : {}", id);
        sessionFormationPersonneRepository.deleteById(id);
    }
}
