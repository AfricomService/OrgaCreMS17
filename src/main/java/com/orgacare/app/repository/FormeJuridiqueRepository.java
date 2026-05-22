package com.orgacare.app.repository;

import com.orgacare.app.domain.FormeJuridique;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormeJuridique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormeJuridiqueRepository extends JpaRepository<FormeJuridique, Long> {}
