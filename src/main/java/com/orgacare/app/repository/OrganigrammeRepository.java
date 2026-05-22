package com.orgacare.app.repository;

import com.orgacare.app.domain.Organigramme;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Organigramme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganigrammeRepository extends JpaRepository<Organigramme, Long> {}
