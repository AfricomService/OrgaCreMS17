package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Formation;
import com.orgacare.app.service.dto.FormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {}
