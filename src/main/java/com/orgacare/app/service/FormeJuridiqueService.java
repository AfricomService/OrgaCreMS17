package com.orgacare.app.service;

import com.orgacare.app.domain.FormeJuridique;
import com.orgacare.app.repository.FormeJuridiqueRepository;
import com.orgacare.app.service.dto.FormeJuridiqueDTO;
import com.orgacare.app.service.mapper.FormeJuridiqueMapper;
import java.util.LinkedList;
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
 * Service Implementation for managing {@link FormeJuridique}.
 */
@Service
@Transactional
public class FormeJuridiqueService {

    private final Logger log = LoggerFactory.getLogger(FormeJuridiqueService.class);

    private final FormeJuridiqueRepository formeJuridiqueRepository;

    private final FormeJuridiqueMapper formeJuridiqueMapper;

    public FormeJuridiqueService(FormeJuridiqueRepository formeJuridiqueRepository, FormeJuridiqueMapper formeJuridiqueMapper) {
        this.formeJuridiqueRepository = formeJuridiqueRepository;
        this.formeJuridiqueMapper = formeJuridiqueMapper;
    }

    /**
     * Save a formeJuridique.
     *
     * @param formeJuridiqueDTO the entity to save.
     * @return the persisted entity.
     */
    public FormeJuridiqueDTO save(FormeJuridiqueDTO formeJuridiqueDTO) {
        log.debug("Request to save FormeJuridique : {}", formeJuridiqueDTO);
        FormeJuridique formeJuridique = formeJuridiqueMapper.toEntity(formeJuridiqueDTO);
        formeJuridique = formeJuridiqueRepository.save(formeJuridique);
        return formeJuridiqueMapper.toDto(formeJuridique);
    }

    /**
     * Get all the formeJuridiques.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FormeJuridiqueDTO> findAll() {
        log.debug("Request to get all FormeJuridiques");
        return formeJuridiqueRepository
            .findAll()
            .stream()
            .map(formeJuridiqueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one formeJuridique by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormeJuridiqueDTO> findOne(Long id) {
        log.debug("Request to get FormeJuridique : {}", id);
        return formeJuridiqueRepository.findById(id).map(formeJuridiqueMapper::toDto);
    }

    /**
     * Delete the formeJuridique by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FormeJuridique : {}", id);
        formeJuridiqueRepository.deleteById(id);
    }

    @Transactional
    public FormeJuridiqueDTO updateEtatToPassif(Long id) {
        log.debug("Request to set FormeJuridique to passive state : {}", id);

        Optional<FormeJuridique> formeJuridiqueOptional = formeJuridiqueRepository.findById(id);

        FormeJuridique formeJuridique = formeJuridiqueOptional.get();
        formeJuridique.setEtat("PASSIF");
        formeJuridique = formeJuridiqueRepository.save(formeJuridique);

        return formeJuridiqueMapper.toDto(formeJuridique);
    }
}
