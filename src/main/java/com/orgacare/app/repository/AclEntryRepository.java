package com.orgacare.app.repository;

import com.orgacare.app.domain.AclEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AclEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
    Optional<AclEntry> findByObjectTypeAndObjectIdAndSidId(String objectType, Long objectId, Long sidId);

    List<AclEntry> findByObjectTypeAndObjectId(String objectType, Long objectId);

    List<AclEntry> findByObjectTypeAndSidIdIn(String objectType, List<Long> sidIds);

    void deleteByObjectTypeAndObjectId(String objectType, Long objectId);
}
