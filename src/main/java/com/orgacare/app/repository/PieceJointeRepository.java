package com.orgacare.app.repository;

import com.orgacare.app.domain.PieceJointe;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PieceJointe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PieceJointeRepository extends JpaRepository<PieceJointe, Long> {
    List<PieceJointe> findAllBySessionId(Long sessionId);

    List<PieceJointe> findAllByFormationId(Long formationId);
}
