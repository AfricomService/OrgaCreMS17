package com.orgacare.app.service;

import com.orgacare.app.domain.AclEntry;
import com.orgacare.app.repository.AclEntryRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AclEntry}.
 */
@Service
@Transactional
public class AclEntryService {

    private final Logger log = LoggerFactory.getLogger(AclEntryService.class);

    private final AclEntryRepository aclEntryRepository;

    public AclEntryService(AclEntryRepository aclEntryRepository) {
        this.aclEntryRepository = aclEntryRepository;
    }

    /**
     * Save a aclEntry.
     *
     * @param aclEntry the entity to save.
     * @return the persisted entity.
     */
    public AclEntry save(AclEntry aclEntry) {
        log.debug("Request to save AclEntry : {}", aclEntry);
        return aclEntryRepository.save(aclEntry);
    }

    /**
     * Partially update a aclEntry.
     *
     * @param aclEntry the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AclEntry> partialUpdate(AclEntry aclEntry) {
        log.debug("Request to partially update AclEntry : {}", aclEntry);

        return aclEntryRepository
            .findById(aclEntry.getId())
            .map(existingAclEntry -> {
                if (aclEntry.getObjectType() != null) {
                    existingAclEntry.setObjectType(aclEntry.getObjectType());
                }
                if (aclEntry.getObjectId() != null) {
                    existingAclEntry.setObjectId(aclEntry.getObjectId());
                }
                if (aclEntry.getSidId() != null) {
                    existingAclEntry.setSidId(aclEntry.getSidId());
                }
                if (aclEntry.getCanRead() != null) {
                    existingAclEntry.setCanRead(aclEntry.getCanRead());
                }
                if (aclEntry.getCanWrite() != null) {
                    existingAclEntry.setCanWrite(aclEntry.getCanWrite());
                }

                return existingAclEntry;
            })
            .map(aclEntryRepository::save);
    }

    /**
     * Get all the aclEntries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AclEntry> findAll() {
        log.debug("Request to get all AclEntries");
        return aclEntryRepository.findAll();
    }

    /**
     * Get one aclEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AclEntry> findOne(Long id) {
        log.debug("Request to get AclEntry : {}", id);
        return aclEntryRepository.findById(id);
    }

    /**
     * Delete the aclEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AclEntry : {}", id);
        aclEntryRepository.deleteById(id);
    }
}
