package com.orgacare.app.service;

import com.orgacare.app.domain.AclSid;
import com.orgacare.app.repository.AclSidRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AclSid}.
 */
@Service
@Transactional
public class AclSidService {

    private final Logger log = LoggerFactory.getLogger(AclSidService.class);

    private final AclSidRepository aclSidRepository;

    public AclSidService(AclSidRepository aclSidRepository) {
        this.aclSidRepository = aclSidRepository;
    }

    /**
     * Save a aclSid.
     *
     * @param aclSid the entity to save.
     * @return the persisted entity.
     */
    public AclSid save(AclSid aclSid) {
        log.debug("Request to save AclSid : {}", aclSid);
        return aclSidRepository.save(aclSid);
    }

    /**
     * Partially update a aclSid.
     *
     * @param aclSid the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AclSid> partialUpdate(AclSid aclSid) {
        log.debug("Request to partially update AclSid : {}", aclSid);

        return aclSidRepository
            .findById(aclSid.getId())
            .map(existingAclSid -> {
                if (aclSid.getSidType() != null) {
                    existingAclSid.setSidType(aclSid.getSidType());
                }
                if (aclSid.getSidValue() != null) {
                    existingAclSid.setSidValue(aclSid.getSidValue());
                }
                if (aclSid.getSidKey() != null) {
                    existingAclSid.setSidKey(aclSid.getSidKey());
                }
                if (aclSid.getNomDescriptif() != null) {
                    existingAclSid.setNomDescriptif(aclSid.getNomDescriptif());
                }

                return existingAclSid;
            })
            .map(aclSidRepository::save);
    }

    /**
     * Get all the aclSids.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AclSid> findAll() {
        log.debug("Request to get all AclSids");
        return aclSidRepository.findAll();
    }

    /**
     * Get one aclSid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AclSid> findOne(Long id) {
        log.debug("Request to get AclSid : {}", id);
        return aclSidRepository.findById(id);
    }

    /**
     * Delete the aclSid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AclSid : {}", id);
        aclSidRepository.deleteById(id);
    }
}
