package com.orgacare.app.service;

import com.orgacare.app.domain.ThemeFormation;
import com.orgacare.app.repository.ThemeFormationRepository;
import com.orgacare.app.service.dto.ThemeFormationDTO;
import com.orgacare.app.service.mapper.ThemeFormationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ThemeFormation}.
 */
@Service
@Transactional
public class ThemeFormationService {

    private final Logger log = LoggerFactory.getLogger(ThemeFormationService.class);

    private final ThemeFormationRepository themeFormationRepository;

    private final ThemeFormationMapper themeFormationMapper;

    public ThemeFormationService(ThemeFormationRepository themeFormationRepository, ThemeFormationMapper themeFormationMapper) {
        this.themeFormationRepository = themeFormationRepository;
        this.themeFormationMapper = themeFormationMapper;
    }

    /**
     * Save a themeFormation.
     *
     * @param themeFormationDTO the entity to save.
     * @return the persisted entity.
     */
    public ThemeFormationDTO save(ThemeFormationDTO themeFormationDTO) {
        log.debug("Request to save ThemeFormation : {}", themeFormationDTO);
        ThemeFormation themeFormation = themeFormationMapper.toEntity(themeFormationDTO);
        themeFormation = themeFormationRepository.save(themeFormation);
        return themeFormationMapper.toDto(themeFormation);
    }

    /**
     * Partially update a themeFormation.
     *
     * @param themeFormationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ThemeFormationDTO> partialUpdate(ThemeFormationDTO themeFormationDTO) {
        log.debug("Request to partially update ThemeFormation : {}", themeFormationDTO);

        return themeFormationRepository
            .findById(themeFormationDTO.getId())
            .map(existingThemeFormation -> {
                themeFormationMapper.partialUpdate(existingThemeFormation, themeFormationDTO);

                return existingThemeFormation;
            })
            .map(themeFormationRepository::save)
            .map(themeFormationMapper::toDto);
    }

    /**
     * Get all the themeFormations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeFormationDTO> findAll() {
        log.debug("Request to get all ThemeFormations");
        return themeFormationRepository
            .findAll()
            .stream()
            .map(themeFormationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Search themeFormations by theme, status or comments (case-insensitive).
     *
     * @param search the text to look for.
     * @return the list of matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeFormationDTO> search(String search, String status) {
        log.debug("Request to search ThemeFormations : {} / {}", search, status);
        return themeFormationRepository
            .search(search == null ? "" : search.trim(), status == null ? "" : status.trim())
            .stream()
            .map(themeFormationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one themeFormation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ThemeFormationDTO> findOne(Long id) {
        log.debug("Request to get ThemeFormation : {}", id);
        return themeFormationRepository.findById(id).map(themeFormationMapper::toDto);
    }

    /**
     * Delete the themeFormation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ThemeFormation : {}", id);
        themeFormationRepository.deleteById(id);
    }
}
