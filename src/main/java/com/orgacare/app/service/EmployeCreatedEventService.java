package com.orgacare.app.service;

import com.orgacare.app.domain.EmployeCreatedEvent;
import com.orgacare.app.repository.EmployeCreatedEventRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeCreatedEvent}.
 */
@Service
@Transactional
public class EmployeCreatedEventService {

    private final Logger log = LoggerFactory.getLogger(EmployeCreatedEventService.class);

    private final EmployeCreatedEventRepository employeCreatedEventRepository;

    public EmployeCreatedEventService(EmployeCreatedEventRepository employeCreatedEventRepository) {
        this.employeCreatedEventRepository = employeCreatedEventRepository;
    }

    /**
     * Save a employeCreatedEvent.
     *
     * @param employeCreatedEvent the entity to save.
     * @return the persisted entity.
     */
    public EmployeCreatedEvent save(EmployeCreatedEvent employeCreatedEvent) {
        log.debug("Request to save EmployeCreatedEvent : {}", employeCreatedEvent);
        return employeCreatedEventRepository.save(employeCreatedEvent);
    }

    /**
     * Partially update a employeCreatedEvent.
     *
     * @param employeCreatedEvent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeCreatedEvent> partialUpdate(EmployeCreatedEvent employeCreatedEvent) {
        log.debug("Request to partially update EmployeCreatedEvent : {}", employeCreatedEvent);

        return employeCreatedEventRepository
            .findById(employeCreatedEvent.getId())
            .map(existingEmployeCreatedEvent -> {
                if (employeCreatedEvent.getMatricule() != null) {
                    existingEmployeCreatedEvent.setMatricule(employeCreatedEvent.getMatricule());
                }
                if (employeCreatedEvent.getNomPrenom() != null) {
                    existingEmployeCreatedEvent.setNomPrenom(employeCreatedEvent.getNomPrenom());
                }
                if (employeCreatedEvent.getEmail() != null) {
                    existingEmployeCreatedEvent.setEmail(employeCreatedEvent.getEmail());
                }
                if (employeCreatedEvent.getUserId() != null) {
                    existingEmployeCreatedEvent.setUserId(employeCreatedEvent.getUserId());
                }

                return existingEmployeCreatedEvent;
            })
            .map(employeCreatedEventRepository::save);
    }

    /**
     * Get all the employeCreatedEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeCreatedEvent> findAll() {
        log.debug("Request to get all EmployeCreatedEvents");
        return employeCreatedEventRepository.findAll();
    }

    /**
     * Get one employeCreatedEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeCreatedEvent> findOne(Long id) {
        log.debug("Request to get EmployeCreatedEvent : {}", id);
        return employeCreatedEventRepository.findById(id);
    }

    /**
     * Delete the employeCreatedEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeCreatedEvent : {}", id);
        employeCreatedEventRepository.deleteById(id);
    }
}
