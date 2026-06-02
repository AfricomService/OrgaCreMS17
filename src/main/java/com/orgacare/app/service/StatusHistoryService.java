package com.orgacare.app.service;

import com.orgacare.app.domain.StatusHistory;
import com.orgacare.app.repository.StatusHistoryRepository;
import com.orgacare.app.service.dto.StatusHistoryDTO;
import com.orgacare.app.service.mapper.StatusHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StatusHistory}.
 */
@Service
@Transactional
public class StatusHistoryService {

    private final Logger log = LoggerFactory.getLogger(StatusHistoryService.class);

    private final StatusHistoryRepository statusHistoryRepository;

    private final StatusHistoryMapper statusHistoryMapper;

    public StatusHistoryService(StatusHistoryRepository statusHistoryRepository, StatusHistoryMapper statusHistoryMapper) {
        this.statusHistoryRepository = statusHistoryRepository;
        this.statusHistoryMapper = statusHistoryMapper;
    }

    /**
     * Save a statusHistory.
     *
     * @param statusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public StatusHistoryDTO save(StatusHistoryDTO statusHistoryDTO) {
        log.debug("Request to save StatusHistory : {}", statusHistoryDTO);
        StatusHistory statusHistory = statusHistoryMapper.toEntity(statusHistoryDTO);
        statusHistory = statusHistoryRepository.save(statusHistory);
        return statusHistoryMapper.toDto(statusHistory);
    }

    /**
     * Get all the statusHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StatusHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StatusHistories");
        return statusHistoryRepository.findAll(pageable).map(statusHistoryMapper::toDto);
    }

    /**
     * Get one statusHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StatusHistoryDTO> findOne(Long id) {
        log.debug("Request to get StatusHistory : {}", id);
        return statusHistoryRepository.findById(id).map(statusHistoryMapper::toDto);
    }

    /**
     * Delete the statusHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StatusHistory : {}", id);
        statusHistoryRepository.deleteById(id);
    }
}
