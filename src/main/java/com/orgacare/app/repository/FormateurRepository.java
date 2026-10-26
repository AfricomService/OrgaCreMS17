package com.orgacare.app.repository;

import com.orgacare.app.domain.Formateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    @Query(
        "select f from Formateur f " +
        "where (lower(f.nomPrenom) like lower(concat('%', :search, '%')) " +
        "or lower(f.cin) like lower(concat('%', :search, '%')) " +
        "or lower(f.email) like lower(concat('%', :search, '%')) " +
        "or lower(f.telephone) like lower(concat('%', :search, '%')) " +
        "or lower(f.comments) like lower(concat('%', :search, '%'))) " +
        "and (:status = '' or f.status = :status)"
    )
    Page<Formateur> search(@Param("search") String search, @Param("status") String status, Pageable pageable);
}
