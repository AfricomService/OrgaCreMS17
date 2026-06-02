package com.orgacare.app.service;

import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.repository.OrganigrammeRepository;
import com.orgacare.app.service.dto.OrganigrammeCodeDTO;
import com.orgacare.app.service.dto.OrganigrammeDTO;
import com.orgacare.app.service.mapper.OrganigrammeMapper;
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
 * Service Implementation for managing {@link Organigramme}.
 */
@Service
@Transactional
public class OrganigrammeService {

    private final Logger log = LoggerFactory.getLogger(OrganigrammeService.class);

    private final OrganigrammeRepository organigrammeRepository;

    private final OrganigrammeMapper organigrammeMapper;

    public OrganigrammeService(OrganigrammeRepository organigrammeRepository, OrganigrammeMapper organigrammeMapper) {
        this.organigrammeRepository = organigrammeRepository;
        this.organigrammeMapper = organigrammeMapper;
    }

    /**
     * Save a organigramme.
     *
     * @param organigrammeDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganigrammeDTO save(OrganigrammeDTO organigrammeDTO) {
        log.debug("Request to save Organigramme : {}", organigrammeDTO);
        Organigramme organigramme = organigrammeMapper.toEntity(organigrammeDTO);
        organigramme = organigrammeRepository.save(organigramme);
        return organigrammeMapper.toDto(organigramme);
    }

    /**
     * Get all the organigrammes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganigrammeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organigrammes");
        return organigrammeRepository.findAll(pageable).map(organigrammeMapper::toDto);
    }

    /**
     * Get one organigramme by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganigrammeDTO> findOne(Long id) {
        log.debug("Request to get Organigramme : {}", id);
        return organigrammeRepository.findById(id).map(organigrammeMapper::toDto);
    }

    /**
     * Delete the organigramme by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Organigramme : {}", id);
        organigrammeRepository.deleteById(id);
    }

    public List<Organigramme> findBySocieteId(Long societeId) {
        return organigrammeRepository.findBySocieteId(societeId);
    }

    public List<OrganigrammeDTO> findAllListOrganigramme() {
        return organigrammeRepository.findAll().stream().map(organigrammeMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrganigrammeCodeDTO> getAllOrganigrammesCodes() {
        log.debug("Request to get all Organigrammes codes");
        return organigrammeRepository.getAllOrganigrammesCodes();
    }
}
