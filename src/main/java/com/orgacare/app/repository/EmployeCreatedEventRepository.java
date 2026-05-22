package com.orgacare.app.repository;

import com.orgacare.app.domain.EmployeCreatedEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmployeCreatedEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeCreatedEventRepository extends JpaRepository<EmployeCreatedEvent, Long> {}
