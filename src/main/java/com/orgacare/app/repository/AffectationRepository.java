package com.orgacare.app.repository;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.enumeration.TypeAffectation;
import feign.Param;
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

    // Affectation n'a pas de champ personne → @Query obligatoire
    @Query(
        """
        SELECT a FROM Affectation a
        WHERE a.departement IN (
            SELECT d FROM Departement d
            JOIN d.personnes p
            WHERE p.id = :personneId
        )
    """
    )
    List<Affectation> findByPersonneId(@Param("personneId") Long personneId);

    List<Affectation> findByDepartementIdIn(List<Long> departementIds);

    List<Affectation> findByDepartementIdAndType(Long departementId, TypeAffectation type);
}
