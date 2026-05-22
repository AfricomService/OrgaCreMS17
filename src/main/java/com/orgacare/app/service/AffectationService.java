package com.orgacare.app.service;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.repository.AffectationRepository;
import com.orgacare.app.service.dto.AffectationDTO;
import com.orgacare.app.service.mapper.AffectationMapper;
import java.util.Optional;
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

    private final AffectationRepository affectationRepository;

    private final AffectationMapper affectationMapper;

    public AffectationService(AffectationRepository affectationRepository, AffectationMapper affectationMapper) {
        this.affectationRepository = affectationRepository;
        this.affectationMapper = affectationMapper;
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
     * Partially update a affectation.
     *
     * @param affectationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AffectationDTO> partialUpdate(AffectationDTO affectationDTO) {
        log.debug("Request to partially update Affectation : {}", affectationDTO);

        return affectationRepository
            .findById(affectationDTO.getId())
            .map(existingAffectation -> {
                affectationMapper.partialUpdate(existingAffectation, affectationDTO);

                return existingAffectation;
            })
            .map(affectationRepository::save)
            .map(affectationMapper::toDto);
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
}
