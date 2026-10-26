package com.orgacare.app.repository;

import com.orgacare.app.domain.SessionFormationPersonne;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionFormationPersonne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionFormationPersonneRepository extends JpaRepository<SessionFormationPersonne, Long> {
    List<SessionFormationPersonne> findBySessionId(Long sessionId);

    void deleteBySessionId(Long sessionId);
}
