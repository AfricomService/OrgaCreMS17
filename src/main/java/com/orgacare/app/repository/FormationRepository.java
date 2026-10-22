package com.orgacare.app.repository;

import com.orgacare.app.domain.Formation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long>, JpaSpecificationExecutor<Formation> {
    List<Formation> findBySessionId(Long sessionId);

    /** Ids distincts des bénéficiaires ayant au moins une formation (pour la liste de recherche). */
    @Query("select distinct f.personneId from Formation f where f.personneId is not null")
    List<Long> findDistinctPersonneIds();
}
