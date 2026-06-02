package com.orgacare.app.service;

import com.orgacare.app.domain.TypeContrat;
import com.orgacare.app.repository.TypeContratRepository;
import com.orgacare.app.service.dto.TypeContratDTO;
import com.orgacare.app.service.mapper.TypeContratMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeContrat}.
 */
@Service
@Transactional
public class TypeContratService {

    private final Logger log = LoggerFactory.getLogger(TypeContratService.class);

    private final TypeContratRepository typeContratRepository;

    private final TypeContratMapper typeContratMapper;

    public TypeContratService(TypeContratRepository typeContratRepository, TypeContratMapper typeContratMapper) {
        this.typeContratRepository = typeContratRepository;
        this.typeContratMapper = typeContratMapper;
    }

    /**
     * Save a typeContrat.
     *
     * @param typeContratDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeContratDTO save(TypeContratDTO typeContratDTO) {
        log.debug("Request to save TypeContrat : {}", typeContratDTO);
        TypeContrat typeContrat = typeContratMapper.toEntity(typeContratDTO);
        typeContrat = typeContratRepository.save(typeContrat);
        return typeContratMapper.toDto(typeContrat);
    }

    /**
     * Get all the typeContrats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeContratDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeContrats");
        return typeContratRepository.findAll(pageable).map(typeContratMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TypeContratDTO> findAll() {
        log.debug("Request to get all TypeContrats");
        return typeContratRepository.findAll().stream().map(typeContratMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one typeContrat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeContratDTO> findOne(Long id) {
        log.debug("Request to get TypeContrat : {}", id);
        return typeContratRepository.findById(id).map(typeContratMapper::toDto);
    }

    /**
     * Delete the typeContrat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeContrat : {}", id);
        typeContratRepository.deleteById(id);
    }

    public List<TypeContratDTO> findByNomContainingIgnoreCase(String nom) {
        return typeContratRepository.findByNomContainingIgnoreCase(nom).stream().map(typeContratMapper::toDto).collect(Collectors.toList());
    }
}
