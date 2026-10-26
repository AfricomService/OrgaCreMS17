package com.orgacare.app.repository;

import com.orgacare.app.domain.Absence;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Absence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByPersonneAbscentId(Long personneAbscentId);

    Page<Absence> findByIdIn(Collection<Long> ids, Pageable pageable);
}
