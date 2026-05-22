package com.orgacare.app.service;

import com.orgacare.app.domain.Fonction;
import com.orgacare.app.repository.FonctionRepository;
import com.orgacare.app.service.dto.FonctionDTO;
import com.orgacare.app.service.mapper.FonctionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fonction}.
 */
@Service
@Transactional
public class FonctionService {

    private final Logger log = LoggerFactory.getLogger(FonctionService.class);

    private final FonctionRepository fonctionRepository;

    private final FonctionMapper fonctionMapper;

    public FonctionService(FonctionRepository fonctionRepository, FonctionMapper fonctionMapper) {
        this.fonctionRepository = fonctionRepository;
        this.fonctionMapper = fonctionMapper;
    }

    /**
     * Save a fonction.
     *
     * @param fonctionDTO the entity to save.
     * @return the persisted entity.
     */
    public FonctionDTO save(FonctionDTO fonctionDTO) {
        log.debug("Request to save Fonction : {}", fonctionDTO);
        Fonction fonction = fonctionMapper.toEntity(fonctionDTO);
        fonction = fonctionRepository.save(fonction);
        return fonctionMapper.toDto(fonction);
    }

    /**
     * Partially update a fonction.
     *
     * @param fonctionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FonctionDTO> partialUpdate(FonctionDTO fonctionDTO) {
        log.debug("Request to partially update Fonction : {}", fonctionDTO);

        return fonctionRepository
            .findById(fonctionDTO.getId())
            .map(existingFonction -> {
                fonctionMapper.partialUpdate(existingFonction, fonctionDTO);

                return existingFonction;
            })
            .map(fonctionRepository::save)
            .map(fonctionMapper::toDto);
    }

    /**
     * Get all the fonctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FonctionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fonctions");
        return fonctionRepository.findAll(pageable).map(fonctionMapper::toDto);
    }

    /**
     * Get one fonction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FonctionDTO> findOne(Long id) {
        log.debug("Request to get Fonction : {}", id);
        return fonctionRepository.findById(id).map(fonctionMapper::toDto);
    }

    /**
     * Delete the fonction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fonction : {}", id);
        fonctionRepository.deleteById(id);
    }
}
