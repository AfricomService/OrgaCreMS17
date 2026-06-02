package com.orgacare.app.repository;

import com.orgacare.app.domain.Personne;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long>, JpaSpecificationExecutor<Personne> {
    @Query(
        "SELECT COALESCE(MAX(CAST(SUBSTRING(p.matricule, LENGTH(:prefix) + 1) AS int)), 0) FROM Personne p WHERE p.matricule LIKE CONCAT(:prefix, '%')"
    )
    int findMaxMatriculeNumber(@Param("prefix") String prefix);

    boolean existsByMatricule(String matricule);
    Optional<Personne> findByMatricule(String matricule);
    Optional<Personne> findByMatriculeIgnoreCase(String matricule);
    Optional<Personne> findByNomPrenom(String nomPrenom);

    Optional<Personne> findByEmailIgnoreCase(String email);
    //    List<Personne> findByMatriculeIn(Collection<String> matricules);
    List<Personne> findByMatriculeIn(List<String> matricules);

    // PersonneRepository.java — ajouter cette méthode
    @Query("SELECT p FROM Personne p WHERE LOWER(p.matricule) IN :matricules")
    List<Personne> findByMatriculeInIgnoreCase(@Param("matricules") List<String> matricules);

    Optional<Personne> findByAffectationId(Long affectationId);
}
