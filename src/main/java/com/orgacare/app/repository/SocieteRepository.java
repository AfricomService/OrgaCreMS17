package com.orgacare.app.repository;

import com.orgacare.app.domain.Societe;
import com.orgacare.app.domain.enumeration.Etat;
import com.orgacare.app.service.dto.SocieteLightDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Societe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocieteRepository extends JpaRepository<Societe, Long> {
    /**
     * Sociétés des départements auxquels la personne (matricule) est rattachée.
     * Couvre departement.societeId ET departement.organigramme.societe.
     */
    @Query(
        "select distinct new com.orgacare.app.service.dto.SocieteLightDTO(" +
        "s.id, s.raisonSociale, s.abreviation, s.codeSociete, s.codeOrganigramme, s.etat) " +
        "from Affectation a " +
        "join a.societe s, Personne p " +
        "where a.personneId = p.id " +
        "and lower(p.matricule) = lower(:matricule) " +
        "and a.etat <> :etat " +
        "order by s.raisonSociale"
    )
    List<SocieteLightDTO> findAllByPersonneMatricule(@Param("matricule") String matricule, @Param("etat") Etat etat);
}
