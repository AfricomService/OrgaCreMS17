package com.orgacare.app.repository;

import com.orgacare.app.domain.Departement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Departement entity.
 */
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    @Query(
        value = "select distinct departement from Departement departement left join fetch departement.personnes",
        countQuery = "select count(distinct departement) from Departement departement"
    )
    Page<Departement> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct departement from Departement departement left join fetch departement.personnes")
    List<Departement> findAllWithEagerRelationships();

    @Query("select departement from Departement departement left join fetch departement.personnes where departement.id =:id")
    Optional<Departement> findOneWithEagerRelationships(@Param("id") Long id);
}
