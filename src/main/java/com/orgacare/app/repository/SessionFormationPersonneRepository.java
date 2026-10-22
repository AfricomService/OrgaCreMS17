package com.orgacare.app.repository;

import com.orgacare.app.domain.SessionFormationPersonne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionFormationPersonne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionFormationPersonneRepository extends JpaRepository<SessionFormationPersonne, Long> {}
