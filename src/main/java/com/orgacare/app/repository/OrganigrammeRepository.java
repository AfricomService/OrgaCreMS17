package com.orgacare.app.repository;

import com.orgacare.app.domain.Organigramme;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Organigramme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganigrammeRepository extends JpaRepository<Organigramme, Long> {
    List<Organigramme> findBySocieteId(Long societeId);

    Organigramme findByCode(String code);
}
