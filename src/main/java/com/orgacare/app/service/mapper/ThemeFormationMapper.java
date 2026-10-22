package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.ThemeFormation;
import com.orgacare.app.service.dto.ThemeFormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ThemeFormation} and its DTO {@link ThemeFormationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThemeFormationMapper extends EntityMapper<ThemeFormationDTO, ThemeFormation> {}
