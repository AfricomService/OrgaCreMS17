package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.SessionFormationPersonne;
import com.orgacare.app.service.dto.SessionFormationPersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SessionFormationPersonne} and its DTO {@link SessionFormationPersonneDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SessionFormationPersonneMapper extends EntityMapper<SessionFormationPersonneDTO, SessionFormationPersonne> {}
