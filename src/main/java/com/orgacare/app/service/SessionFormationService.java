package com.orgacare.app.service;

import com.orgacare.app.domain.SessionFormation;
import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.service.dto.SessionFormationDTO;
import com.orgacare.app.service.mapper.SessionFormationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionFormation}.
 */
@Service
@Transactional
public class SessionFormationService {

    private final Logger log = LoggerFactory.getLogger(SessionFormationService.class);

    private final SessionFormationRepository sessionFormationRepository;

    private final SessionFormationMapper sessionFormationMapper;

    public SessionFormationService(SessionFormationRepository sessionFormationRepository, SessionFormationMapper sessionFormationMapper) {
        this.sessionFormationRepository = sessionFormationRepository;
        this.sessionFormationMapper = sessionFormationMapper;
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
        sessionFormationRepository.deleteById(id);
    }
}
