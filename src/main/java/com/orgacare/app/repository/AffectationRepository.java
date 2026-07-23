package com.orgacare.app.repository;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Affectation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    List<Affectation> findByDepartementId(Long departementId);

    // NOUVEAU
    List<Affectation> findByPersonneId(Long personneId);
    List<Affectation> findBySocieteId(Long societeId);

    List<Affectation> findByDepartementIdIn(List<Long> departementIds);

    List<Affectation> findByDepartementIdAndType(Long departementId, TypeAffectation type);
}
