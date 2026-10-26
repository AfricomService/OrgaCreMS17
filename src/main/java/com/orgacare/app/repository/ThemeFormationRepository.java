package com.orgacare.app.repository;

import com.orgacare.app.domain.ThemeFormation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ThemeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeFormationRepository extends JpaRepository<ThemeFormation, Long> {
    @Query(
        "select t from ThemeFormation t " +
        "where (lower(t.theme) like lower(concat('%', :search, '%')) " +
        "or lower(t.comments) like lower(concat('%', :search, '%'))) " +
        "and (:status = '' or t.status = :status)"
    )
    List<ThemeFormation> search(@Param("search") String search, @Param("status") String status);
}
