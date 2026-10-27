package com.orgacare.app.repository;

import com.orgacare.app.domain.Salaire;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Salaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaireRepository extends JpaRepository<Salaire, Long> {
    /** Historique d'une personne, du plus récent au plus ancien. */
    List<Salaire> findAllByPersonneIdOrderByDateDesc(Long personneId);

    List<Salaire> findAllByIdIn(Collection<Long> ids);
}
