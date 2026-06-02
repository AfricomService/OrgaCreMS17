package com.orgacare.app.repository;

import com.orgacare.app.domain.TypeContrat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeContrat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeContratRepository extends JpaRepository<TypeContrat, Long> {
    Optional<TypeContrat> findByNomIgnoreCase(String nom);
    List<TypeContrat> findByNomContainingIgnoreCase(String nom);
}
