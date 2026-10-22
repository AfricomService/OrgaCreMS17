package com.orgacare.app.repository;

import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.domain.enumeration.TypeAffectation;
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

    Optional<Personne> findFirstByUserId(String userId);

    /** Toutes les personnes affectées à une société (directement ou via département / organigramme). */
    @Query(
        "select distinct p from Affectation a " +
        "left join a.departement d " +
        "left join d.organigramme o " +
        "left join o.societe os " +
        "left join a.societe s, Personne p " +
        "where a.personneId = p.id " +
        "and (s.id = :societeId or d.societeId = :societeId or os.id = :societeId) " +
        "and a.etat <> :etat " +
        "order by p.nomPrenom"
    )
    List<Personne> findBySocieteAffectation(@Param("societeId") Long societeId, @Param("etat") Etat etat);

    /** Idem, filtré sur le type d'affectation (ex : CHEF). */
    @Query(
        "select distinct p from Affectation a " +
        "left join a.departement d " +
        "left join d.organigramme o " +
        "left join o.societe os " +
        "left join a.societe s, Personne p " +
        "where a.personneId = p.id " +
        "and (s.id = :societeId or d.societeId = :societeId or os.id = :societeId) " +
        "and a.type = :type " +
        "and a.etat <> :etat " +
        "order by p.nomPrenom"
    )
    List<Personne> findBySocieteAffectationAndType(
        @Param("societeId") Long societeId,
        @Param("type") TypeAffectation type,
        @Param("etat") Etat etat
    );
}
