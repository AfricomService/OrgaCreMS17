package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.SessionFormation;
import com.orgacare.app.service.dto.SessionFormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SessionFormation} and its DTO {@link SessionFormationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SessionFormationMapper extends EntityMapper<SessionFormationDTO, SessionFormation> {}
