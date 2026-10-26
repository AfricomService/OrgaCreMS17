package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.SessionFormationChefProjet;
import com.orgacare.app.service.dto.SessionFormationChefProjetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SessionFormationChefProjet} and its DTO {@link SessionFormationChefProjetDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SessionFormationChefProjetMapper extends EntityMapper<SessionFormationChefProjetDTO, SessionFormationChefProjet> {}
