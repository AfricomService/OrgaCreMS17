package com.orgacare.app.repository;

import com.orgacare.app.domain.Site;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Site entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findBySocieteId(Long societeId);
}
