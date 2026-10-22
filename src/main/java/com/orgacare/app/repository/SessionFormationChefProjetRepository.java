package com.orgacare.app.repository;

import com.orgacare.app.domain.SessionFormationChefProjet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionFormationChefProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionFormationChefProjetRepository extends JpaRepository<SessionFormationChefProjet, Long> {}
