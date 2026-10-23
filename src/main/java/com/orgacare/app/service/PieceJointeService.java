package com.orgacare.app.service;

import com.orgacare.app.domain.PieceJointe;
import com.orgacare.app.repository.PieceJointeRepository;
import com.orgacare.app.service.dto.PieceJointeDTO;
import com.orgacare.app.service.mapper.PieceJointeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeService.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    public PieceJointeService(PieceJointeRepository pieceJointeRepository, PieceJointeMapper pieceJointeMapper) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
    }

    /**
     * Save a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJointeDTO save(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to save PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Partially update a pieceJointe.
     *
     * @param pieceJointeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointeDTO);

        return pieceJointeRepository
            .findById(pieceJointeDTO.getId())
            .map(existingPieceJointe -> {
                pieceJointeMapper.partialUpdate(existingPieceJointe, pieceJointeDTO);

                return existingPieceJointe;
            })
            .map(pieceJointeRepository::save)
            .map(pieceJointeMapper::toDto);
    }

    /**
     * Get all the pieceJointes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findAll() {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll().stream().map(pieceJointeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pieceJointe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PieceJointeDTO> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findById(id).map(pieceJointeMapper::toDto);
    }

    /**
     * Delete the pieceJointe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }
}
