package com.orgacare.app.repository;

import com.orgacare.app.domain.Contrat;
import feign.Param;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Contrat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long>, JpaSpecificationExecutor<Contrat> {
    List<Contrat> findByPersonneId(Long personneId);
    List<Contrat> findByPersonneIdAndStatus(Long personneId, String status);
    List<Optional<Contrat>> findAllByPersonneId(Long personneId);

    // Renommée pour éviter le conflit avec la surcharge ZonedDateTime
    @Query("SELECT c FROM Contrat c WHERE c.dateDebut BETWEEN :startDate AND :endDate")
    List<Contrat> findByDateDebutBetweenLocalDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Contrat> findAllByTypeContratIdIn(List<Long> typeContratIds);
    List<Contrat> findByStatus(String status);

    List<Contrat> findByDateDebutBefore(ZonedDateTime dateStart);
    List<Contrat> findByDateDebutAfter(ZonedDateTime dateEnd);
    List<Contrat> findByDateDebutBetween(ZonedDateTime dateStart, ZonedDateTime dateEnd);

    List<Contrat> findByDateFinBefore(ZonedDateTime dateStart);
    List<Contrat> findByDateFinAfter(ZonedDateTime dateEnd);
    List<Contrat> findByDateFinBetween(ZonedDateTime dateStart, ZonedDateTime dateEnd);

    @EntityGraph(attributePaths = { "personne", "typeContrat", "societe" })
    Page<Contrat> findAll(Specification<Contrat> spec, Pageable pageable);
}
