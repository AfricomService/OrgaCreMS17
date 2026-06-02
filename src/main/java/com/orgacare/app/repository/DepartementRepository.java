package com.orgacare.app.repository;

import com.orgacare.app.domain.Departement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    List<Departement> findByOrganigrammeId(Long organigrammeId);

    @Query(
        "SELECT COALESCE(MAX(CAST(SUBSTRING(d.code, LENGTH(:prefix) + 1) AS int)), 0) FROM Departement d WHERE d.code LIKE CONCAT(:prefix, '%')"
    )
    int findMaxCodeNumber(@Param("prefix") String prefix);

    boolean existsByCode(String code);

    // CORRIGÉ : Departement n'a pas de champ societe direct
    // navigation via organigramme.societe.id
    @Query("SELECT d FROM Departement d WHERE d.organigramme.societe.id = :societeId AND d.organigramme.id = :organigrammeId")
    List<Departement> findBySocieteIdAndOrganigrammeId(@Param("societeId") Long societeId, @Param("organigrammeId") Long organigrammeId);

    List<Departement> findByOrganigramme_Id(Long organigrammeId);

    Optional<Departement> findByCode(String code);
    Optional<Departement> findByNom(String nom);
    Optional<Departement> findByCodeIgnoreCase(String code);

    List<Departement> findByDepartementParent(Departement parent);

    @Query("SELECT DISTINCT d FROM Departement d JOIN d.personnes p WHERE p.id = :personneId")
    List<Departement> findDistinctByPersonneId(@Param("personneId") Long personneId);

    List<Departement> findByCodeIn(Collection<String> codes);
}
