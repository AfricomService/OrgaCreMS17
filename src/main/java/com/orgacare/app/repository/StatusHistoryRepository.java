package com.orgacare.app.repository;

import com.orgacare.app.domain.StatusHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StatusHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {}
