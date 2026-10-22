package com.orgacare.app.repository;

import com.orgacare.app.domain.SessionFormation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionFormationRepository extends JpaRepository<SessionFormation, Long>, JpaSpecificationExecutor<SessionFormation> {}
